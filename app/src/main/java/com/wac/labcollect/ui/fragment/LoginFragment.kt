package com.wac.labcollect.ui.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.ktx.Firebase
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentLoginBinding
import com.wac.labcollect.ui.activity.counter.CounterActivity
import timber.log.Timber
import kotlin.math.sign

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    //constants
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //configure the Google Sign In
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        //init firebase auth
        auth = FirebaseAuth.getInstance()

    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        var user = auth.currentUser
        updateUI(user)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.googleBtn.setOnClickListener {
            Timber.w("Begin Google sign in")
            signIn()
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
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = accountTask.getResult(ApiException::class.java)!!
                Timber.d("firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w("Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.w("signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w(task.exception, "signInWithCredential:failure")
                    updateUI(null)
                }
            }
//            .addOnSuccessListener { task ->
//                // Sign in success, update UI with the signed-in user's information
//                //get loggedIn user
//                val user = auth.currentUser
//                //get user info
//                val uid = user!!.uid
//                val email =user.email
//                Timber.w("GG Account : Uid: $uid")
//                Timber.w("GG Account : Email: $email")
//                if (task.additionalUserInfo!!.isNewUser){
//                    Timber.w("Account Created!")
//                    Toast.makeText(requireContext(), "Account Created ... \n" +
//                            "$email", Toast.LENGTH_LONG).show()
//                }else
//                {
//                    Timber.w("Existing user...")
//                    Toast.makeText(requireContext(), "LoggedIn ... \n" +
//                            "$email", Toast.LENGTH_LONG).show()
//                }
//                updateUI(user)
//            }
            .addOnFailureListener{e ->
                Timber.w("Loggin Failed!")
                Toast.makeText(requireContext(), "Loggin Failed! \n" +
                        "${e.message}", Toast.LENGTH_LONG).show()
            }

    }
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(requireContext(), "You Signed In successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(requireContext(), CounterActivity::class.java))
        } else {
            auth.signOut()
            Toast.makeText(requireContext(), "You Didnt signed in", Toast.LENGTH_LONG).show()
        }
    }


}