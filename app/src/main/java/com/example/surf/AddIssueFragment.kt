package com.example.surf

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var mAuth: FirebaseAuth
private lateinit var dbref: DatabaseReference

class AddIssueFragment : Fragment() {
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
        return inflater.inflate(R.layout.addfeed, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddIssueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance().getReference()
        val create=view.findViewById<TextView>(R.id.sub)
        create.setOnClickListener {
                val iss = view.findViewById<TextView>(R.id.ptit)
                val issdes = view.findViewById<TextView>(R.id.pdes)
                val puloc = view.findViewById<TextView>(R.id.ploc1)
                val delloc= view.findViewById<TextView>(R.id.ploc2)
                val contact= view.findViewById<TextView>(R.id.pnum)

            val issueText = iss.text.toString()
            val issueDescription = issdes.text.toString()
            val pickupLocation = puloc.text.toString()
            val deliveryLocation = delloc.text.toString()
            val contactInfo = contact.text.toString()


            if (issueText.isNotEmpty() && issueDescription.isNotEmpty() && pickupLocation.isNotEmpty() && deliveryLocation.isNotEmpty() && contactInfo.isNotEmpty()) {
                val issueObject = issue(
                    mAuth.currentUser?.uid!!,
                    "Issuer",
                    issueText,
                    issueDescription,
                    pickupLocation,
                    "",
                    deliveryLocation,
                    contactInfo,
                    0,
                    "9999999999",

                )
                dbref.child("issues").push().setValue(issueObject)
                Toast.makeText(
                    requireActivity(),
                    "Issue added do wait for someone to pick it up",
                    Toast.LENGTH_SHORT
                ).show()

            }
             }
             }

    }
