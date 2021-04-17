package com.template.views.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.template.R
import com.template.databinding.SplashBinding
import com.template.utils.navigateWithId

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Splash : Fragment(R.layout.splash) {

    var binding: SplashBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SplashBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            binding?.root?.navigateWithId(R.id.action_splash_to_login)
        }, 2000)
    }

}