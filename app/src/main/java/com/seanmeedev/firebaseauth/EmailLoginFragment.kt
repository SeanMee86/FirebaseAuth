package com.seanmeedev.firebaseauth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_email_login.*


/**
 * A simple [Fragment] subclass.
 * Use the [emailLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailLoginFragment : Fragment(R.layout.fragment_email_login), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpBtn.setOnClickListener(this)
    }

    override fun onStart(){
        super.onStart()
        val currentUser: FirebaseUser? = mAuth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?){

    }

    private fun signUpUser(){
        Log.d("Click", "signUpUserFunction:clicked")
        val email = emailId.text.toString()
        val password = passwordId.text.toString()
        if(email.isEmpty()){
            emailId.error = "Please enter an email"
            emailId.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailId.error = "Please enter a valid email"
            emailId.requestFocus()
            return
        }
        if(password.isEmpty()){
            passwordId.error = "Please enter a password"
            passwordId.requestFocus()
            return
        }
        createUser(email, password)
    }

    fun createUser(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("Success", "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    Toast.makeText(
                        requireActivity(),
                        "Successfully registered",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Failed", "createUserWithEmail:failure", task.exception);
                    Toast.makeText(
                        requireActivity(),
                        task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show();
                    updateUI(null);
                }
            }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.signUpBtn -> signUpUser()
        }
    }
}
