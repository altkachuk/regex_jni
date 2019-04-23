//
// Created by andre on 05-Apr-19.
//

#include "CLogReader.h"
#include "Pattern.h"

CLogReader::CLogReader() {
    blockstr_ = nullptr;
    pattern_ = nullptr;
}

CLogReader::~CLogReader() {
    if (blockstr_)
        delete blockstr_;
    if (pattern_)
        delete pattern_;
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

bool CLogReader::AddSourceBlock(const char *block, const size_t block_size) {
    if (blockstr_)
        delete blockstr_;

    if (block_size == 0)
        return false;

    blockstr_ = new String(block, block_size);

    return true;
}

void CLogReader::Start() {
    size_t arrsize = 0;
    // split by new string character
    String *strarr = blockstr_->Split(arrsize, '\n');

    for (size_t i = 0; i < arrsize; i++) {
        String str = strarr[i];

        if (str.Match(pattern_)) {
            char *ch = str.GetString();
            onprocesscallback_(ch);
        }
    }

    oncompletecallback_(nullptr);
}

int CLogReader::TestStart() {
    size_t arrsize = 0;
    // split by new string character
    String *strarr = blockstr_->Split(arrsize, '\n');

    int res = 0;
    for (size_t i = 0; i < arrsize; i++) {
        String str = strarr[i];

        if (str.Match(pattern_)) {
            res++;
        }
    }

    return res;
}