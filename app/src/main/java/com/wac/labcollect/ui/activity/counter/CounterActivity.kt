package com.wac.labcollect.ui.activity.counter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigator
import androidx.navigation.Navigation
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wac.labcollect.R
import com.wac.labcollect.databinding.ActivityCounterBinding
import com.wac.labcollect.ui.activity.BaseActivity
import com.wac.labcollect.ui.fragment.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class CounterActivity : BaseActivity() {

    private val viewModel: CounterViewModel by viewModels()
    private lateinit var binding: ActivityCounterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabLayout.applyInsetter {
            type(navigationBars = true) {
                padding()
            }
        }

        viewModel.value()
            .onEach { binding.value.text = it.toString() }
            .launchIn(lifecycleScope)

        viewModel.errors()
            .onEach { showErrorSnackBar() }
            .launchIn(lifecycleScope)

        binding.fabIncrement.setOnClickListener { viewModel.incrementClick() }
        binding.fabDecrement.setOnClickListener { viewModel.decrementClick() }
    }

    private fun showErrorSnackBar() {
        Snackbar.make(binding.coordinatorLayout, R.string.error_message, Snackbar.LENGTH_SHORT)
            .show()
    }

}