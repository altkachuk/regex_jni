//
// Created by andre on 09-Apr-19.
//

#ifndef LINESORTER_CLOGCALLBACK_H
#define LINESORTER_CLOGCALLBACK_H

#include <cstring>


class CLogCallback {
private:
    void (*function)(char*, void*);
    void *parameters[4];

public:
    CLogCallback() {
        function = [](char*, void*) {
        };
    }


    template<class T>
    void operator=(T func) {
        sizeof(int[sizeof(parameters) - sizeof(T)]);

        function=[](char* arg, void* param) {
            (*(T*)param)(arg);
        };

        std::memcpy(parameters, &func, sizeof(T));
    }

    void operator()(char* d) {
        function(d, parameters);
    }
};


#endif //LINESORTER_CLOGCALLBACK_H
