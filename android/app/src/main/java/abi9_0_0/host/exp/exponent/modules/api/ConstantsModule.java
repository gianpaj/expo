// Copyright 2015-present 650 Industries. All rights reserved.

package abi9_0_0.host.exp.exponent.modules.api;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.facebook.device.yearclass.YearClass;
import abi9_0_0.com.facebook.react.bridge.ReactApplicationContext;
import abi9_0_0.com.facebook.react.bridge.ReactContextBaseJavaModule;

import org.json.JSONObject;

import javax.inject.Inject;

import host.exp.exponent.ExponentManifest;
import host.exp.exponent.di.NativeModuleDepsProvider;
import host.exp.exponent.kernel.ExpoViewKernel;
import host.exp.exponent.storage.ExponentSharedPreferences;

public class ConstantsModule extends ReactContextBaseJavaModule {

  @Inject
  ExponentSharedPreferences mExponentSharedPreferences;

  private int mStatusBarHeight = 0;
  private final Map<String, Object> mExperienceProperties;
  private String mSessionId = UUID.randomUUID().toString();
  private JSONObject mManifest;

  private static int convertPixelsToDp(float px, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = px / (metrics.densityDpi / 160f);
    return (int) dp;
  }

  public ConstantsModule(
      ReactApplicationContext reactContext,
      Map<String, Object> experienceProperties,
      JSONObject manifest) {
    super(reactContext);
    NativeModuleDepsProvider.getInstance().inject(ConstantsModule.class, this);

    mManifest = manifest;

    if (!manifest.has(ExponentManifest.MANIFEST_STATUS_BAR_COLOR)) {
      int resourceId = reactContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
      if (resourceId > 0) {
        int statusBarHeightPixels = reactContext.getResources().getDimensionPixelSize(resourceId);
        // Convert from pixels to dip
        mStatusBarHeight = convertPixelsToDp(statusBarHeightPixels, reactContext);
      }
    } else {
      mStatusBarHeight = 0;
    }
    mExperienceProperties = experienceProperties;
  }

  @Override
  public String getName() {
    return "ExponentConstants";
  }

  @Nullable
  @Override
  public Map<String, Object> getConstants() {
    Map<String, Object> constants = new HashMap<>();
    constants.put("sessionId", mSessionId);
    constants.put("exponentVersion", ExpoViewKernel.getInstance().getVersionName());
    constants.put("statusBarHeight", mStatusBarHeight);
    constants.put("deviceYearClass", YearClass.get(getReactApplicationContext()));
    constants.put("deviceId", mExponentSharedPreferences.getOrCreateUUID());
    constants.put("deviceName", Build.MODEL);
    constants.put("manifest", mManifest.toString());
    constants.put("isDevice", !isRunningOnGenymotion() && !isRunningOnStockEmulator());
    if (mExperienceProperties != null) {
      constants.putAll(mExperienceProperties);
    }
    return constants;
  }

  private static boolean isRunningOnGenymotion() {
    return Build.FINGERPRINT.contains("vbox");
  }

  private static boolean isRunningOnStockEmulator() {
    return Build.FINGERPRINT.contains("generic");
  }
}
