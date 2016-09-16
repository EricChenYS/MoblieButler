#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>
#define LOG_TAG "chhd"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

JNIEXPORT jstring JNICALL Java_com_chhd_mobliebutler_activity_FeedbackActivity_getMyNumber
        (JNIEnv *env, jobject object) {
    return (*env)->NewStringUTF(env, "15875006020");
}

JNIEXPORT void JNICALL Java_com_chhd_mobliebutler_global_MyApplication_cfork
        (JNIEnv *env, jobject object) {
    int pid = fork();
    if (pid > 0) {
    } else if (pid == 0) {
        FILE *file;
        int ppid;
        while (1) {
            ppid = getppid();
            if (ppid == 1) {
                file = fopen("/data/data/com.chhd.mobliebutler","r");
                if(file == NULL){
                    execlp("am", "am", "start", "--user","0","-a", "android.intent.action.VIEW", "-d", "http://www.baidu.com", (char *) NULL);
                }
            }
            sleep(2);
        }
    } else {

    }
}