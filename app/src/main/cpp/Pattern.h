//
// Created by andre on 15-Apr-19.
//

#ifndef LINESORTER_PATTERN_H
#define LINESORTER_PATTERN_H

#include <stddef.h>

class Pattern {
    static const char kAnySeqeunce = '*';
    static const char kOneSymbol = '?';
    static const char kBackSlash = '\\';
    static const char kEndLine = '\0';

private:
    Pattern();
    Pattern(const char *regex, size_t len);


public:
    Pattern(const Pattern &other);
    ~Pattern();

public:
    Pattern&operator=(const Pattern &other);

    /**
     * Check special character backslash \.
     * Every backslash should be followed by \ or * or ?
     *
     * @param regex String of regular expression i.e. *some*som?\*\\*
     * @return True if regular expression is correct
     */
    static Pattern Compile(const char *regex);

    /**
     *
     * @param src Source of text
     * @param srclen Length of source of text
     * @return true if matched, otherwise false
     */
    bool Match(const char* src, int srclen);

private:
    bool find(const char* src, int srclen, bool &is_any);
    bool findCharacter(const char *src, int srclen, bool &is_any);

private:
    char* regex_;
    size_t length_;

    size_t src_pos_;
    size_t regex_pos_;
    size_t any_pos_;
    char last_state_;
    size_t src_any_pos_;
};


#endif //LINESORTER_PATTERN_H
