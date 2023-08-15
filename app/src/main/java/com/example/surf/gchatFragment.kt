package com.example.surf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.setFragmentResultListener
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
 * Use the [gchatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class gchatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var gchatrecyclerview: RecyclerView
    private lateinit var messagebox: EditText
    private lateinit var sendbutton: ImageView
    //private lateinit var result:String
    private lateinit var messageAdapter: gMessageAdapter
    private lateinit var messageList: ArrayList<Message>
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
        return inflater.inflate(R.layout.fragment_gchat, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment gchatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            gchatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gchatrecyclerview=view.findViewById(R.id.gchatrecyclerview)
        messagebox=view.findViewById(R.id.messagebox)
        sendbutton=view.findViewById(R.id.sendmsg)
        var result:String=""
        var gcode:String=""
        var secuid:String=""
        dbref= FirebaseDatabase.getInstance().getReference()
        setFragmentResultListener("requestKey"){key,bundle->
            result= bundle.getString("data").toString()
            gcode= bundle.getString("datas").toString()
            secuid=bundle.getString("datez").toString()

            messageList= ArrayList()
            messageAdapter=gMessageAdapter(messageList)
            gchatrecyclerview.layoutManager= LinearLayoutManager(context)
            gchatrecyclerview.adapter=messageAdapter
            val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
            toolbar.title=gcode
            dbref.child("gchats").child(gcode).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (postSnapshot in snapshot.children) {
                            val message = postSnapshot.getValue(Message::class.java)
                            messageList.add(message!!)
                        }
                        messageAdapter.notifyDataSetChanged()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            sendbutton.setOnClickListener{
                messageAdapter=gMessageAdapter(messageList)
                gchatrecyclerview.layoutManager= LinearLayoutManager(context)
                gchatrecyclerview.adapter=messageAdapter
                val message=messagebox.text.toString()?:""
                mAuth= FirebaseAuth.getInstance()
                val messageObject=Message(message, mAuth.currentUser?.uid)
                messageList.add(messageObject)
                messageAdapter.notifyDataSetChanged()
                dbref.child("gchats").child(gcode).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                    }
                messagebox.text=null
                // getFragmentManager()?.beginTransaction()
                // ?.replace(R.id.fragment_container, ShareFragment())?.commit()
            }}

    }





}