package com.example.surf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class grpAdapter( val grouplist: ArrayList<grp>): RecyclerView.Adapter<grpAdapter.MyViewHolder>( ){
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.gchatitem,parent,false)
        return MyViewHolder(itemView,mListener)
    }
    override fun getItemCount(): Int {
        return grouplist.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=grouplist[position]
        holder.pic.setImageResource(R.drawable.screenshot_2023_08_11_at_11_16_09_am)
        holder.name.text= currentItem.gname.toString()
        holder.code.text=currentItem.gcode.toString()

    }
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val pic: ImageView =itemView.findViewById(R.id.imageView)
        val name: TextView =itemView.findViewById(R.id.nam)
        val code: TextView =itemView.findViewById(R.id.mai)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
        }
    } }