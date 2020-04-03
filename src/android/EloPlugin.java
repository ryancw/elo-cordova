package com.ryancw.cordova.plugin;

import com.eloview.sdk.EloSecureUtil;
import android.content.Context;

import java.io.StringReader;
import java.util.Properties;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;

public class EloPlugin extends CordovaPlugin {
  @Override
  public boolean execute(String action, JSONArray args,
    final CallbackContext callbackContext) {
      if (!action.equals("fetchParams")) {
        callbackContext.error("\"" + action + "\" is not a recognized action.");
        return false;
      }

      // here for you m'lady, is m'context       
      Context mContext = this.cordova.getActivity().getApplicationContext();

      String ret = EloSecureUtil.getDeviceInfo(mContext);          
      //String result = parseProp(ret, "pin");
      Properties prop = new Properties();
      try {
        prop.load(new StringReader(ret));
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      JSONObject jsonProps = new JSONObject(prop);
      String result = jsonProps.toString();

      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
      callbackContext.sendPluginResult(pluginResult);
      return true;
  }

  // private String parseProp(String str, String key) {
  //     // Properties prop = new Properties();
  //     // prop.load(new StringReader(str));
  //     // prop.getProperty(key);
  // }
}