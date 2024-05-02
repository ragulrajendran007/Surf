package com.example.surf

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    @SuppressLint("MissingInflatedId")//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        val lmail: TextView = findViewById(R.id.lmail)
        val lpassword: TextView = findViewById(R.id.lpassword)
        val login: Button = findViewById(R.id.login)
        val signup: Button = findViewById(R.id.signup)

        mAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val currentUser = auth.currentUser
            if (currentUser != null) {
//                finish()
//                startActivity(Intent(this, Main::class.java))

            } else {

            }
        }
        login.setOnClickListener {
            val email = lmail.text.toString()
            val pwd = lpassword.text.toString()
            login(email, pwd)





//            firebaseAuth.addAuthStateListener(authStateListener)






        }
        signup.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            finish()
            startActivity(intent)
        }
        val fpwd: Button = findViewById(R.id.fpwd)
        fpwd.setOnClickListener {

            val i = lmail.text.toString()
            if (i.isEmpty()) {
                Toast.makeText(this@Login, "enter mail", Toast.LENGTH_SHORT).show()
            } else {

                mAuth.sendPasswordResetEmail(i).addOnSuccessListener {
                    Toast.makeText(this@Login, "reset mail sent to $i", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }
    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(authStateListener)
    }

    // Unregister the AuthStateListener in onStop
    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(authStateListener)
    }

    private fun login(email: String, pwd: String) {
        mAuth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (mAuth.currentUser?.isEmailVerified == true) {

                        val intent = Intent(this@Login, Main::class.java)
                        intent.putExtra("mail", "${mAuth.currentUser?.email}")

                        finish()
                        startActivity(intent)
                        Toast.makeText(this@Login, "${mAuth.currentUser!!}", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@Login, "please verify", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login, "Unable to Login", Toast.LENGTH_SHORT).show()
                }
            }
    }
}