//
// Created by andre on 05-Apr-19.
//

#include "CLogReader.h"
#include "Pattern.h"

CLogReader::CLogReader() {
    pattern_ = nullptr;
    string_queue_ = new Queue<String>();
    pattern_queue_ = new Queue<Pattern>();

    executed_ = true;
    execute_thread_ = std::thread(&CLogReader::execute, this);
    execute_thread_.detach();
}

CLogReader::~CLogReader() {
    executed_ = false;

    if (pattern_)
        delete pattern_;
    if (string_queue_)
        delete string_queue_;
    if (pattern_queue_)
        delete pattern_queue_;
}

void CLogReader::SetOnProcessCallback(CLogCallback callback) {
    onprocesscallback_ = callback;
}

void CLogReader::SetOnCompleteCallback(CLogCallback callback) {
    oncompletecallback_ = callback;
}

bool CLogReader::SetFilter(const char *filter) {
    try {
        Pattern pattern = Pattern::Compile(filter);
        pattern_ = new Pattern(pattern);
    } catch (int e) {
        return false;
    }
    return true;
}

void CLogReader::AddSourceBlock(const char *block, const size_t block_size) {
    if (block_size == 0) {
        if (!executed_) {
            oncompletecallback_(nullptr);
        }
        return;
    }

    std::lock_guard<std::mutex> lck(mutex_);
    string_queue_->Push(new String(block, block_size));
    pattern_queue_->Push(new Pattern(*pattern_));
}

void CLogReader::execute() {
    while (executed_) {
        while (string_queue_->Size() > 0) {
            std::lock_guard<std::mutex> lck(mutex_);
            String *string = string_queue_->Pop();
            Pattern *pattern = pattern_queue_->Pop();
            size_t arrsize = 0;
            // split by new string character
            String *strarr = string->Split(arrsize, '\n');

            for (size_t i = 0; i < arrsize; i++) {
                String str = strarr[i];

                if (str.Match(pattern_)) {
                    char *ch = str.GetString();
                    onprocesscallback_(ch);
                }
            }
            delete string;
            delete pattern;
        }
    }
    //executed_ = false;
    //oncompletecallback_(nullptr);
}

int CLogReader::NumOfMatches(const char *block, const size_t block_size) {
    String* string = new String(block, block_size);

    size_t arrsize = 0;
    // split by new string character
    String *strarr = string->Split(arrsize, '\n');

    int res = 0;
    for (size_t i = 0; i < arrsize; i++) {
        String str = strarr[i];

        if (str.Match(pattern_)) {
            res++;
        }
    }

    return res;
}