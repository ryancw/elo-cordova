package com.ryancw.cordova.plugin;

import com.eloview.sdk.EloSecureUtil;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.os.Environment;

import java.io.StringReader;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class EloPlugin extends CordovaPlugin {
  @Override
  public boolean execute(String action, JSONArray args,
    final CallbackContext callbackContext) {
      if (!action.equals("fetchParams")) {
        callbackContext.error("\"" + action + "\" is not a recognized action.");
        return false;
      }

      String param;      
      try {
        JSONObject options = args.getJSONObject(0);
        param = options.getString("param");        
      } catch (JSONException e) {
        callbackContext.error("Error encountered: " + e.getMessage());
        return false;
      }
      
      // here for you m'lady, is m'context       
      Context mContext = this.cordova.getActivity().getApplicationContext();
      Properties deviceInfo = this.getDeviceInfo(); //EloSecureUtil.getDeviceInfo(mContext);
      String result = deviceInfo.getProperty(param);

      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
      callbackContext.sendPluginResult(pluginResult); 
      return true;
  }

  private static Properties getDeviceInfo() {
    Properties prop = new Properties();
    FileInputStream in = null;
    File file = null;
    String sdcardEloPath = Environment.getExternalStorageDirectory().getPath() + "/elo/device_info";

    try {
        file = new File(sdcardEloPath, "deviceinfo.properties");
        if (file.exists()) {
            in = new FileInputStream(file);
            prop.load(in);
            in.close();
        }
    } catch (IOException var7) {
        Log.e("EloSecureUtil", "GET_DEVICE_INFO: IOException");
    } catch (Exception var8) {
        Log.e("EloSecureUtil", "GET_DEVICE_INFO: Exception");
    }

    return prop;
  }
}