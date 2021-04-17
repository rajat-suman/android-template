package com.template

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.template.datastore.DataStoreUtil
import com.template.networkcalls.Repository
import com.template.pref.PreferenceFile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil
) : ViewModel() {

    lateinit var navController: NavController

    fun onClick(view: View) {

        when (view.id) {
            R.id.tvHome -> {
                MainActivity.navListener?.openDrawer()
            }

            R.id.tvChangePassword -> {
                MainActivity.navListener?.openDrawer()
            }
            R.id.tvProfile -> {
                MainActivity.navListener?.openDrawer()
            }
            R.id.tvBookings -> {
                MainActivity.navListener?.openDrawer()
            }
            R.id.tvEarnings -> {
                MainActivity.navListener?.openDrawer()
            }
            R.id.tvNotifications -> {
                MainActivity.navListener?.openDrawer()
            }
            R.id.tvContactUs -> {
                MainActivity.navListener?.openDrawer()
            }
            R.id.tvLogout -> {
                MainActivity.navListener?.openDrawer()
                showLogout(view)
            }
            R.id.tvSupport -> {
                MainActivity.navListener?.openDrawer()
            }
        }
    }

    private fun showLogout(view: View) {
        val aD = android.app.AlertDialog.Builder(view.context)
        aD.setTitle(view.context.getString(R.string.are_your_sure_want_to_logout))
        aD.setCancelable(false)
        aD.setPositiveButton(view.context.getString(R.string.ok)) { dialogInterface, i ->
            dialogInterface.cancel()
            dialogInterface.dismiss()
            val bundle = Bundle().apply {
                putBoolean("isLogout", true)
            }
            navController.popBackStack(R.id.dignity_driver, true)
            navController.navigate(R.id.login, bundle)
        }
        aD.setNegativeButton(view.context.getString(R.string.cancel)) { dialogInterface, i ->
            dialogInterface.cancel()
            dialogInterface.dismiss()
        }
        aD.create()
        aD.show()
    }

}
