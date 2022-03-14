package com.wac.labcollect.ui.base

import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.jintin.bindingextension.BindingFragment
import com.wac.labcollect.MainApplication

open class BaseFragment<T: ViewBinding> : BindingFragment<T>() {
    val testRepository by lazy { (requireActivity().application as MainApplication).repository }
    val currentTokenId: String?
        get() = GoogleSignIn.getLastSignedInAccount(requireContext())?.idToken

    fun navigate(destination: NavDirections) = with(findNavController()) {
        currentDestination?.getAction(destination.actionId)?.let { navigate(destination) }
    }
}