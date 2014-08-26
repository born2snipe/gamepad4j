/*
 * GamepadJniWrapper.c
 *
 *  Created on: Jul 28, 2013
 *      Author: msc
 */

#include <jni.h>
#include <stdio.h>
#include <Gamepad.h>
#include <com_gamepad4j_controller_GamepadJniWrapper.h>

static bool verbose = true;


static jmethodID midStr;
static jobject callbackObj;
static JavaVM *gJavaVM;

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
/*
	JNIEnv *env;
printf("---------> 1\n");
	(*gJavaVM)->GetEnv(gJavaVM, (void**)&env, JNI_VERSION_1_6);
	printf("---------> 2\n");
	(*gJavaVM)->AttachCurrentThread(gJavaVM, (void **)&env, NULL);
	printf("---------> 3\n");
	(*env)->CallVoidMethod(env, callbackObj, midStr, device->deviceID);
	printf("---------> 4\n");
	(*gJavaVM)->DetachCurrentThread(gJavaVM);
	printf("---------> 5\n");
	*/
}

void onDeviceRemoved(struct Gamepad_device * device) {
	if (verbose) {
		printf("Device ID %u removed\n", device->deviceID);
	}
}

/*
 * Class:     com_gamepad4j_controller_GamepadJniWrapper
 * Method:    natInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_gamepad4j_controller_GamepadJniWrapper_natInit
(JNIEnv *env, jobject obj) {
	printf("Init!\n");

	printf("Get VM..\n");
	jint rs = (*env)->GetJavaVM(env, &gJavaVM);
	if(rs == JNI_OK) {
		printf("**** VM OK ****\n");
	}

	// Register callback methods
	printf("Prepare callbacks!\n");
	jclass thisClass = (*env)->GetObjectClass(env, obj);
	callbackObj = obj;
	printf("Get method reference..\n");
	midStr = (*env)->GetMethodID(env, thisClass, "callbackConnectDevice", "(I)V");
	Gamepad_deviceAttachFunc(onDeviceAttached);
	Gamepad_deviceRemoveFunc(onDeviceRemoved);
	Gamepad_buttonDownFunc(onButtonDown);
	Gamepad_buttonUpFunc(onButtonUp);
	Gamepad_axisMoveFunc(onAxisMoved);
	Gamepad_init();

	printf("Init completed.\n");
}

/*
 * Class:     com_gamepad4j_controller_GamepadJniWrapper
 * Method:    natRelease
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_gamepad4j_controller_GamepadJniWrapper_natRelease
(JNIEnv *env, jobject obj) {
	printf("Release!\n");
	Gamepad_shutdown();
	return;
}

/*
 * Class:     com_gamepad4j_controller_GamepadJniWrapper
 * Method:    natGetNumberOfPads
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_gamepad4j_controller_GamepadJniWrapper_natGetNumberOfPads(
		JNIEnv *env, jobject obj) {
	return Gamepad_numDevices();
}

/*
 * Class:     com_gamepad4j_controller_GamepadJniWrapper
 * Method:    natGetIdOfPad
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_gamepad4j_controller_GamepadJniWrapper_natGetIdOfPad(
		JNIEnv *env, jobject obj, jint index) {
	struct Gamepad_device * device = Gamepad_deviceAtIndex(index);
	if(device == NULL) {
		return -1;
	}
	return device->deviceID;
}

/*
 * Class:     com_gamepad4j_controller_GamepadJniWrapper
 * Method:    natDetectPads
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_gamepad4j_controller_GamepadJniWrapper_natDetectPads
(JNIEnv *env, jobject obj) {
	Gamepad_detectDevices();
	return;
}





/*
 * Class:     com_gamepad4j_controller_GamepadJniWrapper
 * Method:    natGetControllerId
 * Signature: ()I
 */
JNIEXPORT jstring JNICALL Java_com_gamepad4j_controller_GamepadJniWrapper_natGetControllerDescription(
		JNIEnv *env, jobject obj, jint gamepadIndex)
{
	unsigned int index = gamepadIndex;
	struct Gamepad_device * device = Gamepad_deviceAtIndex(index);
    jstring result = (*env)->NewStringUTF(env, device->description); // C style string to Java String
    return result;
}

/*
 * Class:     com_gamepad4j_controller_GamepadJniWrapper
 * Method:    natGetControllerId
 * Signature: ()I
 */
JNIEXPORT void JNICALL Java_com_gamepad4j_controller_GamepadJniWrapper_natGetControllerIDs(
		JNIEnv *env, jobject obj, jint gamepadIndex, jintArray idArray)
{
	jint *ids = (*env)->GetIntArrayElements(env, idArray, NULL);

	struct Gamepad_device * device = Gamepad_deviceAtIndex(gamepadIndex);
	ids[0] = device->deviceID;
	ids[1] = device->vendorID;
	ids[2] = device->productID;

	(*env)->ReleaseIntArrayElements(env, idArray, ids, 0);
}


