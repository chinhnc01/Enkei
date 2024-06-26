package com.barista_v.image_picker

import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import java.lang.ref.WeakReference

/**
 * Wrapper for permission management.
 *
 * Help checking if permissions were granted, if app needs to show   a rationale and to start
 * permission request from the app.
 */
class PermissionOwner(activity: Fragment) {
  private var weakActivity = WeakReference<Fragment>(activity)

  fun isPermissionGranted(permissions: Array<String>): Boolean {
    return weakActivity.get()?.let {
      Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
          permissions.any { p -> checkSelfPermission(it.context ?: return@let false, p) == PERMISSION_GRANTED }
    } ?: false
  }

  fun shouldShowPermissionRationale(permissions: Array<String>): Boolean = weakActivity.get()?.let {
    permissions.any { p -> shouldShowRequestPermissionRationale(it.activity ?: return@let false, p) }
  } ?: false

  fun requestPermission(permissions: Array<String>, requestCode: Int) =
      weakActivity.get()?.let { requestPermissions(it.activity ?: return@let, permissions, requestCode) }
}
