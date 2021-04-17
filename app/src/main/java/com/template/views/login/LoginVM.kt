package com.template.views.login

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.datastore.LOGIN_DATA
import com.template.datastore.REMEMBER
import com.template.models.LoginData
import com.template.models.SignUpData
import com.template.models.TokenData
import com.template.networkcalls.ApiEnums
import com.template.networkcalls.ApiProcessor
import com.template.networkcalls.Repository
import com.template.networkcalls.RetrofitApi
import com.template.pref.PreferenceFile
import com.template.pref.refreshToken
import com.template.pref.token
import com.template.pref.tokenType
import com.template.utils.showPositiveAlerter
import com.template.validations.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val repository: Repository,
    private val dataStore: DataStoreUtil,
    private val preferences: PreferenceFile
) : ViewModel() {

    var isLogout: Boolean? = false
    val username by lazy { ObservableField("") }
    val password by lazy { ObservableField("") }
    val deviceToken = "dbhbvdhv"

    init {
        MainActivity.navListener?.isLockDrawer(true)
        if (isLogout!!) {
            NavHostFragment.findNavController(Login()).popBackStack()
        }
    }

    fun onClick(view: View) {
        when (view.id) {

            R.id.tvForgotPassword -> {
            }

            R.id.btSignIn -> {
                val context = MainActivity.context.get()

                val validations = Validation.create(context).apply {
                    isEmpty(username.get(), context?.getString(R.string.enter_email_or_phone) ?: "")
                    isEmailValid(
                        username.get(),
                        context?.getString(R.string.enter_valid_email_or_password) ?: ""
                    )
                    isEmpty(password.get(), context?.getString(R.string.enter_password) ?: "")
                }
                if (validations.isValid()) {
//                    login(view = view)
                }
            }

            R.id.tvSignup -> {
            }
        }
    }

    private fun login(view: View?) = viewModelScope.launch {
        repository.makeCall(
            apiKey = ApiEnums.LOGIN,
            loader = true,
            saveInCache = false,
            requestProcessor = object : ApiProcessor<Response<LoginData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<LoginData> {
                    return retrofitApi.login(
                        username = username.get()?.trim(),
                        password = password.get()?.trim(),
                        deviceType = "ANDROID",
                        deviceToken = deviceToken
                    )
                }

                override fun onResponse(res: Response<LoginData>) {
                    val context = MainActivity.context.get()
                    if (res.body()?.isProfileComplete == true) {
                        val signUpData = SignUpData().apply {
                            _id = res.body()?._id ?: ""
                            email = res.body()?.email ?: ""
                            phone = res.body()?.phone ?: ""
                        }
                        val verificationToken = TokenData().apply {
                            type = res.body()?.type ?: ""
                            token = res.body()?.token ?: ""
                        }
                        val bundle = Bundle().apply {
                            putString("type", "signUp")
                            putSerializable("signUpData", signUpData)
                            putSerializable("verificationToken", verificationToken)
                        }

                    } else {
                        dataStore.saveObject(key = LOGIN_DATA, value = res.body())
                        dataStore.saveData(key = REMEMBER, value = true)
                        preferences.storeKey(token, res.body()?.token ?: "")
                        preferences.storeKey(refreshToken, res.body()?.refreshToken ?: "")
                        preferences.storeKey(tokenType, res.body()?.type ?: "")
                        context?.showPositiveAlerter(context.getString(R.string.logged_in_successfully))
                    }
                }
            }
        )
    }

}