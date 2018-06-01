#include <jni.h>
#include <string>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <math.h>
#include <sys/time.h>
#include <fcntl.h>      /*文件控制定义*/
#include <termios.h>
#include <android/log.h>
#include <string.h>

extern "C"{
    JNIEXPORT jstring JNICALL Java_com_fei_demo_uart_Uart_jniTest(JNIEnv *env, jclass obj);
    JNIEXPORT jboolean JNICALL Java_com_fei_demo_uart_Uart_openDevice(JNIEnv *env, jclass);
    JNIEXPORT jboolean JNICALL Java_com_fei_demo_uart_Uart_closeDevice(JNIEnv *env, jclass);
    JNIEXPORT jboolean JNICALL Java_com_fei_demo_uart_Uart_isOpenDevice(JNIEnv *env, jclass);
    JNIEXPORT jbyteArray JNICALL Java_com_fei_demo_uart_Uart_recv(JNIEnv *env, jclass);
    JNIEXPORT jint JNICALL Java_com_fei_demo_uart_Uart_send(JNIEnv *env, jclass, jbyteArray);
}


JNIEXPORT jstring JNICALL Java_com_fei_demo_uart_Uart_jniTest(JNIEnv *env, jclass obj){
    std::string hello = "Hello from C++ yuneec uart";
    return env->NewStringUTF(hello.c_str());
}

#define LOG_TAG    "UartJni"
#define LOGV(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

typedef unsigned char	boolean;
typedef unsigned char 	u8;
typedef signed   char 	s8;
typedef unsigned short 	u16;
typedef signed	 short	s16;
typedef unsigned int 	u32;
typedef signed   int 	s32;

#define  UART_PATH  "/dev/ttyMFD0"


jboolean mIsOpen = false;
int uart_fd;

/*******************************************************************
* 名称：                UART0_Set
* 功能：               设置串口数据位，停止位和效验位
* 入口参数：fd       串口文件描述符
* speed     串口速度
* flow_ctrl 数据流控制 0不使用 1硬件流控制 2软件流控制
* databits   数据位   取值为 7 或者8
* stopbits   停止位   取值为 1 或者2
* parity     效验类型 取值为N,E,O,,S
*出口参数：      正确返回为1，错误返回为0
*******************************************************************/
bool UART_Set(int fd,int speed,int flow_ctrl,int databits,int stopbits,int parity)
{
    int i;
    int status;
    int speed_arr[] = { B230400, B115200, B38400, B19200, B9600, B4800, B2400,
                        B1200, B300 };
    int name_arr[] =
            { 230400, 115200, 38400, 19200, 9600, 4800, 2400, 1200, 300 };
    struct termios options;
    /*tcgetattr(fd,&options)得到与fd指向对象的相关参数，并将其保存于options,该函数还可以测试配置是否正确，
     该串口是否可用等。若调用成功，函数返回值为0，若调用失败，函数返回值为1*/
    if (tcgetattr(fd, &options) != 0) {
        LOGE("SetupSerial 1");
        return (false);
    }
    //设置串口输入波特率和输出波特率
    for (i = 0; i < sizeof(speed_arr) / sizeof(int); i++) {
        if (speed == name_arr[i]) {
            cfsetispeed(&options, speed_arr[i]);
            cfsetospeed(&options, speed_arr[i]);
        }
    }

    //修改控制模式，保证程序不会占用串口
    options.c_cflag |= CLOCAL;
    //修改控制模式，使得能够从串口中读取输入数据
    options.c_cflag |= CREAD;

    //设置数据流控制
    switch (flow_ctrl) {
        case 0: //不使用流控制
            options.c_cflag &= ~CRTSCTS;
            break;

        case 1: //使用硬件流控制
            options.c_cflag |= CRTSCTS;
            break;
        case 2: //使用软件流控制
            options.c_cflag |= IXON | IXOFF | IXANY;
            break;
    }
    //设置数据位
    //屏蔽其他标志位
    options.c_cflag &= ~CSIZE;
    switch (databits) {
        case 5:
            options.c_cflag |= CS5;
            break;
        case 6:
            options.c_cflag |= CS6;
            break;
        case 7:
            options.c_cflag |= CS7;
            break;
        case 8:
            options.c_cflag |= CS8;
            break;
        default:
            LOGV("Unsupported data size\n");
            return (false);
    }
    //设置校验位
    switch (parity) {
        case 'n':
        case 'N': //无奇偶校验位。
            options.c_cflag &= ~PARENB;
            options.c_iflag &= ~INPCK;
            break;
        case 'o':
        case 'O': //设置为奇校验
            options.c_cflag |= (PARODD | PARENB);
            options.c_iflag |= INPCK;
            break;
        case 'e':
        case 'E': //设置为偶校验
            options.c_cflag |= PARENB;
            options.c_cflag &= ~PARODD;
            options.c_iflag |= INPCK;
            break;
        case 's':
        case 'S': //设置为空格
            options.c_cflag &= ~PARENB;
            options.c_cflag &= ~CSTOPB;
            break;
        default:
            LOGV("Unsupported parity\n");
            return (false);
    }
    // 设置停止位
    switch (stopbits) {
        case 1:
            options.c_cflag &= ~CSTOPB;
            break;
        case 2:
            options.c_cflag |= CSTOPB;
            break;
        default:
            LOGV("Unsupported stop bits\n");
            return (false);
    }

    //修改输出模式，原始数据输出
    options.c_oflag &= ~OPOST;

    options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG); //我加的
    //options.c_lflag &= ~(ISIG | ICANON);

    options.c_iflag &= ~(BRKINT | ICRNL | INPCK | ISTRIP | IXON);

    //设置等待时间和最小接收字符
    options.c_cc[VTIME] = 1; /* 读取一个字符等待1*(1/10)s */
    options.c_cc[VMIN] = 0; /* 读取字符的最少个数为1 */

    //如果发生数据溢出，接收数据，但是不再读取 刷新收到的数据但是不读
    tcflush(fd, TCIFLUSH);

    //激活配置 (将修改后的termios数据设置到串口中）
    if (tcsetattr(fd, TCSANOW, &options) != 0) {
        LOGE("com set error!\n");
        return (false);
    }
    return (true);
}

JNIEXPORT jboolean JNICALL Java_com_fei_demo_uart_Uart_openDevice(
        JNIEnv *env, jclass obj) {
    uart_fd = open(UART_PATH, O_RDWR | O_NOCTTY); /*榛樿涓洪樆濉炶鏂瑰紡*/
    if(uart_fd == -1){
        LOGE("open serial ttyMFD1 failed\n");
        mIsOpen = false;
        return false;
//		return env->NewStringUTF("FAIL");
    }
    if (UART_Set(uart_fd, 230400, 0, 8, 1, 'N') == false) {
        LOGE("config uart failed\n");
        mIsOpen = false;
//		return env->NewStringUTF("FAIL");
    } else {
        LOGI("open uart successed -- >\n");
        mIsOpen = true;
//		return env->NewStringUTF("OK");
    }
    return mIsOpen;
}

JNIEXPORT jboolean JNICALL Java_com_fei_demo_uart_Uart_closeDevice(JNIEnv *env, jclass obj){
    close(uart_fd);
    uart_fd = -1;
    mIsOpen = false;
    return 0;
}

JNIEXPORT jboolean JNICALL Java_com_fei_demo_uart_Uart_isOpenDevice(JNIEnv *env, jclass obj){
    return mIsOpen;
}

//#define TMP_MAX_SIZE 24
//u8 recvBuf[TMP_MAX_SIZE] = {0};

jboolean doSend(int fd,const void *buffer,int len){
    int n = -1;
    if (mIsOpen){
        //LOGI("doSend\n");
        //dumpBuffer((u8*)buffer,len);
        n = write(fd, buffer, len);
    }else{
        LOGE("Device is not open");
        return false;
    }
    if(n != len){
        LOGE("send failed expected %d,actual %d",len,n);
        return false;
    }
    return true;
}

//u32 doRecv(u32 fd,u8 *buffer,u32 len){
//	jint n = -1;
//	n = read(fd, buffer, len);
//	return n;
//}

const int defaultRecvLen=64;
unsigned char recvBuf[defaultRecvLen] = {0};
JNIEXPORT jbyteArray JNICALL Java_com_fei_demo_uart_Uart_recv
        (JNIEnv *env, jclass){
    if (mIsOpen) {
        int recvLen=read(uart_fd, recvBuf, defaultRecvLen);
        jbyteArray arr = env->NewByteArray(recvLen);
        env->SetByteArrayRegion(arr, 0, recvLen, (signed char *)recvBuf);
//        env->SetByteArrayRegion(arr, 0, len,recvBuf);
        return arr;
    } else {
        LOGE("Recv Msg: Device is not open");
        return NULL;
    }
}


const int defaultSendLen=256;
unsigned char sendBuf[defaultSendLen] = {0};
JNIEXPORT jint JNICALL Java_com_fei_demo_uart_Uart_send
        (JNIEnv *env, jclass, jbyteArray arr){
    if (mIsOpen){
        int len=env->GetArrayLength(arr);
        if(len>256){
            LOGE("arr is to large,limited lenght is 256");
            return -3;
        }
        jbyte* byteArray = env->GetByteArrayElements(arr, 0);
        memcpy(sendBuf, byteArray, len);
        env->ReleaseByteArrayElements(arr, byteArray, 0);
        return write(uart_fd, sendBuf, len);
    }else{
        LOGE("Device is not open");
        return -2;
    }
}

