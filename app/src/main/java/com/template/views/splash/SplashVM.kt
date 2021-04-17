package com.template.views.splash

import android.view.View
import androidx.lifecycle.ViewModel
import com.template.datastore.DataStoreUtil
import com.template.networkcalls.Repository
import com.template.pref.PreferenceFile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil
) : ViewModel() {
    init {

    }

    fun onClick(view: View) {
        when (view.id) {

        }
    }

}