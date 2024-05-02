package com.example.surf

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
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
            var stopper=0
            val mailid=email
            var flag=0
            val mailtag= "nitt.edu"
            if ( name.length==0 || email.length==0 || pwd.length==0){
                Toast.makeText(this@Signup,"Fill all the fields , none should be empty",Toast.LENGTH_SHORT).show()
                stopper=1
            }else{
                var j = 0
                for ( i in 0..mailid.length - 1 ){
                    if(mailid[i] == '@') {
                        flag = 1
                        continue
                    }
                    if( flag == 1 && mailid[i] != mailtag[j] ) break
                    if( j < mailtag.length-1 ) j++
                }
                if ( j != mailtag.length ) {
                    stopper = 1
                    Toast.makeText(this@Signup,"Enter valid nitt mail id",Toast.LENGTH_SHORT).show()
                }
            }
            if ( stopper == 0 ) signup(name,email,pwd)
        }
    }
    private fun signup(name: String, email: String, pwd: String) {
        mAuth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this@Signup, "Verification email sent", Toast.LENGTH_SHORT).show()

                        AddUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                        val intent = Intent(this@Signup, Login::class.java)
                        startActivity(intent)
                        finish()
                    }?.addOnFailureListener {
                        Toast.makeText(this@Signup, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Signup, "Unable to Signup: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
private fun AddUserToDatabase(name:String,email:String,uid:String){
    dRef=FirebaseDatabase.getInstance().getReference()
    dRef.child("user").child(uid).setValue(User(name,email,uid))


}

}
