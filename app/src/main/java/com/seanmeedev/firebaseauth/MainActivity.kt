package com.seanmeedev.firebaseauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailLoginFragment = EmailLoginFragment()
        val googleLoginFragment = GoogleSignInFragment()

        supportFragmentManager.beginTransaction().apply {
            add(R.id.flLayout, emailLoginFragment)
            commit()
        }

        gso_btn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flLayout, googleLoginFragment)
                commit()
            }
        }

        eso_btn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flLayout, emailLoginFragment)
                commit()
            }
        }
    }
}