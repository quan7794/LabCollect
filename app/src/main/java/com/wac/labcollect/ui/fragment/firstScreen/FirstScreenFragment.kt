package com.wac.labcollect.ui.fragment.firstScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentFirstScreenBinding
import com.wac.labcollect.ui.base.BaseFragment
import timber.log.Timber

class FirstScreenFragment : BaseFragment(R.layout.fragment_first_screen) {
    private val firstScreenTabItems = arrayListOf(R.string.home, R.string.add, R.string.profile)
    private var _firstScreenBinding: FragmentFirstScreenBinding? = null
    private val firstScreenBinding: FragmentFirstScreenBinding
    get() = _firstScreenBinding!!

    private var authListener = FirebaseAuth.AuthStateListener {
            Timber.e("Is logout: ${it.currentUser == null} ")
            if (it.currentUser == null) {
                val action = FirstScreenFragmentDirections.toLoginFragment()
                activity?.let { it1 -> Navigation.findNavController(it1, R.id.nav_host_fragment).navigate(action) }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _firstScreenBinding = FragmentFirstScreenBinding.inflate(inflater)
        return firstScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstScreenBinding.apply {
            viewPager.adapter = ViewPagerAdapter(this@FirstScreenFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getString(firstScreenTabItems[position])
            }.attach()
        }
        setAuthListener()
    }

    private fun setAuthListener() = Firebase.auth.addAuthStateListener(authListener)

    override fun onDestroyView() {
        super.onDestroyView()
        Firebase.auth.removeAuthStateListener(authListener)
        firstScreenBinding.viewPager.adapter = null
        _firstScreenBinding = null
    }


}
