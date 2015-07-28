package com.storefront.cordova.piwik;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.piwik.sdk.Tracker;

import java.net.MalformedURLException;


/**
 * This class echoes a string called from JavaScript.
 */
public class Piwik extends CordovaPlugin {
    public static final String START_TRACKER = "startTracker"; // [trackerUrl, applicationId, userId]);
    public static final String TRACK_SCREEN_VIEW = "trackScreenView"; // [path, title]);
    public static final String TRACK_EVENT = "trackEvent"; // [category, action, label, value]);
    public static final String TRACK_GOAL = "trackGoal"; // [idGoal, revenue]);
    public static final String TRACK_EXCEPTION = "trackException"; // [class, description, isFatal]
    public static final String SET_SCREEN_CUSTOM_VARIABLE = "setScreenCustomVariable"; // [index, name, value]);
    public static final String SET_USER_CUSTOM_VARIABLE = "setUserCustomVariable"; // [index, name, value]);

    Tracker piwikTracker = null;


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (action.equals(START_TRACKER)) {
            String trackerUrl;
            int applicationId;
            String userId;
            try {
                trackerUrl = args.getString(0);
            } catch (JSONException ex) {
                callbackContext.error("Tracker URL does not appear to be a valid String");
                return true;
            }
            try {
                applicationId = args.getInt(1);
            } catch (JSONException ex) {
                callbackContext.error("Appilcation id does not appear to be a valid integer");
                return true;
            }
            userId = args.opt(2) != null ? args.optString(2) : "";

            this.startTracker(trackerUrl, applicationId, userId, callbackContext);
            return true;
        } else if (action.equals(TRACK_SCREEN_VIEW)) {
            String path;
            try {
                path = args.getString(0);
            } catch (JSONException ex) {
                callbackContext.error("Path does not appear to be a valid string");
                return true;
            }
            String title = args.opt(1) != null ? args.optString(1) : "";
            this.trackScreenView(path, title, callbackContext);
        } else if (action.equals(TRACK_EVENT)) {
            String category;
            String eventAction;
            try {
                category = args.getString(0);
            } catch (JSONException ex) {
                callbackContext.error("Category does not appear to be a valid string");
                return true;
            }
            try {
                eventAction = args.getString(1);
            } catch (JSONException ex) {
                callbackContext.error("Action does not appear to be a valid string");
                return true;
            }
            String label = args.opt(2) != null ? args.optString(2) : "";
            Integer value = args.opt(3) != null ? args.optInt(3) : null;

            this.trackEvent(category, eventAction, label, value, callbackContext);
        } else if (action.equals(TRACK_GOAL)) {
            Integer idGoal;
            try {
                idGoal = args.getInt(0);
            } catch (JSONException ex) {
                callbackContext.error("idGoal does not appear to be a valid integer");
                return true;
            }
            Integer revenue = args.opt(1) != null ? args.optInt(1) : null;
            this.trackGoal(idGoal, revenue, callbackContext);
        } else if (action.equals(TRACK_EXCEPTION)) {
            String className;
            String description;
            Boolean isFatal;
            try {
                className = args.getString(0);
                description = args.getString(1);
                isFatal = args.getBoolean(2);
            } catch (JSONException ex) {
                callbackContext.error(ex.getMessage());
                return true;
            }
            this.trackException(className, description, isFatal, callbackContext);

        } else if (action.equals(SET_USER_CUSTOM_VARIABLE) || action.equals(SET_SCREEN_CUSTOM_VARIABLE)) {
            Integer index;
            String name;
            String value;
            try {
                index = args.getInt(0);
            } catch (JSONException ex) {
                callbackContext.error("index does not appear to be a valid integer");
                return true;
            }
            try {
                name = args.getString(1);
                value = args.getString(2);
            } catch (JSONException ex) {
                callbackContext.error("Name and value must be strings");
                return true;
            }
            if (action.equals(SET_USER_CUSTOM_VARIABLE)) {
                this.setUserCustomVariable(index, name, value, callbackContext);
            } else {
                this.setScreenCustomVariable(index, name, value, callbackContext);
            }

        }
        return false;
    }

    private void trackException(String className, String description, Boolean isFatal, CallbackContext callbackContext) {
        if (piwikTracker == null) {
            callbackContext.error("piwik not initialized with setTracker");
        } else {
            piwikTracker.trackException(className, description, isFatal);
            callbackContext.success("tracked exception for " + className);
        }
    }

    private void setUserCustomVariable(Integer index, String name, String value, CallbackContext callbackContext) {
        if (piwikTracker == null) {
            callbackContext.error("piwik not initialized with setTracker");
        } else {
            piwikTracker.setUserCustomVariable(index, name, value);
            callbackContext.success("Set custom user variable " + name + " to " + value);
        }
    }

    private void setScreenCustomVariable(Integer index, String name, String value, CallbackContext callbackContext) {
        if (piwikTracker == null) {
            callbackContext.error("piwik not initialized with setTracker");
        } else {
            piwikTracker.setScreenCustomVariable(index, name, value);
            callbackContext.success("Set custom screen variable " + name + " to " + value);
        }
    }

    private void trackGoal(Integer idGoal, Integer revenue, CallbackContext callbackContext) {
        if (piwikTracker == null) {
            callbackContext.error("piwik not initialized with setTracker");
        } else {
            if (revenue != null) {
                piwikTracker.trackGoal(idGoal, revenue);
            } else {
                piwikTracker.trackGoal(idGoal);
            }
            callbackContext.success("Tracked goal " + idGoal);
        }
    }

    private void trackEvent(String category, String action, String label, Integer value, CallbackContext callbackContext) {
        if (piwikTracker == null) {
            callbackContext.error("piwik not initialized with setTracker");
        } else {
            piwikTracker.trackEvent(category, action, label.length() == 0  || label.equals("null") ? null : label, value);
            callbackContext.success("Tracked event " + action);
        }
    }


    private void startTracker(String url, int applicationId, String userId, CallbackContext callbackContext) {
        try {
            piwikTracker = org.piwik.sdk.Piwik.getInstance(this.cordova.getActivity().getApplication()).newTracker(url, applicationId);
            if (userId.length() > 0 && !userId.equals("null")) {
                piwikTracker.setUserId(userId);
            }
            callbackContext.success("Started tracker for application: " + applicationId);
        } catch (MalformedURLException e) {
            callbackContext.error("url is malformed");
        }
    }

    private void trackScreenView(String path, String title, CallbackContext callbackContext) {
        if (piwikTracker == null) {
            callbackContext.error("piwik not initialized with setTracker");
        } else {
            if (title.length() > 0 && !title.equals("null")) {
                piwikTracker.trackScreenView(path, title);
            } else {
                piwikTracker.trackScreenView(path);
            }
            callbackContext.success("Tracked page " + path + (title.length() > 0 ? " / " + title : ""));
        }
    }


}
