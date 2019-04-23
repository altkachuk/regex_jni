//
// Created by andre on 15-Apr-19.
//

#include "StringUtil.h"

int StringUtil::Find(const char *src, size_t srclen, size_t pos, char search) throw(int) {
    // position of source at the end of string
    if (pos >= srclen)
        return -1;

    for (size_t i = pos; i < srclen; i++) {
        if (src[i] == search)
            return i;
    }

    return -1;
}

int StringUtil::Find(const char *src, size_t srclen, size_t pos, const char *search, size_t searchlen) throw(int) {
    // searching string is less than source text
    if (srclen < searchlen)
        throw 0;

    // position of source at the end of string
    if (pos >= srclen)
        throw 0;

    for (size_t i = pos; i <= srclen - searchlen; i++) {
        int j;
        for (j = 0; j < searchlen; j++) {
            if (src[i + j] != search[j])
                break;
        }
        if (j == searchlen)
            return i;
    }

    return -1;
}

char* StringUtil::Substr(const char *src, size_t srclen, size_t pos, size_t len) throw(int) {
    if (srclen - pos < len)
        throw 0;


    char *res = new char[len + 1];
    size_t i = 0;
    for (; i < len; i++) {
        res[i] = src[i+pos];
    }
    res[i] = '\0';
    return res;
}