package com.wac.labcollect.ui.fragment.firstScreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentFirstScreenBinding
import com.wac.labcollect.ui.base.BaseFragment
import timber.log.Timber

class FirstScreenFragment : BaseFragment<FragmentFirstScreenBinding>() {
    private val firstScreenTabItems = arrayListOf(R.string.home, R.string.add, R.string.profile)

    private var authListener = FirebaseAuth.AuthStateListener {
            Timber.e("Is logout: ${it.currentUser == null} ")
            if (it.currentUser == null) {
                val action = FirstScreenFragmentDirections.toLoginFragment()
                activity?.let { it1 -> Navigation.findNavController(it1, R.id.nav_host_fragment).navigate(action) }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewPager.adapter = ViewPagerAdapter(this@FirstScreenFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getString(firstScreenTabItems[position])
            }.attach()
        }
        setAuthListener()
    }

    private fun setAuthListener() = Firebase.auth.addAuthStateListener(authListener)

    override fun onDestroyView() {
        Firebase.auth.removeAuthStateListener(authListener)
        binding.viewPager.adapter = null
        super.onDestroyView()
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
