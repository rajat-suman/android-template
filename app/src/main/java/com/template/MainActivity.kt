package com.template

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.template.databinding.ActivityMainBinding
import com.template.interfaces.NavigationListener
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationListener {

    lateinit var binding: ActivityMainBinding
    private val mainVM: MainVM by viewModels()


    companion object {
        var navListener: NavigationListener? = null
        lateinit var context: WeakReference<Context>
    }

    override fun onStart() {
        super.onStart()
        context = WeakReference(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT)
        mainVM.navController = findNavController(R.id.fragmentMain)
        binding.vm = mainVM
        checkUpdate()
        setListeners()
        navListener = this
    }

    /***
     * Set listener
     * */
    private fun setListeners() {
        binding.drawerLayout.addDrawerListener(object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.nav_app_bar_open_drawer_description,
            R.string.nav_app_bar_navigate_up_description
        ) {
            private val scaleFactor = 6f

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset
                binding.content.translationX = slideX
                binding.content.scaleX = 1 - slideOffset / scaleFactor
                binding.content.scaleY = 1 - slideOffset / scaleFactor
            }
        })
    }

    /**
     * ovveride onBackPressed method
     * */
    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        if (mainVM.navController.backStack.size == 2) {
            showExit()
        } else
            super.onBackPressed()
    }

    /***
     * Check app update from playstore
     * */
    private fun checkUpdate() {
        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.UPDATE_AVAILABLE
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        1234
                    )
                }
            }
    }

    /***
     * Override result method
     * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1234 -> {
                val TAG = "null"
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d(TAG, "" + "Result Ok")
                        //  handle user's approval }
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.d(TAG, "" + "Result Cancelled")
                        //  handle user's rejection  }
                    }
                    ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                        //if you want to request the update again just call checkUpdate()
                        Log.d(TAG, "" + "Update Failure")
                        //  handle update failure
                    }
                }
            }
        }
    }

    /***
     * If want to LockDrawer
     *
     * */
    override fun isLockDrawer(isLock: Boolean) {
        if (isLock) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    /***
     * Open drawer listener
     * */
    override fun openDrawer() {
        try {
            Log.e("openDrawer", "ddsdsd")
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navListener = null
    }

    private fun showExit() {
        val aD = AlertDialog.Builder(this)
        aD.setTitle(getString(R.string.exit_message))
        aD.setCancelable(false)
        aD.setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
            dialogInterface.cancel()
            dialogInterface.dismiss()
            finishAffinity()
        }
        aD.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i ->
            dialogInterface.cancel()
            dialogInterface.dismiss()
        }
        aD.create()
        aD.show()
    }

}