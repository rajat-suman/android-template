package com.template

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.template.datastore.DataStoreUtil
import com.template.networkcalls.Repository
import com.template.pref.PreferenceFile
import com.template.utils.showConfirmDialog
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
                showLogout(view)
            }
            R.id.tvSupport -> {
                MainActivity.navListener?.openDrawer()
            }
        }
    }

    private fun showLogout(view: View) {
        view.showConfirmDialog(view.context.getString(R.string.are_your_sure_want_to_logout)){
            navController.popBackStack(R.id.navGraph, true)
            navController.navigate(R.id.login)
        }
    }

}
