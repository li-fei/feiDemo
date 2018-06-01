package com.fei.demo.uart;

import android.util.Log;

import com.fei.demo.utils.ByteUtils;
import com.fei.demo.utils.CrcUtils;

import java.util.LinkedList;
import java.util.Queue;

public class UartDateProtocol implements UartDateReceivedListener{
    private MessageManager manager = new MessageManager();
    ControllerParseInterface controllerParseInterface;
    UartDateProtocol(ControllerParseInterface controllerParseInterface){
        this.controllerParseInterface = controllerParseInterface;
        Uart.getInstance().setUartDateReceivedListener(this);
    }

    /**
     * Called when some data arrived.
     *
     * @param data   The data buffer of read from connection.
     * @param offset The offset of data.
     * @param length The length of received.
     */
    @Override
    public void onReceive(byte[] data, int offset, int length) {
        if (length > 0) {
            manager.read(data, offset, length);
        }
        Message message = null;
        while ((message = manager.popMessage()) != null) {
            byte[] body = message.body;
            Log.e("yuneec0","---------" + ByteUtils.byteArrayToHexString(body) + "  -->" + body.length);
            byte[] bs = MessageManager.pack(body);
            Log.e("yuneec0","*********" + ByteUtils.byteArrayToHexString(bs) + "  -->" + body.length);
            controllerParseInterface.toCommandData(bs,0,bs.length);

        }
        message = null;
    }

    public static class Message {
        public byte[] body = null;
        public byte check;
        // 读到第几位 0表示没读
        protected int i = READ_BODY_NOT_START;
        public static final int READ_BODY_NOT_START = 0;
        public static final int READ_BODY_FINISHED = -1;

        protected Message() {
            super();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (byte b : body) {
                sb.append(b).append(',');
            }
            sb.append("   校验位：" + check);
            return sb.toString();
        }
    }

    public static class MessageManager {
        public static final byte[] Header = {0x55, 0x55};
        public static final int Dev_Time = 1000;
        private Queue<Message> queue = new LinkedList<>();
        private Message unFinished;
        private boolean hasMore = false;
        private byte more;
        private long receivedTime;

        private int readSize(byte[] bs, int offset) {
            return bs[offset] & 0xff;
        }

        /**
         * @param messages 数据写入对象
         * @param bs       读入数据
         * @param offset   偏移量 从bs的offset位置开始读 offset从0开始的
         * @param start    偏移量 从message的start位置开始写 start从0开始
         * @return 返回的int表示写入的数据长度
         */
        private int readBody(Message messages, byte[] bs, int offset, int length, int start) {
            int bSize = messages.body.length;
            int bNeedSize = bSize - start;
            int len = length > bNeedSize ? bNeedSize : length;
            int i = 0;
            for (; i < len; i++) {
                messages.body[i + start] = bs[offset + i];
            }
            messages.i = start + i;
            if (messages.i == bSize) {
                messages.i = Message.READ_BODY_FINISHED;
            }
            return len + start;
        }

        /**
         * 读取检验位
         *
         * @param messages
         * @param bs       数据包
         * @param offset
         */
        private void readCheck(Message messages, byte[] bs, int offset) {
            messages.check = bs[offset];
        }

        /**
         * 检验body
         *
         * @param message
         * @return
         */
        public static boolean checkBody(Message message) {
            return CrcUtils.crc8Table(message.body) == message.check;
        }

        public void read(byte[] bs) {
            read(bs, 0, bs.length);
        }

        public void read(byte[] bs, int offset, int len) {
            long now = System.currentTimeMillis();
            if (now - receivedTime > Dev_Time) {
                unFinished = null;
                hasMore = false;
            }
            receivedTime = now;
            try {
                analysis(bs, offset, len);
            } catch (Exception e) {
                Log.e("feiDemo","An error occurred while parsing datas! "+e.getMessage());
            }
        }

        /**
         * 解析数据包
         *
         * @param bs     byte[]
         * @param offset 数组开始位置偏移
         */
        private void analysis(byte[] bs, int offset, int length) {
            if (offset < 0 || length <= 0 || offset + length > bs.length) {
                return;
            }
            if (unFinished != null) {
                // body尚未初始化(还没有读消息体的长度位)
                if (unFinished.body == null) {
                    int size = readSize(bs, offset);
                    unFinished.body = new byte[size - 1];
                    offset++;
                    length--;
                }
                int index = unFinished.i;
                int len;
                if (index == Message.READ_BODY_FINISHED) {
                    len = index;
                } else {
                    len = readBody(unFinished, bs, offset, length, unFinished.i);
                }
                if (len < unFinished.body.length && index != -1) {
                    // 没有读完呢 等下个byte数组继续
                    return;
                } else {
                    int dv = len - index;
                    offset += dv;
                    length -= dv;
                    if (offset >= bs.length) {
                        return;
                    }
                    readCheck(unFinished, bs, offset);
                    offset++;
                    length--;
                    // 校验消息是否正确 不正确的话 需要重新取
                    if (checkBody(unFinished)) {
                        queue.add(unFinished);
                        unFinished = null;
                        // 一条指令读完了 试试还有没有数据读了
                        analysis(bs, offset, length);
                    } else {
                        // 校验错误 重构byte数组bs,将错误message的body和当前的bs想加 添加1位header[1]和校验位
                        // 然后 重新找消息头
                        int _eL = unFinished.i == Message.READ_BODY_FINISHED ? unFinished.body.length : unFinished.i;
                        byte[] _bs = new byte[_eL + length + 2];
                        int i = 1;
                        for (; i <= _eL; i++) {
                            _bs[i] = unFinished.body[i - 1];
                        }
                        _bs[i++] = unFinished.check;
                        for (int j = 0; j < length; j++) {
                            _bs[j + i] = bs[j + offset];
                        }
                        _bs[0] = Header[1];
                        unFinished = null;
                        analysis(_bs, 0, _bs.length);
                    }
                }
            } else {
                if (length > 1) {
                    boolean checkHeader = false;
                    int fp;
                    if (hasMore) {
                        checkHeader = Header[0] == more && Header[1] == bs[offset];
                        fp = 1;
                        hasMore = false;
                    } else {
                        checkHeader = Header[0] == bs[offset] && Header[1] == bs[offset + 1];
                        fp = 2;
                    }
                    if (checkHeader) {
                        offset += fp;
                        length -= fp;
                        Message message = new Message();
                        if (length > 0) {
                            int size = readSize(bs, offset);
                            message.body = new byte[size - 1];
                            offset++;
                            length--;
                            int len = readBody(message, bs, offset, length, 0);
                            // 这个表示消息不完整 需要等待下一个数据包继续解析
                            if ( (len < size - 1)|| len==0) {
                                unFinished = message;
                            } else {
                                offset += len;
                                length -= len;
                                if (offset >= bs.length) {
                                    unFinished = message;
                                    return;
                                }
                                readCheck(message, bs, offset);
                                // 校验消息是否正确 不正确的话 需要重新取
                                if (checkBody(message)) {
                                    queue.add(message);
                                    message = null;
                                    // 一条指令读完了 试试还有没有数据读了
                                    analysis(bs, offset + 1, length - 1);
                                } else {
                                    int dv = len + 2;
                                    offset -= dv;
                                    length += dv;
                                    message = null;
                                    analysis(bs, offset, length);
                                }
                            }
                        } else {
                            unFinished = message;
                        }
                    } else {
                        analysis(bs, offset + 1, length - 1);
                    }
                } else {
                    if (hasMore && more == Header[0] && bs[offset] == Header[1]) {
                        unFinished = new Message();
                        hasMore = false;
                    } else {
                        hasMore = true;
                        more = bs[offset];
                    }
                }
            }
        }

        /**
         * 包装正常的byte[]
         *
         * @param data
         * @return
         */
        public static byte[] pack(byte[] data) {
            if (data == null || data.length == 0) {
                return null;
            }
            byte[] bs = new byte[data.length + 4];
            bs[0] = Header[0];
            bs[1] = Header[1];
            bs[2] = (byte) (data.length + 1);
            int crci = CrcUtils.crc8Table(data);
            bs[bs.length - 1] = (byte) crci;
            System.arraycopy(data, 0, bs, 3, data.length);
            return bs;
        }

        /**
         * 从消息队列中取消息
         *
         * @return
         */
        public Message popMessage() {
            return queue.poll();
        }
    }

}
