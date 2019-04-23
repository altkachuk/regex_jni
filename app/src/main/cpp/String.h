//
// Created by andre on 13-Apr-19.
//

#ifndef LINESORTER_STRING_H
#define LINESORTER_STRING_H

#include <stddef.h>
#include "Pattern.h"


class String {
public:
    String();
    String(const char *str, size_t len);
    ~String();

    String& operator =(const String &other);

    /**
     *
     * @return Current string
     */
    char* GetString();

    /**
     *
     * @return Length of string
     */
    size_t GetLength();

    /**
     *
     * @param dstarrsize Size of split strings
     * @param pred Character of split
     * @return Array of split strings
     */
    String* Split(size_t &dstarrsize, char pred);

    /**
     *
     * @param dstarrsize Size of split strings
     * @param pred String of split
     * @param predlen size of string of split
     * @return Array of split strings
     */
    String* Split(size_t &dstarrsize, char* pred, size_t predlen);

    /**
     *
     * @param pattern Pattern for matching
     * @return True if matched, otherwise false
     */
    bool Match(Pattern* pattern);

private:
    char* string_;
    size_t length_;
};


#endif //LINESORTER_STRING_H
