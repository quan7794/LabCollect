package com.wac.labcollect.ui.fragment.viewPager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.wac.labcollect.databinding.FragmentHomeBinding
import com.wac.labcollect.ui.fragment.FirstScreenFragmentDirections

class HomeFragment : Fragment() {
    private  var binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding!!.logoutBtn.setOnClickListener {
            auth.signOut()
            checkUser(it)
        }
        return binding!!.root
    }

    private fun checkUser(view: View) {
        val handle = Handler(Looper.getMainLooper())
        handle.postDelayed({
            val firebaseUser = auth.currentUser
            if (firebaseUser == null) {
                val action = FirstScreenFragmentDirections.toLoginFragment()
                Navigation.findNavController(view).navigate(action)
            }
        }, 3000)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUser(binding!!.root)

    }
}