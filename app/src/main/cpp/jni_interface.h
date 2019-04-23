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

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* vm, void* reserved);

JNIEXPORT void JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_InitApplication(
        JNIEnv *env,
        jobject instance);

JNIEXPORT void JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_SetListener(
        JNIEnv *env,
        jobject instance,
        jobject nativeCallLitener);

JNIEXPORT jboolean JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_SetFilter(
        JNIEnv *env,
        jobject instance,
        jstring filter);

JNIEXPORT jboolean JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_AddSourceBlock(
        JNIEnv *env,
        jobject instance,
        jstring block);

JNIEXPORT void JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_StartParsing(
        JNIEnv *env,
        jobject instance);

JNIEXPORT jint JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_TestParsing(
        JNIEnv *env,
        jobject instance);

#ifdef __cplusplus
};
#endif

#endif //LINESORTER_JNI_INTERFACE_H
