package com.example.surf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(val messagelist: ArrayList<com.example.surf.Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

val ITEM_RECEIVE=1
    val ITEM_SENT=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==1){
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(itemView)
        }
        else{
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage=messagelist[position]
        if(holder.javaClass==SentViewHolder::class.java){

            val viewHolder=holder as SentViewHolder
            holder.sentmessage.text=currentMessage.message
        }
        else{
            val viewHolder=holder as ReceiveViewHolder
            holder.receivedmessage.text=currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentmessage=messagelist[position]
        if(FirebaseAuth.getInstance().currentUser!!.uid.equals(currentmessage.senderid)){
            return ITEM_SENT}
        else{
            return ITEM_RECEIVE

        }
    }
    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentmessage=itemView.findViewById<TextView>(R.id.sentmessage)
    }
    class ReceiveViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val receivedmessage=itemView.findViewById<TextView>(R.id.receivemessage)

    }
}