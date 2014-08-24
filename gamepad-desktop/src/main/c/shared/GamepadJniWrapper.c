/*
 * GamepadJniWrapper.c
 *
 *  Created on: Jul 28, 2013
 *      Author: msc
 */

#include <jni.h>
#include <stdio.h>
#include <Gamepad.h>
#include <com_jplay_gamepad_jni_GamepadJniWrapper.h>

static bool verbose = true;

void onButtonDown(struct Gamepad_device * device, unsigned int buttonID,
		double timestamp) {
	if (verbose) {
		printf("Button %u down on device %u at %f\n", buttonID,
				device->deviceID, timestamp);
	}
}

void onButtonUp(struct Gamepad_device * device, unsigned int buttonID,
		double timestamp) {
	if (verbose) {
		printf("Button %u up on device %u at %f\n", buttonID, device->deviceID,
				timestamp);
	}
}

void onAxisMoved(struct Gamepad_device * device, unsigned int axisID,
		float value, double timestamp) {
	if (verbose) {
		printf("Axis %u moved to %f on device %u at %f\n", axisID, value,
				device->deviceID, timestamp);
	}
}

void onDeviceAttached(struct Gamepad_device * device) {
	if (verbose) {
		printf("Device ID %u attached (vendor = 0x%X; product = 0x%X)\n",
				device->deviceID, device->vendorID, device->productID);
	}
}

void onDeviceRemoved(struct Gamepad_device * device) {
	if (verbose) {
		printf("Device ID %u removed\n", device->deviceID);
	}
}

/*
 * Class:     com_jplay_gamepad_jni_GamepadJniWrapper
 * Method:    natInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_jplay_gamepad_jni_GamepadJniWrapper_natInit
(JNIEnv *env, jobject obj) {
	printf("Init!\n");
	Gamepad_deviceAttachFunc(onDeviceAttached);
	Gamepad_deviceRemoveFunc(onDeviceRemoved);
	Gamepad_buttonDownFunc(onButtonDown);
	Gamepad_buttonUpFunc(onButtonUp);
	Gamepad_axisMoveFunc(onAxisMoved);
	Gamepad_init();
	return;
}

/*
 * Class:     com_jplay_gamepad_jni_GamepadJniWrapper
 * Method:    natRelease
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_jplay_gamepad_jni_GamepadJniWrapper_natRelease
(JNIEnv *env, jobject obj) {
	printf("Release!\n");
	Gamepad_shutdown();
	return;
}

/*
 * Class:     com_jplay_gamepad_jni_GamepadJniWrapper
 * Method:    natGetNumberOfPads
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_jplay_gamepad_jni_GamepadJniWrapper_natGetNumberOfPads(
		JNIEnv *env, jobject obj) {
	return Gamepad_numDevices();
}

/*
 * Class:     com_jplay_gamepad_jni_GamepadJniWrapper
 * Method:    natDetectPads
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_jplay_gamepad_jni_GamepadJniWrapper_natDetectPads
(JNIEnv *env, jobject obj) {
	Gamepad_detectDevices();
	return;
}

