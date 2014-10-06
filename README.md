Gamepad4J
=========

A simple, elegant and powerful API for using gamepads like the Xbox 360 or DualShock 3 pad in Java-based games. 

*Release 1.0 planned for Nov/Dec 2014*

 
Features
--------

  * The core API provides simple, object-oriented access to the buttons, analog sticks and triggers of a common gamepad.
  * Convenience methods for getting the direction (degrees) and distance to center of an analog stick
  * Constants for querying specific buttons, like "lower face button" or "upper left shoulder button"
  * Constants for handling common "accept" and "deny" buttons
  * Predefined text keys and labels for all the buttons, so that with a DualShock 3, a message can be displayed like "Press X to accept", while with an Xbox 360 pad, it would be "Press A to accept".
  * Low-level methods for accessing all analog axes and digital buttons of a controller
  * Low footprint, minimal object allocation at runtime
  * Single Uber-jar contains all wrappers for all supported platforms
  * The executable Uber-jar also contains a mapper tool that can be used to create new mappings for desktop controllers.
  * Provides clean Gradle and Maven build for the Java part
  * Apache 2.0 license


Usage Example
-------------

Initialize the API, query the connected controllers, and check the state of some buttons and an analog stick. Then shut down the API again when exiting the game:

        // Initialize the API
        Controllers.initialize();
        ...
        // Poll the state of the controllers
        Controllers.checkControllers();
        IController[] gamepads = Controllers.getControllers();
        ...
        // Use the lower gamepad face button ("X" on DualShock, or "A" on Xbox pad)
        IButton jumpButton = gamepads[0].getButton(ButtonID.FACE_DOWN);
        if(jumpButton.isPressed()) {
            ...jump...
        }
        // Use the d-pad
        if(gamepads[0].getDpadDirection() == DpadDirection.DOWN) {
            ...move down...
        }
        // Use an analog stick
        IStick leftStick = gamepad[0].getStick(StickID.LEFT);
        StickPosition pos = leftStick.getPosition();
        // Now check stick position
        if(!pos.isStickCentered() && pos.getDirection() == 45f) {
            ...move character up right, 45 degrees...
        }
        // Game ended / shut down the API
        Controllers.shutdown();
    

Platform Support
----------------

  * OUYA (uses OUYA ODK controller API)
  * Desktop (Windows, Linux, MacOS, uses JNI wrapper)
  
Support for more platforms is planned for the future, specifically generic Android 2/3/4, Sony Xperia Play and Gamestick.


Controller Support
------------------

The API provides mappings for some popular controllers out of the box:

 * Microsoft Xbox 360 wired (USB) and wireless controller
 * Sony DualShock 3
 * Sony DualShock 4
 * Logitech F310
 
Support for more controllers will probably be included in future releases. Sending in your own, custom created mappings (using the Mapper tool contained in the executable Uber-jar) will help a lot.


Future Controller Support
-------------------------

It is not (yet) designed to deal specifically with joysticks / flightsticks, steering wheels etc. However, since today probably 80% of game developers really just want to support a gamepad for their game, this is what this API is focusing on. Still, all the analog axes and digital buttons of a controller can be queried, so the API can be used to handle any kind of controller, it just provides no mappings or convenience methods to do so. In the future, specific support for joysticks will probably be added (and maybe for steering wheels, if possible).


Known Issues
------------

 * On desktop systems, the mappings may not always be correct, depending on what driver is being used.
 * The Xbox 360 pad analog triggers are not properly supported on Windows, due to the Microsoft driver putting them both on the same analog axis (one using the positive, the other using the negative value range; press both, and they negate each other). Ironically, there seems to be no such issue on Linux and MacOS.
 

Links / Web Resources
---------------------

 * Project Site: http://www.gamepad4j.org
 * Github Site: https://github.com/marcelschoen/gamepad4j
 * Javadoc: http://www.gamepad4j.org/javadoc
 * Downloads: https://sourceforge.net/projects/gamepad4j/files/
 * Native Gamepad API: http://forums.tigsource.com/index.php?topic=10675


Credits
-------
 * Java API by Marcel Schoen, Switzerland
 * Desktop native API by Alex Diener: http://forums.tigsource.com/index.php?topic=10675.msg328738#msg328738
 * Native API multiplatform build support and testing by Thomas Frauenknecht, Switzerland
 * Website Design by Jason Long: https://github.com/jasonlong/architect-theme
 * Jekyll Theme by Pietro Menna: https://github.com/pietromenna/jekyll-architect-theme


Build Information
=================

Prerequisites
-------------

 * GIT (sourcecode is on Github)
 * Java 1.6
 * Maven 3.2 or Gradle 1.2
 * OUYA SDK (ODK) installed, environment variable "ODK" set for Gradle, or Maven property "ouya.sdk.path" set accordingly 
 * GCC 4.7.x on Linux (only if native re-compile is required)
 * MinGW on Windows (dito)
 * XCode on MacOS (dito)

 
GIT Branches
------------

Currently there are only two branches, "master" and "gh-pages". Only "master" is relevant for the code. "gh-pages" is a special branch used by GitHub for publishing the project site. Separate, new branches will probably be used in the future for developing new experimental stuff.


Building The API
----------------

The project was developed using Gradle 1.2 and/or Maven 3.2.3. It can be built with either Gradle or Maven, just to make it accessible to more developers. Just check out the project using GIT and build it with either one of these commands:

    $ gradle build

or

    $ mvn install

The native libraries should not need any more changes. If they do, see below.
 
 
Building Native Libraries
-------------------------

For the desktop support, this API uses native libraries, wrapped with JNI. In order to re-compile the native code, the proper environment is required for each platform. The first releases were built with:

 * Build scripts are included in "gamepad4j-desktop/src/build/"
 * Compile / link with GCC on Linux and Windows. Version 4.7.3 was used on Linux
 * Use MinGW on Windows for the GCC build (only minimal installation required)
 * On MacOS, the Apple XCode SDK is required.
 
You will have to adapt some environment variables in the corresponding build scripts, according to your environment, before you can compile the code.

Once compiled, the native libraries have been copied into the resources folder. Just rebuild the Jar file, and bob's your uncle.
 
