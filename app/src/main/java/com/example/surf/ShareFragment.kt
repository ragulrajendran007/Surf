package com.example.surf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShareFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShareFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var array: ArrayList<chatshow>
    lateinit var imageid: Array<Int>
    lateinit var names: Array<String>
    lateinit var userlist:ArrayList<User>


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
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShareFragment.
         */
        // TODO: Rename and change types and number of parameters
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
        datainitialize()
        val layoutManager= LinearLayoutManager(context)
        recyclerView=view.findViewById(R.id.recyclerView)
        userlist= arrayListOf<User>()
        recyclerView.layoutManager=layoutManager
        adapter= MyAdapter(userlist)
        recyclerView.adapter=adapter

        mAuth= FirebaseAuth.getInstance()
        dbref=FirebaseDatabase.getInstance().getReference()

        adapter.setOnItemClickListener(listener = object: MyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val result="${userlist[position].name}"
                val results="${userlist[position].uid}"
                var senuid= FirebaseAuth.getInstance().currentUser?.uid
                setFragmentResult("requestKey", bundleOf("data" to result,"datas" to results,"datez" to senuid))
                getFragmentManager()?.beginTransaction()
                    ?.replace(R.id.fragment_container, chatFragment())?.commit()

            }
        })
        dbref.child("user").addValueEventListener(object:ValueEventListener{
            //snapshot-used to get data from db
            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != currentUser!!.uid){
                    userlist.add(currentUser!!)
                }}
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"sorry for the unexpected error",Toast.LENGTH_SHORT)
            }


        })
        val search=view.findViewById<EditText>(R.id.search)
        val click=view.findViewById<ImageView>(R.id.click)
        click.setOnClickListener{
            dbref.child("user").addValueEventListener(object:ValueEventListener{
                //snapshot-used to get data from db
                override fun onDataChange(snapshot: DataSnapshot) {
                    userlist.clear()
                    for (postSnapshot in snapshot.children){
                        val currentUser=postSnapshot.getValue(User::class.java)
                        if(mAuth.currentUser?.uid != currentUser!!.uid){
                            if(currentUser!!.name==search.text.toString()){
                            userlist.add(currentUser!!)}
                            if(search.text.toString()==""){
                                userlist.add(currentUser!!)
                            }
                        }}
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity,"sorry for the unexpected error",Toast.LENGTH_SHORT)
                }


            })
            //search.text=null
        }

    }
    private fun datainitialize(){
        array= arrayListOf<chatshow>()
        imageid= arrayOf()
        names= arrayOf()
        for (i in imageid.indices){
            val arr = chatshow(imageid[i], names[i])
            array.add(arr)
        }
}}