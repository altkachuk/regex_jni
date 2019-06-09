//
// Created by andre on 13-Apr-19.
//

#include "String.h"
#include "StringUtil.h"

String::String() {
    string_ = nullptr;
    length_ = 0;
}

String::String(const char *str, size_t len) {
    string_ = new char[len + 1];
    int i = 0;
    for (; i < len; i++) {
        string_[i] = str[i];
    }
    string_[i] = '\0';
    length_ = len;
}

String::~String() {
    if (string_)
        delete[] string_;
    length_ = 0;
}

String& String::operator=(const String &other) {
    if (string_ != nullptr)
        delete[] string_;

    string_ = new char[other.length_ + 1];
    int i = 0;
    for (; i < other.length_; i++) {
        string_[i] = other.string_[i];
    }
    string_[i] = '\0';
    length_ = other.length_;

    return *this;
}

char* String::GetString() {
    return string_;
}

size_t String::GetLength() {
    return length_;
}

String* String::Split(size_t &dstarrsize, char pred) {
    String temparr[length_];
    int arrindex = 0;
    size_t j = 0;
    for (size_t i = 0; i < length_; i++) {
        size_t index = StringUtil::Find(string_, length_, i, pred);
        if (index == -1) {
            size_t strlen = length_- i;
            char* str = StringUtil::Substr(string_, length_, i, strlen);
            String *s = new String(str, strlen);
            temparr[arrindex] = *s;
            arrindex++;
            i = length_;
        } else {
            size_t strlen = index - i;
            char *str = StringUtil::Substr(string_, length_, i, strlen);
            String *s = new String(str, strlen);
            temparr[arrindex] = *s;
            arrindex++;
            i = index;
        }
    }

    dstarrsize = arrindex;
    String *resarr = new String[dstarrsize];
    for (size_t i = 0; i < dstarrsize; i++) {
        resarr[i] = temparr[i];
    }

    return resarr;
}

String* String::Split(size_t &dstarrsize, char* pred, size_t predlen) {
    String temparr[length_];
    int arrindex = 0;
    size_t j = 0;
    for (size_t i = 0; i < length_; i++) {
        size_t index = StringUtil::Find(string_, length_, i, pred, predlen);
        if (index == -1) {
            size_t strlen = length_- i;
            char* str = StringUtil::Substr(string_, length_, i, strlen);
            String *s = new String(str, strlen);
            temparr[arrindex] = *s;
            arrindex++;
            i = length_;
        } else {
            size_t strlen = index - i;
            char *str = StringUtil::Substr(string_, length_, i, strlen);
            String *s = new String(str, strlen);
            temparr[arrindex] = *s;
            arrindex++;
            i = index + predlen;
        }
    }

    dstarrsize = arrindex;
    String *resarr = new String[dstarrsize];
    for (size_t i = 0; i < dstarrsize; i++) {
        resarr[i] = temparr[i];
    }

    return resarr;
}

bool String::Match(Pattern* pattern) {
    return pattern->Match(string_, length_);
}