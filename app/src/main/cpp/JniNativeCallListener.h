//
// Created by andre on 09-Apr-19.
//

#ifndef LINESORTER_JNINATIVECALLLISTENER_H
#define LINESORTER_JNINATIVECALLLISTENER_H


#include <jni.h>

class JniNativeCallListener {

public:
    JniNativeCallListener(JNIEnv* env, jobject instance);

    void callJavaOnProcessMethod(const char* line);

    void callJavaOnCompleteMethod();

    void callJavaOnErrorMethod(const char* mes);

private:
    JNIEnv* getEnv();

    jmethodID onprocessid_;
    jmethodID oncompleteid_;
    jmethodID onerrorid_;

    jobject objectref_;
    JavaVM* jvm_;

};


#endif //LINESORTER_JNINATIVECALLLISTENER_H
