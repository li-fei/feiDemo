package com.fei.demo.utils;

/**
 * Created by liangkui on 6/6/16.
 */

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ByteUtils {
    /**
     * Allow derivatives.
     */
    protected ByteUtils() {
    }

    /**
     * Converts a byte array to short value.
     * Equivalent to byteArrayToShort(paRawBytes, 0, pbBigEndian);
     *
     * @param paRawBytes  the byte array
     * @param pbBigEndian true if the bytes are in Big-endian order; false otherwise
     * @return short representation of the bytes
     */
    public static short byteArrayToShort(byte[] paRawBytes, boolean pbBigEndian) {
        return byteArrayToShort(paRawBytes, 0, pbBigEndian);
    }

    /**
     * Converts a portion of a byte array with given offset to short value.
     *
     * @param paRawBytes  the byte array
     * @param piOffset    offset in the original array to start reading bytes from
     * @param pbBigEndian true if the bytes are in Big-endian order; false otherwise
     * @return short representation of the bytes
     */
    public static short byteArrayToShort(byte[] paRawBytes, int piOffset, boolean pbBigEndian) {
        int iRetVal = -1;

        // TODO: revisit this: should we silently add missing byte and should
        // we ingnore excessing bytes?
        if (paRawBytes.length < piOffset + 2)
            return -1;

        int iLow;
        int iHigh;

        if (pbBigEndian) {
            iLow = paRawBytes[piOffset + 1];
            iHigh = paRawBytes[piOffset + 0];
        } else {
            iLow = paRawBytes[piOffset + 0];
            iHigh = paRawBytes[piOffset + 1];
        }

        // Merge high-order and low-order byte to form a 16-bit double value.
        iRetVal = (iHigh << 8) | (0xFF & iLow);

        return (short) iRetVal;
    }

    /**
     * Converts a byte array to int value.
     * Equivalent to intArrayToShort(paRawBytes, 0, pbBigEndian);
     *
     * @param paRawBytes  the byte array
     * @param pbBigEndian true if the bytes are in Big-endian order; false otherwise
     * @return int representation of the bytes
     */
    public static int byteArrayToInt(byte[] paRawBytes, boolean pbBigEndian) {
        return byteArrayToInt(paRawBytes, 0, pbBigEndian);
    }

    /**
     * Converts a portion of a byte array with given offset to int value.
     *
     * @param paRawBytes  the byte array
     * @param piOffset    offset in the original array to start reading bytes from
     * @param pbBigEndian true if the bytes are in Big-endian order; false otherwise
     * @return int representation of the bytes
     */
    public static int byteArrayToInt(byte[] paRawBytes, int piOffset, boolean pbBigEndian) {
        int iRetVal = -1;

        if (paRawBytes.length < piOffset + 4)
            return iRetVal;

        int iLowest;
        int iLow;
        int iMid;
        int iHigh;

        if (pbBigEndian) {
            iLowest = paRawBytes[piOffset + 3];
            iLow = paRawBytes[piOffset + 2];
            iMid = paRawBytes[piOffset + 1];
            iHigh = paRawBytes[piOffset + 0];
        } else {
            iLowest = paRawBytes[piOffset + 0];
            iLow = paRawBytes[piOffset + 1];
            iMid = paRawBytes[piOffset + 2];
            iHigh = paRawBytes[piOffset + 3];
        }

        // Merge four bytes to form a 32-bit int value.
        iRetVal = (iHigh << 24) | ((iMid & 0xFF) << 16) | ((iLow & 0xFF) << 8) | (0xFF & iLowest);

        return iRetVal;
    }

    /**
     * Converts an int value to a byte array.
     *
     * @param piValueToConvert the original integer
     * @param pbBigEndian      true if the bytes are in Big-endian order; false otherwise
     * @return byte[] representation of the int
     */
    public static byte[] intToByteArray(int piValueToConvert, boolean pbBigEndian) {
        byte[] aRetVal = new byte[4];

        byte iLowest;
        byte iLow;
        byte iMid;
        byte iHigh;

        iLowest = (byte) (piValueToConvert & 0xFF);
        iLow = (byte) ((piValueToConvert >> 8) & 0xFF);
        iMid = (byte) ((piValueToConvert >> 16) & 0xFF);
        iHigh = (byte) ((piValueToConvert >> 24) & 0xFF);

        if (pbBigEndian) {
            aRetVal[3] = iLowest;
            aRetVal[2] = iLow;
            aRetVal[1] = iMid;
            aRetVal[0] = iHigh;
        } else {
            aRetVal[0] = iLowest;
            aRetVal[1] = iLow;
            aRetVal[2] = iMid;
            aRetVal[3] = iHigh;
        }

        return aRetVal;
    }

    /**
     * Converts an int value to a byte array.
     *
     * @param piValueToConvert the original integer
     * @param pbBigEndian      true if the bytes are in Big-endian order; false otherwise
     * @return byte[] representation of the int
     */
    public static byte[] shortToByteArray(short piValueToConvert, boolean pbBigEndian) {
        byte[] aRetVal = new byte[2];

        byte iLow;
        byte iHigh;

        iLow = (byte) (piValueToConvert & 0xFF);
        iHigh = (byte) ((piValueToConvert >> 8) & 0xFF);

        if (pbBigEndian) {
            aRetVal[1] = iLow;
            aRetVal[0] = iHigh;
        } else {
            aRetVal[0] = iLow;
            aRetVal[1] = iHigh;
        }

        return aRetVal;
    }

    /**
     * Converts a byte array to String value.
     * Cleans up non-word characters along the way.
     * <p/>
     * Equivalent to byteArrayToString(paRawBytes, 0, paRawBytes.length);
     *
     * @param paRawBytes the byte array, non-UNICODE
     * @return UNICODE String representation of the bytes
     */
    public static String byteArrayToString(byte[] paRawBytes) {
        return byteArrayToString(paRawBytes, 0, paRawBytes.length);
    }

    /**
     * Converts a portion of a byte array to String value.
     * Cleans up non-word characters along the way.
     *
     * @param paRawBytes the byte array, non-UNICODE
     * @param piOffset   offset in the original array to start reading bytes from
     * @param piLength   how many bytes of the array paramter to interpret as String
     * @return UNICODE String representation of the bytes with trailing garbage stripped;
     * "" if array length is less than piOffset + piLength;
     * "" if the generatied string begins with garbage
     */
    public static String byteArrayToString(byte[] paRawBytes, int piOffset, int piLength) {
        if (paRawBytes.length < piOffset + piLength)
            return "";

        String oBeautifulString = new String(paRawBytes, piOffset, piLength);
        int i = 0;

        if (oBeautifulString.matches("^\\W") == true) {
            oBeautifulString = "";
        } else {
            for (i = piOffset; i < piOffset + piLength; i++) {
                if (paRawBytes[i] < 32 || paRawBytes[i] > 128) {
                    break;
                }
            }

            oBeautifulString = oBeautifulString.substring(0, i - piOffset);
        }

        return oBeautifulString;
    }

    /**
     * Converts a String value to a byte array in US-ASCII charset.
     * <p/>
     * Equivalent to stringToByteArray(pstrStringToConvert, "US-ASCII");
     *
     * @param pstrStringToConvert the original string
     * @return null-terminated byte[] representation of the String
     */
    public static byte[] stringToByteArray(String pstrStringToConvert) {
        return stringToByteArray(pstrStringToConvert, "US-ASCII");
    }

    /**
     * Attempts to convert a String value to a byte array in specified charset.
     * If the charset is invalid, returns plain byte-representation of the host environment.
     *
     * @param pstrStringToConvert the original string
     * @param pstrCharSet         characted set to assume for the original string
     * @return null-terminated byte[] representation of the String
     */
    public static byte[] stringToByteArray(String pstrStringToConvert, String pstrCharSet) {
        byte[] aRecordData = null;

        try {
            aRecordData = (pstrStringToConvert + '\0').getBytes(pstrCharSet);
        } catch (UnsupportedEncodingException e) {
            System.err.println("WARNING: " + e);
            aRecordData = (pstrStringToConvert + '\0').getBytes();
        }

        return aRecordData;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.6 $";
    }


    public static float byteArrayToFloat(byte[] b) {
        return Float.intBitsToFloat(byteArrayToInt(b, false));
    }

    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "GBK");
    }


    public static short getShort(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static short getShort(byte[] bytes, int offset) {
        return (short) ((0xff & bytes[offset]) | (0xff00 & (bytes[offset + 1] << 8)));
    }

    public static char getChar(byte[] bytes) {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static char getChar(byte[] bytes, int offset) {
        return (char) ((0xff & bytes[offset]) | (0xff00 & (bytes[offset + 1] << 8)));
    }


    public static int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public static int getInt3(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16));
    }

    public static int getInt(byte[] bytes, int offset) {
        return (0xff & bytes[offset]) | (0xff00 & (bytes[offset + 1] << 8)) | (0xff0000 & (bytes[offset + 2] << 16)) | (0xff000000 & (bytes[offset + 3] << 24));
    }

    public static long getLong(byte[] bytes) {
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16)) | (0xff000000L & ((long) bytes[3] << 24))
                | (0xff00000000L & ((long) bytes[4] << 32)) | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48)) | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public static long getLong(byte[] bytes, int offset) {
        return (0xffL & (long) bytes[offset]) | (0xff00L & ((long) bytes[offset + 1] << 8)) | (0xff0000L & ((long) bytes[offset + 2] << 16)) | (0xff000000L & ((long) bytes[offset + 3] << 24))
                | (0xff00000000L & ((long) bytes[offset + 4] << 32)) | (0xff0000000000L & ((long) bytes[offset + 5] << 40)) | (0xff000000000000L & ((long) bytes[offset + 6] << 48)) | (0xff00000000000000L & ((long) bytes[offset + 7] << 56));
    }

    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static float getFloat(byte[] bytes, int offset) {
        return Float.intBitsToFloat(getInt(bytes, offset));
    }

    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    public static double getDouble(byte[] bytes, int offset) {
        return Double.longBitsToDouble(getLong(bytes, offset));
    }

    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes, int offset, int len, String charsetName) {
        return new String(bytes, offset, len, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, "UTF8");
    }

    public static String getString(byte[] bytes, int offset, int len) {
        return getString(bytes, offset, len, "UTF8");
    }

    public static String byteArrayToHexString(byte[] byteArray){
        return byteArrayToHexString(byteArray,0,byteArray.length);
    }

    public static String byteArrayToHexString(byte[] byteArray, int offset, int length) {
        if (byteArray == null || (byteArray.length > (offset + length))) {
            return "Length invalid";
        }
        StringBuffer outBuffer = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            outBuffer.append(String.format("%02x ", byteArray[offset + i]));
        }
//        outBuffer.append("\n");
        return outBuffer.toString();
    }

    public static String byteArrayToDecimalString(byte[] byteArray, int offset, int length) {
        if (byteArray == null || (byteArray.length > (offset + length))) {
            return "Length invalid";
        }
        StringBuffer outBuffer = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            outBuffer.append(String.format("%02d ", byteArray[offset + i]&0xff));
        }
        outBuffer.append("\n");
        return outBuffer.toString();
    }

    //将字节转换为二进制字符串
    public static String byte2bits(byte b) {
        int z = b;
        z |= 256;
        String str = Integer.toBinaryString(z);
        int len = str.length();
        return str.substring(len - 8, len);
    }

    //将二进制字符串转换回字节
    public static byte bit2byte(String bString){
        byte result=0;
        for(int i=bString.length()-1,j=0;i>=0;i--,j++){
            result+=(Byte.parseByte(bString.charAt(i)+"")* Math.pow(2, j));
        }
        return result;
    }

    public static String bytes2bits(byte[] bytes){
        StringBuffer sb=new StringBuffer();
        for (byte b : bytes) {
            sb.append(ByteUtils.byte2bits(b));
        }
        return sb.toString();
    }

    public static byte[] bits2bytes(String bytesString){
        byte[] bytes=new byte[bytesString.length()/8];
        for (int i = 0; i < bytesString.length(); i+=8) {
            bytes[i/8]= Integer.valueOf(bytesString.substring(i,i+8),2).byteValue();
        }
        return bytes;
    }


    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static int simpleFind(byte[] src, int srcOff, int srcLen, byte[] dst) {
        return simpleFind(src, srcOff, srcLen, dst, 0, dst.length);
    }

    public static int simpleFind(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff, int dstLen) {
        for (int i =    0; i < srcLen; i++) {
            if ((srcLen - i) < dstLen) {
                return -1;
            }
            int j = 0;
            for (; j < dstLen; j++) {
                if (src[srcOff + i + j] != dst[dstOff + j]) {
                    break;
                }
            }
            if (j == dstLen) {
                return i;
            }
        }
        return -1;
    }

    public static void shiftArray(byte[] src, int off, int len, int shift) {
        if (shift == 0 || len < shift) {
            return;
        }
        for (int i = off; i < off + len - shift; i++) {
            src[i] = src[i + shift];
        }
    }


}