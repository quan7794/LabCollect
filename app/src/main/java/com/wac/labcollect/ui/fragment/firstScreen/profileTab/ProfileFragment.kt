package com.wac.labcollect.ui.fragment.firstScreen.profileTab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.wac.labcollect.databinding.FragmentProfileBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.DarkModeType
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections
import com.wac.labcollect.ui.fragment.firstScreen.ProfileViewModel
import com.wac.labcollect.utils.NightModeHelper
import kotlinx.coroutines.launch


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private var auth = FirebaseAuth.getInstance()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            logoutBtn.setOnClickListener { FirebaseAuth.getInstance().signOut() }
            accountEmail.text = auth.currentUser?.email
            accountName.text = auth.currentUser?.displayName
        }
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
        lifecycleScope.launchWhenCreated { viewModel.getDarkModeSetting(requireContext()) }
    }

    private fun setDarkModeSetting(value: Int) {
        lifecycleScope.launch {
            NightModeHelper.handleSetNightMode(value)
            viewModel.setDarkModeSetting(requireContext(), value)
        }
    }

}