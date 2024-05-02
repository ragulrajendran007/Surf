package com.example.surf

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyIssuesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: MyAdapterIssue
    private lateinit var recyclerView: RecyclerView
    private lateinit var array: ArrayList<chatshow>
    lateinit var imageid: Array<Int>
    lateinit var names: Array<String>
    lateinit var issuelist: ArrayList<issueResponse>


    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_issue, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShareFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun fetchUserProfile(uid: String, callback: (name: String?, email: String?) -> Unit) {
        val userRef = FirebaseDatabase.getInstance().getReference("user").child(uid)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    // Invoke the callback with retrieved name and email
                    callback(user.name.toString(), user.mail.toString())
                } else {
                    // Invoke the callback with null values if user data is not found
                    callback(null, null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
                println("Failed to retrieve user data: ${error.message}")
                // Invoke the callback with null values if an error occurs
                callback(null, null)
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        issuelist = arrayListOf<issueResponse>()
        recyclerView.layoutManager = layoutManager
        adapter = MyAdapterIssue(issuelist)
        recyclerView.adapter = adapter

        mAuth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance().getReference()

        adapter.setOnItemClickListener(listener = object : MyAdapterIssue.onItemClickListener {
            override fun onItemClick(position: Int) {
                val dialogBinding1 = layoutInflater.inflate(R.layout.acceptedissue, null)
                val mydialog1 = Dialog(requireActivity())
                mydialog1.setContentView(dialogBinding1)
                mydialog1.setCancelable(true)

                val key=issuelist[position].key
                val issue = dialogBinding1.findViewById<TextView>(R.id.ivi)
                val issuedesc = dialogBinding1.findViewById<TextView>(R.id.ivid)
                val pickup = dialogBinding1.findViewById<TextView>(R.id.ivpul)
                val drop = dialogBinding1.findViewById<TextView>(R.id.ivdl)
                val contact = dialogBinding1.findViewById<TextView>(R.id.ivcon)
                val status=dialogBinding1.findViewById<TextView>(R.id.issStatus)
                val uname=dialogBinding1.findViewById<TextView>(R.id.ivname)
                val umail=dialogBinding1.findViewById<TextView>(R.id.ivmail)


                issue.text=issuelist[position].iss
                issuedesc.text=issuelist[position].issdes
                pickup.text=issuelist[position].puloc
                drop.text=issuelist[position].delloc
                contact.text="Not accepted yet"
                if(issuelist[position].taken==1){
                    contact.text=issuelist[position].delContact
                }
                if(issuelist[position].uid == mAuth.currentUser?.uid){

                    status.text="Receiving"
                    fetchUserProfile(issuelist[position].takenBy.toString()) { name, email ->
                        uname.text=name
                        umail.text=email
                    }


                }else{
                    status.text="Delivering"
                    fetchUserProfile(issuelist[position].uid.toString()) { name, email ->
                        uname.text=name
                        umail.text=email
                    }
                }


                Toast.makeText(activity,issuelist[position].key,Toast.LENGTH_SHORT)




                val acc = dialogBinding1.findViewById<TextView>(R.id.ivlocation)

                acc.setOnClickListener {
                    val startLocation = pickup.text.toString().trim()
                    val endLocation = drop.text.toString().trim()

                    val uri = "https://www.google.com/maps/dir/?api=1&origin=$startLocation&destination=$endLocation"

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    intent.setPackage("com.google.android.apps.maps")
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    } else {
                        // If Google Maps app is not available, open in browser
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(browserIntent)
                    }
                }

                mydialog1.show()
            }
        })
        dbref.child("issues").addValueEventListener(object : ValueEventListener {
            //snapshot-used to get data from db
            override fun onDataChange(snapshot: DataSnapshot) {
                issuelist.clear()
                for (postSnapshot in snapshot.children) {
                    val currentissue = postSnapshot.getValue(issueResponse::class.java)
                    currentissue?.key = postSnapshot.key
                    if (mAuth.currentUser?.uid == currentissue!!.uid || mAuth.currentUser?.uid == currentissue.takenBy) {
                        issuelist.add(currentissue!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "sorry for the unexpected error", Toast.LENGTH_SHORT)
            }


        })
//        val search = view.findViewById<EditText>(R.id.search)
//        val click = view.findViewById<ImageView>(R.id.click)
//        click.setOnClickListener {
//            dbref.child("user").addValueEventListener(object : ValueEventListener {
//                //snapshot-used to get data from db
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    userlist.clear()
//                    for (postSnapshot in snapshot.children) {
//                        val currentUser = postSnapshot.getValue(User::class.java)
//                        if (mAuth.currentUser?.uid != currentUser!!.uid) {
//                            if (currentUser!!.name == search.text.toString()) {
//                                userlist.add(currentUser!!)
//                            }
//                            if (search.text.toString() == "") {
//                                userlist.add(currentUser!!)
//                            }
//                        }
//                    }
//                    adapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(activity, "sorry for the unexpected error", Toast.LENGTH_SHORT)
//                }
//
//
//            })
//            //search.text=null
//        }

    }
}