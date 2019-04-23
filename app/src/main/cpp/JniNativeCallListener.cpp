//
// Created by andre on 09-Apr-19.
//

#include "JniNativeCallListener.h"

JniNativeCallListener::JniNativeCallListener(JNIEnv *env, jobject instance) {
    env->GetJavaVM(&jvm_);
    objectref_ = env->NewGlobalRef(instance);
    jclass clazz = env->GetObjectClass(instance);

    onprocessid_ = env->GetMethodID(clazz, "onProcess", "(Ljava/lang/String;)V");
    oncompleteid_ = env->GetMethodID(clazz, "onComplete", "()V");
    onerrorid_ = env->GetMethodID(clazz, "onError", "(Ljava/lang/String;)V");

}

JNIEnv* JniNativeCallListener::getEnv() {
    JavaVMAttachArgs attachargs;
    attachargs.version = JNI_VERSION_1_6;
    attachargs.name = ">>>NativeThread__Any";
    attachargs.group = NULL;

    JNIEnv* env;
    if (jvm_->AttachCurrentThread(&env, &attachargs) != JNI_OK) {
        env = NULL;
    }
    return env;
}

void JniNativeCallListener::callJavaOnProcessMethod(const char* line) {
    JNIEnv* env = getEnv();
    jstring str = env->NewStringUTF(line);
    env->CallVoidMethod(objectref_, onprocessid_, str);
}

void JniNativeCallListener::callJavaOnCompleteMethod() {
    JNIEnv* env = getEnv();
    env->CallVoidMethod(objectref_, oncompleteid_);
}

void JniNativeCallListener::callJavaOnErrorMethod(const char* mes) {
    JNIEnv* env = getEnv();
    jstring str = env->NewStringUTF(mes);
    env->CallVoidMethod(objectref_, onerrorid_, str);
}