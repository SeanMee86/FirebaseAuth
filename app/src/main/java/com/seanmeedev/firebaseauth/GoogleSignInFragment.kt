package com.seanmeedev.firebaseauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_google_sign_in.*

/**
 * A simple [Fragment] subclass.
 * Use the [GoogleSignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoogleSignInFragment : Fragment(R.layout.fragment_google_sign_in), View.OnClickListener {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_in_button.setOnClickListener(this)
        sign_out_button.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RCA_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch(e: ApiException){
                Log.w(TAG, "Google signin failed", e)
            }
        }
    }

    fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RCA_SIGN_IN)
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        updateUI(null)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if(task.isSuccessful) {
                    Log.d(TAG, "SignInWithCredential: success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "SignInWithCredential:failure", task.exception)
                    Snackbar.make(mainActivity, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    fun updateUI(user: FirebaseUser?){
        if( user == null ) {
            isLoggedInGoogle.text = "We are not logged in"
            sign_in_button.visibility = View.VISIBLE
        } else {
            isLoggedInGoogle.text = getString(R.string.logged_in_text, user.email)
            sign_in_button.visibility = View.GONE
        }
    }

    companion object {
        private const val RCA_SIGN_IN = 9001
        private const val TAG = "GoogleAuthentication"
    }


    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.sign_in_button -> signIn()
            R.id.sign_out_button -> signOut()
        }
    }
}