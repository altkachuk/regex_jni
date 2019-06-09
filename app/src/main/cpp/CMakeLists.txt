cmake_minimum_required(VERSION 3.4.1)

add_library(linesorter
	SHARED
		linesorter_main.cpp
		CLogReader.cpp
		JniNativeCallListener.cpp
		String.cpp
		Pattern.cpp
		StringUtil.cpp
		Queue.h
		)
		
#include libraries needed for echo lib
target_link_libraries(linesorter
    android
    log)