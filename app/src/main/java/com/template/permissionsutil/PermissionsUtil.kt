package com.template.permissionsutil

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment

object PermissionsUtil {


    fun Context.requestFragmentPermission(fragment: Fragment, requestCode: Int): Boolean {
        val permissionList = ArrayList<String>()

        val hasWritePermission =
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val hasCameraPermission =
            checkSelfPermission(android.Manifest.permission.CAMERA)
        if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.CAMERA)
        }
        if (permissionList.isNotEmpty()) {
            fragment.requestPermissions(
                permissionList.toTypedArray(),
                requestCode
            )
            return false
        }
        return true

    }


    fun Context.arePermissionsGranted(requestCode: Int): Boolean {
        val permissionList = ArrayList<String>()

        val hasWritePermission =
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val hasCameraPermission =
            checkSelfPermission(android.Manifest.permission.CAMERA)
        if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.CAMERA)
        }
        if (permissionList.isNotEmpty()) {
            requestPermissions(
                this as Activity,
                permissionList.toTypedArray(),
                requestCode
            )
            return false
        }
        return true
    }
}