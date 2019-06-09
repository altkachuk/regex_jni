//
// Created by andre on 05-Apr-19.
//

#ifndef LINESORTER_CLOGREADER_H
#define LINESORTER_CLOGREADER_H


#include <jni.h>
#include "CLogCallback.h"
#include "String.h"
#include "Queue.h"
#include <thread>

class CLogReader {

public:
    CLogReader();
    ~CLogReader();

public:

    void SetOnProcessCallback(CLogCallback callback);
    void SetOnCompleteCallback(CLogCallback callback);

    /**
     *
     * @param filter
     * @return
     */
    bool SetFilter(const char *filter);

    /**
     *
     * @param block
     * @param block_size
     * @return
     */
    void AddSourceBlock(const char *block, const size_t block_size);

    /**
     *
     * This method only for testing
     *
     * @return size of matches
     */
    int NumOfMatches(const char *block, const size_t block_size);

private:
    void execute();


private:
    CLogCallback onprocesscallback_;
    CLogCallback oncompletecallback_;
    Pattern *pattern_;
    Queue<String> *string_queue_;
    Queue<Pattern> *pattern_queue_;
    bool executed_;
    std::thread execute_thread_;
    std::mutex mutex_;
    std::condition_variable cond_var_;

};


#endif //LINESORTER_CLOGREADER_H
