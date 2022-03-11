package com.wac.labcollect.ui.base

import androidx.viewbinding.ViewBinding
import com.jintin.bindingextension.BindingFragment
import com.wac.labcollect.MainApplication

open class BaseFragment<T: ViewBinding> : BindingFragment<T>() {
    val testRepository by lazy { (requireActivity().application as MainApplication).repository }
}