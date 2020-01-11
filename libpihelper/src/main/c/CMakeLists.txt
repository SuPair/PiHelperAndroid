# Set the minimum required version for cmake
cmake_minimum_required(VERSION 3.4.1)


set(PIHELPER_STATIC off CACHE BOOL "")
set(PIHELPER_SHARED on CACHE BOOL "")

add_subdirectory(libpihelper)

add_library(
        pihelper
        SHARED
        pihelper-android.c
)

# Include the Android logging library for native code
find_library(
        log-lib
        log
)

# Link our library with the Android logging library
target_link_libraries(
        pihelper
        ${log-lib}
        libpihelpershared
)