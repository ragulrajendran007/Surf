package com.example.surf

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class gfeedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var gchatrecyclerview: RecyclerView
    private lateinit var messagebox: EditText
    private lateinit var sendbutton: ImageView
    private lateinit var gcode:String
    private lateinit var desc:String
    private lateinit var messageAdapter: gMessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        val rootView = inflater.inflate(R.layout.fragment_gfeed, container, false)
//
//        storageReference = FirebaseStorage.getInstance().reference
//        var result:String=""
//
//        var secuid:String=""
//        dbref = FirebaseDatabase.getInstance().getReference()
//        setFragmentResultListener("requestKey") { key, bundle ->
//            result = bundle.getString("data").toString()
//            gcode = bundle.getString("datas").toString()
//            secuid = bundle.getString("datez").toString()
//           // val toolbar = rootView.findViewById<Toolbar>(R.id.toolbar)
////            toolbar.title=gcode
//            val addpost=rootView.findViewById<ImageView>(R.id.addpost)
//            addpost.setOnClickListener {
//                val dialogBinding1 = layoutInflater.inflate(R.layout.addfeed, null)
//                val mydialog1 = Dialog(requireActivity())
//                mydialog1.setContentView(dialogBinding1)
//                mydialog1.setCancelable(true)
//                val sub = dialogBinding1.findViewById<TextView>(R.id.sub)
//                val pdesc = dialogBinding1.findViewById<TextView>(R.id.pdes)
//                val pic=dialogBinding1.findViewById<ImageView>(R.id.addpic)
//                pic.setOnClickListener {
//                    if (pdesc.text != "") {
//                        desc=pdesc.text.toString()
//                    openImagePicker()
//                        }}
//                sub.setOnClickListener {
//
//
//                        mydialog1.dismiss()
//                    }
//
//                mydialog1.show()
//            }
//        }
//        // Open image picker when a button is clicked
//
//
//
//
        return rootView
    }





private fun openImagePicker() {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
        val selectedImageUri: Uri = data.data!!
        val storageRef = storageReference.child("images/${selectedImageUri.lastPathSegment}")

        storageRef.putFile(selectedImageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL asynchronously
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
mAuth= FirebaseAuth.getInstance()
                    dbref.child("gchats").child(gcode).child("posts").push()
                        .setValue(Post(desc, mAuth.currentUser?.uid, imageUrl))
                        .addOnSuccessListener {
                            // Success
                        }
                        .addOnFailureListener { exception ->
                            // Handle error
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }
}
}