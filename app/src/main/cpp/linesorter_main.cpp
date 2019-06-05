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

static void InitApplication(JNIEnv* env, jobject instance) {
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

static void SetListener(JNIEnv* env, jobject instance, jobject nativeCallLitener) {
    listener_ = new JniNativeCallListener(env, nativeCallLitener);
}

static jboolean SetFilter(JNIEnv *env, jobject instance, jstring jfilter) {
    const char *filter = env->GetStringUTFChars(jfilter, 0);

    if (!logreader_->SetFilter(filter)) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

static jboolean AddSourceBlock(JNIEnv *env, jobject instance, jstring jblock) {
    const char *block = env->GetStringUTFChars(jblock, 0);
    size_t block_size = strlen(block);

    if (!logreader_->AddSourceBlock(block, block_size)) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

static void StartParsing(JNIEnv *env, jobject instance) {
    logreader_->Start();
}

static jint TestParsing(JNIEnv *env, jobject instance) {
    return logreader_->TestStart();
}

int jniRegisterNativeMethods(JNIEnv* env, const char* className, const JNINativeMethod* jMethods,
                             int numMethods) {
    jclass clazz;
    int tmp;

    LOGI("Registering %s natives\n", className);
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        LOGI("Native registration unable to find class '%s'\n", className);
        return -1;
    }
    if ((tmp=env->RegisterNatives(clazz, jMethods, numMethods)) <0) {
        LOGI("RegisterNatives failed for '%s', %d\n", className, tmp);
        return -1;
    }
    return 0;
}

int registerNativeMethods(JNIEnv *env) {
    return jniRegisterNativeMethods(env, "atproj/com/linesorter/presenters/JNIPresenter", methods,
                                    sizeof(methods) / sizeof(methods[0]));
}

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    jint result = JNI_ERR;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }

    if (registerNativeMethods(env) != JNI_OK) {
        return -1;
    }


    result = JNI_VERSION_1_6;
    LOGI("jni load start: %d", result);

    return result;
}

/*JNIEXPORT jint JNICALL
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
}*/

