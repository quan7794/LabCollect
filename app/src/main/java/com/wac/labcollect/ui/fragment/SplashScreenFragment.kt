package com.wac.labcollect.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentSplashScreenBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>() {

    private var authListener = FirebaseAuth.AuthStateListener {
        val nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val action = if (it.currentUser == null) { SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment() }
        else { SplashScreenFragmentDirections.actionSplashScreenFragmentToFirstScreenFragment() }
        nav.navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.img.animate().translationY(-2500F).setDuration(1000).startDelay = 5000
        binding.lottie.animate().translationY(1500F).setDuration(1000).startDelay = 5000

        Handler(Looper.getMainLooper()).postDelayed({
            setAuthListener()
        }, 6000)
    }

    private fun setAuthListener() = Firebase.auth.addAuthStateListener(authListener)
}