package com.wac.labcollect.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crazylegend.viewbinding.viewBinding
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentFirstScreenBinding

class FirstScreenFragment : Fragment(R.layout.fragment_first_screen) {
    private val firstScreenBinding by viewBinding(FragmentFirstScreenBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return firstScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstScreenFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
