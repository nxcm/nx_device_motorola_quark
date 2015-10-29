/*
 * Copyright (c) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import java.util.List;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

public class CameraActivationAction implements SensorAction {
    private static final String TAG = "CMActions";

    private static final int TURN_SCREEN_ON_WAKE_LOCK_MS = 500;
    private static final String GESTURE_CAMERA_KEY = "gesture_camera";

    private final KeyguardManager mKeyguardManager;
    private PowerManager mPowerManager;
    private PackageManager mPackageManager;

    private Context mContext;

    private boolean mGestureCameraEnabled = true;

    public CameraActivationAction(Context context) {
        mContext = context;
        mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mPackageManager = context.getPackageManager();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loadPreferences(sharedPrefs);
        sharedPrefs.registerOnSharedPreferenceChangeListener(mPrefListener);
    }

    @Override
    public void action() {
        if (mGestureCameraEnabled) {
            vibrate();
            turnScreenOn();
            if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
                 launchCameraIntent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
            } else {
                 launchCameraIntent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            }
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    private void turnScreenOn() {
        PowerManager.WakeLock wl = mPowerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
        wl.acquire(TURN_SCREEN_ON_WAKE_LOCK_MS);
    }

    private void launchCameraIntent(String intentName) {
        Intent intent = new Intent(intentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        List <ResolveInfo> activities = mPackageManager.queryIntentActivities(intent, 0);
        if (activities.size() > 0) {
            ActivityInfo activity = activities.get(0).activityInfo;
            ComponentName componentName = new ComponentName(activity.applicationInfo.packageName,
                activity.name);
            intent.setComponent(componentName);
        }
        mContext.startActivity(intent);
    }

    private void loadPreferences(SharedPreferences sharedPreferences) {
        mGestureCameraEnabled = sharedPreferences.getBoolean(GESTURE_CAMERA_KEY, true);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (GESTURE_CAMERA_KEY.equals(key)) {
                mGestureCameraEnabled = sharedPreferences.getBoolean(GESTURE_CAMERA_KEY, true);
            } 
        }
    };
}