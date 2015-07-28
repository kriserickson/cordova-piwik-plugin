# Piwik Analytics Plugin #

Native Piwik Analytics based upon the [Piwik SDK for Android](https://github.com/piwik/piwik-sdk-android) and 
[PiwikTracker iOS SDK](https://github.com/piwik/piwik-sdk-ios).

* This plugin is built for Cordova >= v3.3.0..

## Supported Platforms ##

* Android
* <strike>iOS</strike> *Half Working, YMMV*

## Installation ##

 Piwik Analytics Plugin is compatible with [Cordova Plugman](https://github.com/apache/cordova-plugman), compatible with [PhoneGap 3++ CLI](http://docs.phonegap.com/en/edge/guide_cli_index.md.html#The%20Command-Line%20Interface).
 
```
$ cordova plugin add https://github.com/kriserickson/cordova-piwik-plugin
```

ote: The iOS version does not yet suppport tracking goals and custom variables...  And you have to change the C Language Dialect in Xcode to get the app to compile (see [Github Issues](https://github.com/kriserickson/cordova-piwik-plugin/issues/1#issuecomment-114652098) for more detail).

## Usage ##

Before you do anything you must initialize the tracker with startTracker.  The trackerUrl is the url of your piwik tracker (see [how to track Mobile apps usage](http://piwik.org/blog/2012/04/how-to-use-piwik-to-track-mobile-apps-activity-clicks-phones-errors-etc/) for more information
on setting up Piwik as a mobile app tracker), and the applicationId is the applicationId you have created for your tracker.  
Passing a userId is optional, and should respect the users privacy and any platform limitations about passing around things like the device.uuid.

```
piwik.startTracker(trackerUrl, applicationId, [userId], [success], [error]);
```

Tracking a screen takes a required path, and an optional title.

```
piwk.trackScreenView(path, [title], [success], [error]);
```

Tracking an exception is done by passing the class the exception occurred in, as well a description.  You can optionally state whether it was
a fatal error (boolean).

```
piwik.trackException(class, description, [isFatal], [success], [error]);
```

Tracking an event requires a category and an action, the label and value are both optional.  

```
piwik.trackEvent(category, action, [label], [value], [success], [error]);
```

Tracking a goal takes requires a goalId (which is an integer and must be set up from your Piwik Administrator Panel).  Revenue associated with said goal is optional.

```
piwik.trackGoal(idGoal, [revenue], [success], [error]);
```
	
You can set customVariables by screen and by user.  Index is an integer between 1 and 5 (by default Piwik currently limits 5 variables per site	however this can be 
be [changed in the configuration](http://piwik.org/faq/how-to/faq_17931/)).  name and value are strings but must be shorter than 200 characters.  For more information
see [Custom Variable Analytics](http://piwik.org/docs/custom-variables/) in the Piwik documentation.
	
```	
piwik.setScreenCustomVariable(index, name, value, [success], [error]) {
piwik.setUserCustomVariable = function(index, name, value, success, error) {
```
