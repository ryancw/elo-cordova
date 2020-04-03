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
      String deviceInfo = this.getDeviceInfo(); //EloSecureUtil.getDeviceInfo(mContext);

      Properties prop = new Properties();
      try {
        prop.load(new StringReader(deviceInfo));
      } catch (IOException e) {
        callbackContext.error("Error encountered: " + e.getMessage());
        return false;
      }

      String result = prop.getProperty(param);

      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, deviceInfo);
      callbackContext.sendPluginResult(pluginResult); 
      return true;
  }

  private static String getDeviceInfo() {
    String ret = null;
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
            ret = prop.toString();
        }
    } catch (IOException var7) {
        Log.e("EloSecureUtil", "GET_DEVICE_INFO: IOException");
    } catch (Exception var8) {
        Log.e("EloSecureUtil", "GET_DEVICE_INFO: Exception");
    }

    return ret;
  }
}