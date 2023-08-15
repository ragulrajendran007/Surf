package com.example.surf

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.awaitAll

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/*private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"*/

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    /*private var param1: String? = null
    private var param2: String? = null*/
    private lateinit var adapter: grpAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var array: ArrayList<gchatshow>
    lateinit var imageid: Array<Int>
    lateinit var names: Array<String>
    lateinit var code: Array<String>
    lateinit var grplist:ArrayList<grp>
    lateinit var gcodez: MutableList<String>
    lateinit var gnamez: MutableList<String>
    lateinit var cgrplist:ArrayList<grp>


    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

 /*   companion object {
        *//**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutFragment.
         *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutFragment().apply {
                *//*arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }*//*
            }
    }*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    datainitialize()
    val layoutManager= LinearLayoutManager(context)
    recyclerView=view.findViewById(R.id.recyclerView)

    cgrplist= arrayListOf<grp>()
    recyclerView.layoutManager=layoutManager
    adapter= grpAdapter(grplist)
    recyclerView.adapter=adapter
    gcodez= mutableListOf()
        gnamez= mutableListOf()
    mAuth= FirebaseAuth.getInstance()
    dbref= FirebaseDatabase.getInstance().getReference()
    adapter.setOnItemClickListener(listener = object: grpAdapter.onItemClickListener{
        override fun onItemClick(position: Int) {
            val result="${grplist[position].gname}"
            val results="${grplist[position].gcode}"
            var senuid= FirebaseAuth.getInstance().currentUser?.uid
            setFragmentResult("requestKey", bundleOf("data" to result,"datas" to results,"datez" to senuid))
            getFragmentManager()?.beginTransaction()
                ?.replace(R.id.fragment_container, gchatFragment())?.commit()
        }
    })
        fun get(){
            Log.d("user","${mAuth.currentUser?.uid!!}")
            dbref.child("usergroup").child(mAuth.currentUser?.uid!!).addValueEventListener(object: ValueEventListener {
                //snapshot-used to get data from db
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("user","${mAuth.currentUser?.uid!!}")
                    gcodez.clear()
                    for (postSnapshot in snapshot.children){
                        val currentgrp= postSnapshot.getValue(Grppuser::class.java)!!.gcode
                        Log.d("user","${currentgrp}")
                        gcodez.add(currentgrp!!)
                        grplist.add(grp("Group Name", currentgrp.toInt()))
                    }
                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        get()
}
    private fun datainitialize(){
        grplist= arrayListOf<grp>()
        gcodez= mutableListOf()
        gnamez= mutableListOf()
        for (i in gcodez.indices){
            grplist.add(grp("Group Name", gcodez[i].toInt()))

        }
    }
}