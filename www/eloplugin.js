function EloPlugin() {}

EloPlugin.prototype.fetchParams = function(successCallback, errorCallback) {
  var options = {};
  cordova.exec(successCallback, errorCallback, 'EloPlugin', 'fetchParams', [options]);
}

// Installation constructor that binds EloPlugin to window
EloPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.eloPlugin = new EloPlugin();
  return window.plugins.eloPlugin;
};
cordova.addConstructor(EloPlugin.install);