package com.example.surf

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()
        val sname: TextView =findViewById(R.id.sname)
        val smail: TextView =findViewById(R.id.smail)
        val spassword: TextView =findViewById(R.id.spassword)

        val signup: Button =findViewById(R.id.signup)
        signup.setOnClickListener {
            val name=sname.text.toString()
            val email = smail.text.toString()
            val pwd=spassword.text.toString()
            signup(name,email,pwd)
        }
    }
    private fun signup(name:String,email:String,pwd:String){
        mAuth.createUserWithEmailAndPassword(email,pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                  mAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                      Toast.makeText(this@Signup,"Email sent",Toast.LENGTH_SHORT).show()
                      AddUserToDatabase(name,email, mAuth.currentUser?.uid!!)
                      val intent =Intent(this@Signup,Login::class.java)
                      finish()
                      startActivity(intent)
                  }

                      ?.addOnFailureListener{
                          Toast.makeText(this@Signup,"fail",Toast.LENGTH_SHORT).show()
                      }

                } else {
                   Toast.makeText(this@Signup,"Unable to Signup",Toast.LENGTH_SHORT).show()
                }
            }

    }
private fun AddUserToDatabase(name:String,email:String,uid:String){
    dRef=FirebaseDatabase.getInstance().getReference()
    dRef.child("user").child(uid).setValue(User(name,email,uid))


}

}
