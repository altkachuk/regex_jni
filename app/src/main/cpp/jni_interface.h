//
// Created by andre on 08-Apr-19.
//

#ifndef LINESORTER_JNI_INTERFACE_H
#define LINESORTER_JNI_INTERFACE_H

#include <jni.h>
#include <android/log.h>
#include <inttypes.h>

#ifdef __cplusplus
extern "C" {
#endif

static void InitApplication(JNIEnv* env, jobject instance);

static void SetListener(JNIEnv* env, jobject instance, jobject nativeCallLitener);

static jboolean SetFilter(JNIEnv *env, jobject instance, jstring jfilter);

static void AddSourceBlock(JNIEnv *env, jobject instance, jstring jblock);

static jint NumOfMatches(JNIEnv *env, jobject instance, jstring jblock);

static JNINativeMethod methods[] = {
        {"InitApplication", "()V", (void*) &InitApplication},
        {"SetListener", "(Latproj/com/linesorter/jni/NativeCallListener;)V", (void*) &SetListener},
        {"SetFilter", "(Ljava/lang/String;)Z", (void*) &SetFilter},
        {"AddSourceBlock", "(Ljava/lang/String;)V", (void*) &AddSourceBlock},
        {"NumOfMatches", "(Ljava/lang/String;)I", (void*) &NumOfMatches},
};

int jniRegisterNativeMethods(JNIEnv* env, const char* className, const JNINativeMethod* jMethods,
        int numMethods);

int registerNativeMethods(JNIEnv *env);

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* vm, void* reserved);

#ifdef __cplusplus
};
#endif

#endif //LINESORTER_JNI_INTERFACE_H
