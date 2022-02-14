package com.template.imagevideopicker

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.template.MainActivity
import com.template.R

object PermissionsUtil {

    /**CAMERA PERMISSION*/
    fun cameraPermission(
        approved: (Boolean) -> Unit
    ) {
        checkPermissions(
            Manifest.permission.CAMERA
        ) {
            approved(it)
        }
    }

    /**STORAGE PERMISSION*/
    fun storagePermission(approved: (Boolean) -> Unit) {
        checkPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            approved(it)
        }
    }

    /**LOCATION PERMISSION*/
    fun locationPermission(approved: (Boolean) -> Unit) {
        checkPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            approved(it)
        }
    }

    /**Check Permission*/
    private fun checkPermissions(vararg list: String, returnValue: (Boolean) -> Unit) = try {
        val activity = (MainActivity.context.get() as Activity)
        val check = Dexter.withContext(activity)
            .withPermissions(*list)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    returnValue(p0?.areAllPermissionsGranted() ?: false)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
        check
    } catch (e: Exception) {
        e.printStackTrace()
    }

    /**Alert for Permission Necessity*/
    fun permissionAlert(
        locationPermission: Boolean = false,
        cameraPermission: Boolean = false,
        storagePermission: Boolean = false,
        requestPermission: (Boolean) -> Unit
    ) {
        val context = MainActivity.context.get()
        val aD = android.app.AlertDialog.Builder(context)
        val title = when {
            locationPermission -> context?.getString(R.string.location_permission_required)
            cameraPermission -> context?.getString(R.string.camera_permission_required)
            storagePermission -> context?.getString(R.string.storage_permission_required)
            else -> ""
        }
        aD.setTitle(title)
        aD.setCancelable(false)
        aD.setPositiveButton(context?.getString(R.string.ok)) { dialogInterface, _ ->
            dialogInterface.dismiss()
            requestPermission(true)
        }
        aD.setNegativeButton(context?.getString(R.string.cancel)) { dialogInterface, _ ->
            dialogInterface.cancel()
            requestPermission(false)
        }
        aD.create()
        aD.show()
    }
}