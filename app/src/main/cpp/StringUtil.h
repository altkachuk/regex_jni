//
// Created by andre on 15-Apr-19.
//

#ifndef LINESORTER_STRINGUTIL_H
#define LINESORTER_STRINGUTIL_H

#include <stddef.h>

class StringUtil {

public:

    /**
     *
     * @param src Source of text
     * @param srclen Length of source of text
     * @param pos Start position on source
     * @param search searching character
     * @return position of first searched element or -1
     */
    static int Find(const char *src, size_t srclen, size_t pos, char search) throw(int);

    /**
     *
     * @param src Source of text
     * @param srclen Length of source of text
     * @param pos Start position on source
     * @param search Searching string
     * @param searchlen Length of searching string
     * @return position of first searched element or -1
     */
    static int Find(const char *src, size_t srclen, size_t pos, const char *search, size_t searchlen) throw(int);

    /**
     *
     * @param src Source of text
     * @param pos Start position in source
     * @param len Length of text
     * @return Substring of text,
     */
    static char* Substr(const char *src, size_t srclen, size_t pos, size_t len) throw(int);
};


#endif //LINESORTER_STRINGUTIL_H
