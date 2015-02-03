#import <Cordova/CDV.h>

@interface PiwikPlugin : CDVPlugin {
    bool _trackerStarted; 
}

- (void) startTracker: (CDVInvokedUrlCommand*)command;
- (void) trackScreenView: (CDVInvokedUrlCommand*)command;
- (void) trackEvent: (CDVInvokedUrlCommand*)command;
- (void) trackException: (CDVInvokedUrlCommand*)command; 
- (void) trackGoal: (CDVInvokedUrlCommand*)command;
- (void) setScreenCustomVariable: (CDVInvokedUrlCommand*)command;
- (void) setUserCustomVariable: (CDVInvokedUrlCommand*)command;


@end
