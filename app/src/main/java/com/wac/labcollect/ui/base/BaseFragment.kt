package com.wac.labcollect.ui.base

import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jintin.bindingextension.BindingFragment
import com.wac.labcollect.MainApplication

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
}