var exec = require('cordova/exec');

var piwikExport = {};

/**
 *
 * @param trackerUrl {String}
 * @param applicationId {Number}
 * @param [userId] {String}
 * @param success {Function}
 * @param [error] {Function}
 */
piwikExport.startTracker = function(trackerUrl, applicationId, userId, success, error) {
    if (typeof userId === 'function') {
        error = success;
        success = userId;
        userId = undefined;
    }
    exec(success, error, "Piwik", "startTracker", [trackerUrl, applicationId, userId]);
};

/**
 *
 * @param path {String}
 * @param title {String}
 * @param success {Function}
 * @param [error] {Function}
 */
piwikExport.trackScreenView = function(path, title, success, error) {
    if (typeof title === 'function') {
        error = success;
        success = title;
        title = undefined;
    }
    exec(success, error, "Piwik", "trackScreenView", [path, title]);
};

/**
 *
 * @param category {String}
 * @param action {String}
 * @param [label] {String}
 * @param [value] {Number}
 * @param success {Function}
 * @param [error] {Function}
 */
piwikExport.trackEvent = function(category, action, label, value, success, error) {
    if (typeof label === 'function') {
        error = success;
        success = label;
        value = undefined;
        label = undefined;
    }
    if (typeof value === 'function') {
        error = success;
        success = value;
        value = undefined;
    }
    exec(success, error, "Piwik", "trackEvent", [category, action, label, value]);
};

/**
 *
 * @param idGoal {Number}
 * @param revenue {Number}
 * @param success {Function}
 * @param [error] {Function}
 */
piwikExport.trackGoal = function(idGoal, revenue, success, error) {
    if (typeof revenue === 'function') {
        error = success;
        success = revenue;
        revenue = undefined;
    }
    exec(success, error, "Piwik", "trackGoal", [idGoal, revenue]);
};


/**
 *
 * @param className {String}
 * @param description {String}
 * @param [isFatal] {Boolean}
 * @param success {Function}
 * @param [error] {Function}
 */
piwikExport.trackException = function(className, description, isFatal, success, error) {
    var fatal = isFatal ? 'true' : 'false';
    if (typeof isFatal === 'function') {
        error = success;
        success = isFatal;
        fatal = 'false';
    } 
    exec(success, error, "Piwik", "trackException", [className, description, fatal]);
};


/**
 *
 * @param index {Number}
 * @param name {String}
 * @param value {String}
 * @param success {Function}
 * @param [error] {Function}
 */
piwikExport.setScreenCustomVariable = function(index, name, value, success, error) {
    if (name.length > 200 || value.length > 200) {
        error('CustomVariables must have a ' + (name.length > 200 ? 'name' : 'value') + ' less than 200 characters');
    } else {
        exec(success, error, "Piwik", "setScreenCustomVariable", [index, name, value]);
    }
};

/**
 *
 * @param index {Number}
 * @param name {String}
 * @param value {String}
 * @param success {Function}
 * @param [error] {Function}
 */
piwikExport.setUserCustomVariable = function(index, name, value, success, error) {
    if (name.length > 200 || value.length > 200) {
        error('CustomVariables must have a ' + (name.length > 200 ? 'name' : 'value') + ' less than 200 characters');
    } else {
        exec(success, error, "Piwik", "setUserCustomVariable", [index, name, value]);
    }
};

module.exports = piwikExport;