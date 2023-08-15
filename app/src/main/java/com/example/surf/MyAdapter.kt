
package com.example.surf
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter( val userlist: ArrayList<User>): RecyclerView.Adapter<MyAdapter.MyViewHolder>( ){
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.chatitem,parent,false)
        return MyViewHolder(itemView,mListener)
    }
    override fun getItemCount(): Int {
        return userlist.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userlist[position]
        holder.pic.setImageResource(R.drawable.surflogo)
        holder.name.text= currentItem.name.toString()
        holder.mail.text=currentItem.mail.toString()

    }
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val pic: ImageView =itemView.findViewById(R.id.imageView)
        val name: TextView =itemView.findViewById(R.id.nam)
        val mail: TextView =itemView.findViewById(R.id.mai)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
        }
        } }
