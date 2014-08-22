/*
 * GamepadJniWrapper.c
 *
 *  Created on: Jul 28, 2013
 *      Author: msc
 */


#include <jni.h>
#include <stdio.h>
#include <Gamepad.h>
#include <com_jplay_gdx_joypad_GamepadJniWrapper.h>


/*
 * Class:     com_jplay_gdx_joypad_GamepadJniWrapper
 * Method:    natInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_jplay_gdx_joypad_GamepadJniWrapper_natInit
  (JNIEnv *env, jobject obj) {
	   printf("Init!\n");
	   Gamepad_init();
	   return;
}

/*
 * Class:     com_jplay_gdx_joypad_GamepadJniWrapper
 * Method:    natRelease
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_jplay_gdx_joypad_GamepadJniWrapper_natRelease
  (JNIEnv *env, jobject obj) {
	   printf("Release!\n");
	   Gamepad_shutdown();
	   return;
}

/*
 * Class:     com_jplay_gdx_joypad_GamepadJniWrapper
 * Method:    natGetNumberOfPads
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_jplay_gdx_joypad_GamepadJniWrapper_natGetNumberOfPads
  (JNIEnv *env, jobject obj) {
	   printf("get number of pads...\n");

	   return 0;
}

/*
 * Class:     com_jplay_gdx_joypad_GamepadJniWrapper
 * Method:    natDetectPads
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_jplay_gdx_joypad_GamepadJniWrapper_natDetectPads
  (JNIEnv *env, jobject obj) {
	   printf("detect pads...\n");
	   return;
}

