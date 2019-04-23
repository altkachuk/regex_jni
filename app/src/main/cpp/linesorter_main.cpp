#include "jni_interface.h"
#include "CLogReader.h"
#include "JniNativeCallListener.h"

// Android log function wrappers
static const char* kTAG = "linesorter";
#define LOGI(...) \
  ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))
#define LOGW(...) \
  ((void)__android_log_print(ANDROID_LOG_WARN, kTAG, __VA_ARGS__))
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))

CLogReader *logreader_;
JniNativeCallListener* listener_;

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* vm, void* reserved) {
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_InitApplication(
        JNIEnv *env,
        jobject instance) {
    logreader_ = new CLogReader();

    CLogCallback onProcessCallback;
    onProcessCallback = [](char *s) {
        listener_->callJavaOnProcessMethod(s);
    };
    logreader_->SetOnProcessCallback(onProcessCallback);

    CLogCallback oncompletecallback;
    oncompletecallback = [](char *s) {
        listener_->callJavaOnCompleteMethod();
    };
    logreader_->SetOnCompleteCallback(oncompletecallback);
}

JNIEXPORT void JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_SetListener(
        JNIEnv *env,
        jobject instance,
        jobject nativeCallLitener) {
    listener_ = new JniNativeCallListener(env, nativeCallLitener);
}

JNIEXPORT jboolean JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_SetFilter(
        JNIEnv *env,
        jobject jobj,
        jstring jfilter) {
    const char *filter = env->GetStringUTFChars(jfilter, 0);

    if (!logreader_->SetFilter(filter)) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_AddSourceBlock(
        JNIEnv *env,
        jobject instance,
        jstring jblock) {
    const char *block = env->GetStringUTFChars(jblock, 0);
    size_t block_size = strlen(block);

    if (!logreader_->AddSourceBlock(block, block_size)) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

JNIEXPORT void JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_StartParsing(
        JNIEnv *env,
        jobject instance) {
    logreader_->Start();
}

JNIEXPORT jint JNICALL
Java_atproj_com_linesorter_presenters_JNIPresenter_TestParsing(
        JNIEnv *env,
        jobject instance) {
    return logreader_->TestStart();
}

