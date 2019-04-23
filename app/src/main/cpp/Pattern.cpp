//
// Created by andre on 14-Apr-19.
//

#include "Pattern.h"
#include "StringUtil.h"

Pattern::Pattern() {
    regex_ = nullptr;
    length_ = 0;
}

Pattern::Pattern(const char *regex, size_t len) {
    regex_ = new char[len+1];
    size_t i = 0;
    for (; i < len; i++) {
        regex_[i] = regex[i];
    }
    regex_[i] = '\0';
    length_ = len;
}

Pattern::Pattern(const Pattern &other) {
    regex_ = new char[other.length_+1];
    int i = 0;
    for (; i < other.length_; i++) {
        regex_[i] = other.regex_[i];
    }
    regex_[i] = '\0';
    length_ = other.length_;
}

Pattern::~Pattern() {
    if (regex_)
        delete[] regex_;
    length_ = 0;
}

Pattern& Pattern::operator=(const Pattern &other) {
    if (regex_)
        delete[] regex_;
    regex_ = new char[other.length_+1];
    int i = 0;
    for (; i < other.length_; i++) {
        regex_[i] = other.regex_[i];
    }
    regex_[i] = '\0';
    length_ = other.length_;
    return *this;
}

Pattern Pattern::Compile(const char *regex) {
    int length = 0;
    while (regex[length] != '\0') {
        length++;
    }

    if (length == 0) {
        throw 0;
    }

    for (size_t i = 0; i < length; i++) {
        if (StringUtil::Find(regex, length, i, kBackSlash) == i) {
            if (i >= length-1)
                throw 0;

            if (regex[i+1] != kAnySeqeunce && regex[i+1] != kOneSymbol && regex[i+1] != kBackSlash)
                throw 0;
        }
    }

    return Pattern(regex, length);
}

bool Pattern::Match(const char *src, int srclen) {
    src_pos_ = 0;
    regex_pos_ = 0;
    any_pos_ = -1;
    last_state_ = 0;

    bool is_any = false;
    while (regex_pos_ < length_) {
        if (!find(src, srclen, is_any))
            return false;
        // check end of the line
        if (regex_pos_ == length_) {
            if (!find(src, srclen, is_any))
                return false;
        }
    }


    return true;
}

bool Pattern::find(const char *src, int srclen, bool &is_any) {
    size_t res = -1;
    char t = regex_[regex_pos_];
    switch (regex_[regex_pos_]) {
        case kBackSlash:
            regex_pos_++;
            return findCharacter(src, srclen, is_any);
        case kAnySeqeunce:
            any_pos_ = regex_pos_;
            src_pos_++;
            if (src_pos_ > srclen)
                return false;
            regex_pos_++;
            is_any = true;
            src_any_pos_ = src_pos_;
            last_state_ = kAnySeqeunce;
            return true;
        case kOneSymbol:
            src_pos_++;
            if (src_pos_ > srclen)
                return false;
            regex_pos_++;
            is_any = false;
            last_state_ = kOneSymbol;
            return true;
        case kEndLine:
            if (last_state_ != 'c' && last_state_ != kOneSymbol)
                return true;
            if (src_pos_ == srclen)
                return true;
            if (any_pos_ == -1)
                return false;
            regex_pos_ = any_pos_;
            src_pos_ = src_any_pos_;
            return find(src, srclen, is_any);
        default:
            return findCharacter(src, srclen, is_any);
    }
}

bool Pattern::findCharacter(const char *src, int srclen, bool &is_any) {
    size_t res = -1;

    res = StringUtil::Find(src, srclen, src_pos_, regex_[regex_pos_]);
    if (res == -1)
        return false;

    if (!is_any && res != src_pos_) {
        if (any_pos_ == -1)
            return false;

        regex_pos_ = any_pos_;
        src_pos_ = src_any_pos_;
        return find(src, srclen, is_any);
    }

    regex_pos_++;
    src_pos_ = res+1;
    is_any = false;
    last_state_ = 'c';
    return true;
}
