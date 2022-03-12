package com.wac.labcollect.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentSplashScreenBinding
import com.wac.labcollect.ui.base.BaseFragment

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.img.animate().translationY(-2500F).setDuration(1000).startDelay = 5000
        binding.lottie.animate().translationY(1500F).setDuration(1000).startDelay = 5000

        Handler(Looper.getMainLooper()).postDelayed({
            val nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToFirstScreenFragment()
            nav.navigate(action)
        }, 3000)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}