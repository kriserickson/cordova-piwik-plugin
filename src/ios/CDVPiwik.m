/********* CDVPiwik.m Cordova Plugin Implementation *******/

#import "CDVPiwik.h"
#import "PiwikTracker.h"

@implementation PiwikPlugin



- (void) startTracker: (CDVInvokedUrlCommand*)command {
    CDVPluginResult* pluginResult = nil;
    NSString* url = [command.arguments objectAtIndex:0];
    NSString* siteId = [command.arguments objectAtIndex:1];
    // Todo: handle userId 

    [PiwikTracker sharedInstanceWithSiteID:siteId baseURL:[NSURL URLWithString:url]];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

    [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    _trackerStarted = true;

}

- (void) trackScreenView: (CDVInvokedUrlCommand*)command {
    CDVPluginResult* pluginResult = nil;

    if ( ! _trackerStarted) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Tracker not started"];
      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
      return;
    }

    NSString *path = nil;
    NSString *title = nil;

    if ([command.arguments count] > 0) {
        path = [command.arguments objectAtIndex:0];
    }

    if ([command.arguments count] > 1) {
        title = [command.arguments objectAtIndex:1];
    }

    [[PiwikTracker sharedInstance] sendViews:path, title, nil];

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];


}

- (void) trackEvent: (CDVInvokedUrlCommand*)command {
    CDVPluginResult* pluginResult = nil;

    if ( ! _trackerStarted) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Tracker not started"];
      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
      return;
    }

    NSString *category = nil;
    NSString *action = nil;
    NSString *label = nil;
    NSNumber *value = nil;

    if ([command.arguments count] > 0) {
        category = [command.arguments objectAtIndex:0];
    }

    if ([command.arguments count] > 1) {
        action = [command.arguments objectAtIndex:1];
    }

    if ([command.arguments count] > 2) {
        label = [command.arguments objectAtIndex:2];
    }

    if ([command.arguments count] > 3) {
        value = [command.arguments objectAtIndex:3];
    }

    [[PiwikTracker sharedInstance] sendEventWithCategory:category action:action name:label value:value];

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

}

- (void) trackException: (CDVInvokedUrlCommand*)command {
     CDVPluginResult* pluginResult = nil;

    if ( ! _trackerStarted) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Tracker not started"];
      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
      return;
    }

    NSString *className = nil;
    NSString *description = nil;
    NSString *isFatal = nil;

    if ([command.arguments count] > 0) {
        className = [command.arguments objectAtIndex:0];
    } else {
        className = @"";
    }

    if ([command.arguments count] > 1) {
        description = [command.arguments objectAtIndex:1];
    } else {
        description = @"";
    }

    if ([command.arguments count] > 2) {
        isFatal = [command.arguments objectAtIndex:2];
    }

    NSString *fullDescription = [NSString stringWithFormat:@"%@ - %@", className, description];


    [[PiwikTracker sharedInstance] sendExceptionWithDescription:fullDescription isFatal:NO];

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

}

- (void) trackGoal: (CDVInvokedUrlCommand*)command {

}

- (void) setScreenCustomVariable: (CDVInvokedUrlCommand*)command {

}

- (void) setUserCustomVariable: (CDVInvokedUrlCommand*)command {


}


@end
