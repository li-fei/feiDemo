#include <jni.h>
#include <string>

extern "C"{
    JNIEXPORT jstring JNICALL Java_com_fei_demo_uart_Jni_stringFromJNI(JNIEnv *env, jobject /* this */);
    JNIEXPORT jstring JNICALL Java_com_fei_demo_uart_Jni_jniTest(JNIEnv *env, jclass obj);
    JNIEXPORT jboolean JNICALL Java_com_fei_demo_uart_Jni_jniTestBoolean(JNIEnv *env, jclass obj);
}

JNIEXPORT jstring JNICALL Java_com_fei_demo_uart_Jni_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_com_fei_demo_uart_Jni_jniTest(JNIEnv *env, jclass obj){
    std::string hello = "Hello from C++ yuneec";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jboolean JNICALL Java_com_fei_demo_uart_Jni_jniTestBoolean(JNIEnv *env, jclass obj){
    return true;
}
