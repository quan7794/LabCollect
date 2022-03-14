package com.wac.labcollect.ui.fragment

import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentLoginBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.LoginViewModel
import com.wac.labcollect.utils.Resource
import com.wac.labcollect.utils.Status
import timber.log.Timber

@Suppress("DEPRECATION")
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    //constants
    private companion object {
        private const val RC_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //configure the Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener { viewModel.updateProgress(Resource.loading()) }
        viewModel.currentStatus.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING-> {
                    Timber.w("Begin Google sign in")
                    binding.apply {
                        loadingAnimation.progress = 0F
                        loadingAnimation.visibility = View.VISIBLE
                        loginButton.isEnabled = false

                    }
                    signIn()
                }
                Status.SUCCESS -> {
                    binding.loadingAnimation.visibility = View.GONE
                    Toast.makeText(requireContext(), "You Signed In successfully", Toast.LENGTH_LONG).show()
                    navigate(LoginFragmentDirections.toFirstScreenFragment())
                }
                Status.ERROR -> {
                    binding.loadingAnimation.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    Toast.makeText(requireContext(), "Login fail, error: ${it.message}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    private fun signIn() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            Timber.d("Login status: $requestCode, $resultCode, $data")
            if (resultCode == RESULT_OK) {
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
                GoogleSignIn.getLastSignedInAccount(requireContext())?.idToken
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = accountTask.getResult(ApiException::class.java)!!
                    Timber.d("firebaseAuthWithGoogle:" + account.idToken)
                    firebaseAuthWithGoogleAccount(account)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Timber.e("Google sign in failed", e)
                }
            } else {
                viewModel.updateProgress(Resource(Status.ERROR, null, "User canceled login action."))
            }

        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener {updateUI(if (it.isSuccessful) "signInWithCredential:success" else "signInWithCredential:failure (${it.exception})") }
            .addOnFailureListener{updateUI(it.message) }
    }

    private fun updateUI(message: String? = "") {
        if (FirebaseAuth.getInstance().currentUser != null) viewModel.updateProgress(Resource(Status.SUCCESS))
        else viewModel.updateProgress(Resource.error(message = message.toString()))
    }
}