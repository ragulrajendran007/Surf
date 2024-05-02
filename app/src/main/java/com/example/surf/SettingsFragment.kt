package com.example.surf

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var mAuth: FirebaseAuth
private lateinit var dbref: DatabaseReference

class SettingsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth= FirebaseAuth.getInstance()
        dbref= FirebaseDatabase.getInstance().getReference()
        val create=view.findViewById<TextView>(R.id.create)
        val join=view.findViewById<TextView>(R.id.join)
        create.setOnClickListener {

                val dialogBinding1 = layoutInflater.inflate(R.layout.newgrp, null)
                val mydialog1 = Dialog(requireActivity())
                mydialog1.setContentView(dialogBinding1)
                mydialog1.setCancelable(true)
            val gname = dialogBinding1.findViewById<TextView>(R.id.gname)
            //val gcode = dialogBinding1.findViewById<TextView>(R.id.gcode)
            val sub = dialogBinding1.findViewById<TextView>(R.id.sub)
            val people_object=grpuser(mAuth.currentUser?.uid!!,"Admin")
             sub.setOnClickListener {
                 if (gname.text != "") {
                     val groups= (100000..11234567).random().toInt()
                     Toast.makeText(requireActivity(),"${groups} is your group code",Toast.LENGTH_SHORT).show()
                     dbref.child("groupszzzzz").child(groups.toString())
                         .child("participants").push()
                         .setValue(people_object)
                     dbref.child("grpppppp").push()
                         .setValue(grp(gname.text.toString(), groups))
                     dbref.child("usergroup").child(mAuth.currentUser!!.uid).push()
                         .setValue(grpuser(groups.toString(),"Admin"))
                     mydialog1.dismiss()
                 }
             }
            mydialog1.show() }
            join.setOnClickListener{
                val dialogBinding1 = layoutInflater.inflate(R.layout.joingroup, null)
                val mydialog1 = Dialog(requireActivity())
                mydialog1.setContentView(dialogBinding1)
                mydialog1.setCancelable(true)
                val gcode = dialogBinding1.findViewById<TextView>(R.id.gname)
                //val gcode = dialogBinding1.findViewById<TextView>(R.id.gcode)
                val sub = dialogBinding1.findViewById<TextView>(R.id.sub)
                val people_object=grpuser(mAuth.currentUser?.uid!!,"User")
                sub.setOnClickListener {
                    if (gcode.text != "") {
                        dbref.child("groupszzzzz").child(gcode.text.toString())
                            .child("participants").push()
                            .setValue(people_object)
                        dbref.child("usergroup").child(mAuth.currentUser!!.uid).push()
                            .setValue(usergroup(gcode.text.toString(),"User"))
                        mydialog1.dismiss()
                    }
                }
                mydialog1.show()
            }
    }
    }
