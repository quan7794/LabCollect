package com.wac.labcollect.ui.fragment.firstScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentProfileBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.utils.NightModeHelper
import kotlinx.coroutines.launch


class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!
    private var auth = FirebaseAuth.getInstance()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.apply {
            logoutBtn.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
            }
            accountEmail.text = auth.currentUser?.email
            accountName.text = auth.currentUser?.displayName
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initData()
        initActionView()
    }

    private fun initActionView() {
        binding.apply {
            manageTemplate.setOnClickListener {
                val action = FirstScreenFragmentDirections.actionFirstScreenFragmentToTemplateManagerFragment()
                this@ProfileFragment.findNavController().navigate(action)
            }

            darkModeSwitch.setOnClickListener { v ->
                val checked = (v as SwitchMaterial).isChecked
                if (checked) {
                    setDarkModeSetting(DarkModeType.TURN_ON)
                } else {
                    setDarkModeSetting(DarkModeType.TURN_OFF)
                }
            }
        }
    }

    private fun initData() {
        lifecycleScope.launchWhenCreated {
            viewModel.getDarkModeSetting(requireContext())
        }
    }

    private fun setDarkModeSetting(value: Int) {
        lifecycleScope.launch {
            NightModeHelper.handleSetNightMode(value)
            viewModel.setDarkModeSetting(requireContext(), value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}