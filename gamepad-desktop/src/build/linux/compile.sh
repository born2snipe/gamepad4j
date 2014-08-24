
BIN_DIR=target
SRC_SHARED=../../../src/main/c/shared
SRC_LINUX=../../../src/main/c/linux
INCLUDE=../../../src/main/h/shared
JDK_INCLUDE=/stuff/java/j2se/current/include/
JDK_INCLUDE_LINUX=/stuff/java/j2se/current/include/linux/
TARGET_TMP=../../../${BIN_DIR}/linux-tmp
TARGET=../../../${BIN_DIR}/linux

rm -rf ${TARGET}
rm -rf ${TARGET_TMP}
mkdir -p ${TARGET_TMP}
mkdir -p ${TARGET}

gcc -Wall -fPIC -pthread -c -I ${INCLUDE} -I ${JDK_INCLUDE} -I ${JDK_INCLUDE_LINUX} ${SRC_SHARED}/Gamepad_private.c -o ${TARGET_TMP}/Gamepad_private.o
gcc -Wall -fPIC -pthread -c -I ${INCLUDE} -I ${JDK_INCLUDE} -I ${JDK_INCLUDE_LINUX} ${SRC_SHARED}/GamepadJniWrapper.c -o ${TARGET_TMP}/GamepadJniWrapper.o
gcc -Wall -fPIC -pthread -c -I ${INCLUDE} -I ${JDK_INCLUDE} -I ${JDK_INCLUDE_LINUX} ${SRC_LINUX}/Gamepad_linux.c -o ${TARGET_TMP}/Gamepad_linux.o

gcc -shared -Wall -Wl,-soname,libgamepad-jni-wrapper.so.1 -o ${TARGET}/libgamepad4j.so ${TARGET_TMP}/*.o
rm -rf ${TARGET_TMP}

