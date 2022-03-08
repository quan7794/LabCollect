package com.wac.labcollect.ui.fragment.firstScreen

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentHomeBinding
import com.wac.labcollect.databinding.FragmentProfileBinding
import com.wac.labcollect.ui.activity.mainActivity.MainViewModel
import com.wac.labcollect.ui.fragment.FirstScreenFragmentDirections
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.switchmaterial.SwitchMaterial
import com.wac.labcollect.utils.NightModeHelper
import com.wac.labcollect.viewModel.DarkModeType
import com.wac.labcollect.viewModel.SettingViewModel
import kotlinx.coroutines.launch


class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!
    private var auth = FirebaseAuth.getInstance()
    private lateinit var viewModel: SettingViewModel

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

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
        viewModel = ViewModelProvider(requireActivity())[SettingViewModel::class.java]
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initData()
        initActionView()
    }

    private fun initActionView() {
        binding?.apply {
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