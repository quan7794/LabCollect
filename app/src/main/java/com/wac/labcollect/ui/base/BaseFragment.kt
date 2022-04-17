package com.wac.labcollect.ui.base

import android.view.View
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jintin.bindingextension.BindingFragment
import com.wac.labcollect.MainApplication
import com.wac.labcollect.utils.Status

open class BaseFragment<T : ViewBinding> : BindingFragment<T>() {
    val testRepository by lazy { (requireActivity().application as MainApplication).testRepository }
    val googleApiRepository
        get() = (requireActivity().application as MainApplication).googleApiRepository

    fun currentTokenId() = context?.let { GoogleSignIn.getLastSignedInAccount(it)?.idToken }

    fun lastSignedAccount(): GoogleSignInAccount? = context?.let { GoogleSignIn.getLastSignedInAccount(it) }

    fun setUpGoogleAccountCredential() {
        (activity?.application as MainApplication).authManager.setUpGoogleAccountCredential(lastSignedAccount()?.account)
    }

    fun navigate(destination: NavDirections) = with(findNavController()) {
        currentDestination?.getAction(destination.actionId)?.let { navigate(destination) }
    }

    protected fun initProgress(viewModel: BaseViewModel, progressBarId: Int) {
        viewModel.currentStatus.observe(viewLifecycleOwner) {
            it.message?.let { message -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show() }
            binding.root.findViewById<LottieAnimationView>(progressBarId)?.apply {
                when (it.status) {
                    Status.LOADING  -> startAnimation()
                    Status.SUCCESS  -> visibility = View.GONE
                    Status.NOTHING  -> visibility = View.GONE
                    Status.ERROR    -> visibility = View.GONE
                }
            }
        }
    }

    private fun LottieAnimationView.startAnimation() {
        progress = 0F
        visibility = View.VISIBLE
    }

}