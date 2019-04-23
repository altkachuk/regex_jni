//
// Created by andre on 05-Apr-19.
//

#ifndef LINESORTER_CLOGREADER_H
#define LINESORTER_CLOGREADER_H


#include <jni.h>
#include "CLogCallback.h"
#include "String.h"

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
    bool AddSourceBlock(const char *block, const size_t block_size);


    /**
     *
     * This method runs searching
     *
     */
    void Start();

    /**
     *
     * This method only for testing
     *
     * @return size of matches
     */
    int TestStart();

private:
    CLogCallback onprocesscallback_;
    CLogCallback oncompletecallback_;

private:
    Pattern *pattern_;
    String *blockstr_;

};



#endif //LINESORTER_CLOGREADER_H
