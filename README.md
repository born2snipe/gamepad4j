Gamepad4J
=========

A simple, elegant and powerful API for using gamepads like the Xbox 360 or DualShock 3 pad in Java-based games. Release 1.0 planned before end of 2014 (releases on SourceForge).

 
Features
--------

  * The core API provides simple, object-oriented access to the buttons, analog sticks and triggers of a common gamepad.
  * Convenience methods for getting the direction (degrees) and distance to center of an analog stick
  * Constants for querying specific buttons, like "lower face button" or "upper left shoulder button"
  * Constants for handling common "accept" and "deny" buttons
  * Predefined text keys and labels for all the buttons, so that with a DualShock 3, a message can be displayed like "Press X to accept", while with an Xbox 360 pad, it would be "Press A to accept".
  * Low-level methods for accessing all analog axes and digital buttons of a controller
  * Single Uber-jar contains all wrappers for all supported platforms
  * The executable Uber-jar also contains a mapper tool that can be used to create new mappings for desktop controllers.
  * Provides clean Gradle and Maven build for the Java part


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