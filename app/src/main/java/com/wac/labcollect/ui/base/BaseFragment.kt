package com.wac.labcollect.ui.base

import android.accounts.Account
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jintin.bindingextension.BindingFragment
import com.wac.labcollect.MainApplication

open class BaseFragment<T: ViewBinding> : BindingFragment<T>() {
    val testRepository by lazy { (requireActivity().application as MainApplication).repository }

    fun currentTokenId() = context?.let { GoogleSignIn.getLastSignedInAccount(it)?.idToken }

    fun lastSignedAccount(): GoogleSignInAccount? = context?.let { GoogleSignIn.getLastSignedInAccount(it) }

    fun navigate(destination: NavDirections) = with(findNavController()) {
        currentDestination?.getAction(destination.actionId)?.let { navigate(destination) }
    }
}