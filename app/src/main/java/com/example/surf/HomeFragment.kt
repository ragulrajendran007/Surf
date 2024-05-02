package com.example.surf

import android.annotation.SuppressLint
import android.app.Dialog
import android.icu.text.Transliterator.Position
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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


class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: MyAdapterIssue
    private lateinit var recyclerView: RecyclerView
    private lateinit var array: ArrayList<chatshow>
    lateinit var imageid: Array<Int>
    lateinit var names: Array<String>
    lateinit var issuelist:ArrayList<issueResponse>

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
            } }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager= LinearLayoutManager(context)
        recyclerView=view.findViewById(R.id.recyclerView)
        issuelist= arrayListOf<issueResponse>()
        recyclerView.layoutManager=layoutManager
        adapter= MyAdapterIssue(issuelist)
        recyclerView.adapter=adapter

        mAuth= FirebaseAuth.getInstance()
        dbref=FirebaseDatabase.getInstance().getReference()

        adapter.setOnItemClickListener(listener = object: MyAdapterIssue.onItemClickListener{
            @SuppressLint("MissingInflatedId")
            override fun onItemClick(position: Int) {
                val dialogBinding1 = layoutInflater.inflate(R.layout.issueview, null)
                val mydialog1 = Dialog(requireActivity())
                mydialog1.setContentView(dialogBinding1)
                mydialog1.setCancelable(true)
                val key=issuelist[position].key
                val issue = dialogBinding1.findViewById<TextView>(R.id.ivi)
                val issuedesc = dialogBinding1.findViewById<TextView>(R.id.ivid)
                val pickup = dialogBinding1.findViewById<TextView>(R.id.ivpul)
                val drop = dialogBinding1.findViewById<TextView>(R.id.ivdl)
                val contact = dialogBinding1.findViewById<TextView>(R.id.ivcon)


                val contactDel = dialogBinding1.findViewById<TextView>(R.id.ivgcon)

                issue.text=issuelist[position].iss
                issuedesc.text=issuelist[position].issdes
                pickup.text=issuelist[position].puloc
                drop.text=issuelist[position].delloc
                contact.text=issuelist[position].contact
                Toast.makeText(activity,issuelist[position].key,Toast.LENGTH_SHORT)
                val acc = dialogBinding1.findViewById<TextView>(R.id.acc)

                acc.setOnClickListener {
                    val currentIssueKey = issuelist[position].key
                    val currentUserId = mAuth.currentUser?.uid

                    val issueRef = dbref.child("issues").child((currentIssueKey.toString()))
                    issueRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val issueData = snapshot.getValue(issueResponse::class.java)
                            if (issueData != null) {
                                if (issueData.taken== 0) {
                                    issueRef.child("taken").setValue(1)
                                    issueRef.child("delContact").setValue(contactDel.text.toString())
                                    issueRef.child("takenBy").setValue(currentUserId)
                                        .addOnSuccessListener {
                                            Toast.makeText(activity, "Issue taken succesfully , check my Issues", Toast.LENGTH_SHORT).show()

                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(activity, "Failed to update TakenBy: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    Toast.makeText(activity, "Issue is already taken", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(activity, "Issue data not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(activity, "Failed to retrieve issue data: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })


                }
                mydialog1.show()
            }
        })
        dbref.child("issues").addValueEventListener(object:ValueEventListener{
            //snapshot-used to get data from db
            override fun onDataChange(snapshot: DataSnapshot) {
                issuelist.clear()
                for (postSnapshot in snapshot.children){
                    val currentissue=postSnapshot.getValue(issueResponse::class.java)
                    if((mAuth.currentUser?.uid != currentissue!!.uid) ){
                        currentissue.key = postSnapshot.key
                        if (mAuth.currentUser?.uid != currentissue.uid && currentissue.taken == 0) {
                            issuelist.add(currentissue)
                        }
                    }}
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"sorry for the unexpected error",Toast.LENGTH_SHORT)
            }
        })
//        val search=view.findViewById<EditText>(R.id.search)
//        val click=view.findViewById<ImageView>(R.id.click)
//        click.setOnClickListener{
//            dbref.child("user").addValueEventListener(object:ValueEventListener{
//                //snapshot-used to get data from db
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    userlist.clear()
//                    for (postSnapshot in snapshot.children){
//                        val currentUser=postSnapshot.getValue(User::class.java)
//                        if(mAuth.currentUser?.uid != currentUser!!.uid){
//                            if(currentUser!!.name==search.text.toString()){
//                                userlist.add(currentUser!!)}
//                            if(search.text.toString()==""){
//                                userlist.add(currentUser!!)
//                            }
//                        }}
//                    adapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(activity,"sorry for the unexpected error",Toast.LENGTH_SHORT)
//                }
//
//
//            })
//            //search.text=null
//        }
    }
  }