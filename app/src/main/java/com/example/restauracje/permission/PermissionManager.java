package com.example.restauracje.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager {

    static public void makeRequest(Activity activity, String permission, int requestCode) {
        String[] permissions = new String[]{permission};
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    static public void makeRequest(Activity activity, String[] permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    static public boolean hasPermissionTo(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    static public boolean hasPermissionTo(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
