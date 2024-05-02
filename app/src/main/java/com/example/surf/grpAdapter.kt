package com.example.surf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
        var bgArray= arrayListOf<Int>(R.drawable.moneybg,R.drawable.chattybg,R.drawable.prodbg)
        holder.name.text= currentItem.gname.toString()
        holder.code.text=currentItem.gcode.toString()
        holder.bg.setBackgroundResource(bgArray[position%3])

    }
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val bg:ConstraintLayout=itemView.findViewById(R.id.bg)
        val name: TextView =itemView.findViewById(R.id.tit)
        val code: TextView =itemView.findViewById(R.id.mai)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
        }
    } }