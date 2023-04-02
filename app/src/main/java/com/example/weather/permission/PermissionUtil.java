package com.example.weather.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * UI thread only.
 */
public class PermissionUtil {

    /**
     * This function check permission, also try to request permission if not granted yet.
     *
     * @param context The context to use
     * @param permission The permission to check/request
     * @param activityResultLauncher
     * @param rationaleUiRunnable
     * @return True if already have the permission, otherwise false
     */
    public static boolean checkPermission(
            Activity context,
            String permission,
            ActivityResultLauncher<String> activityResultLauncher,
            Runnable rationaleUiRunnable) {
        Log.e("here", "here in PermissionUtil.checkPermission");
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.e("here", "here in PermissionUtil.checkPermission, PERMISSION_GRANTED");
            return true;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            Log.e("here", "here in PermissionUtil.checkPermission, shouldShowRequestPermissionRationale");
            rationaleUiRunnable.run();
            return false;
        } else {
            Log.e("here", "here in PermissionUtil.checkPermission, activityResultLauncher.launch(permission)");
            activityResultLauncher.launch(permission);
            return false;
        }
    }
}
