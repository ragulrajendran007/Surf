
package com.example.surf
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapterIssue(val issuelist: ArrayList<issueResponse>): RecyclerView.Adapter<MyAdapterIssue.MyViewHolder>( ){
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.issueitem,parent,false)
        return MyViewHolder(itemView,mListener)
    }
    override fun getItemCount(): Int {
        return issuelist.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=issuelist[position]
        holder.title.text=currentItem.iss.toString()
        holder.pickuplocation.text= currentItem.puloc.toString()
        holder.deliverylocation.text=currentItem.delloc.toString()

    }
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){

        val title: TextView =itemView.findViewById(R.id.tit)
        val pickuplocation: TextView =itemView.findViewById(R.id.pul)
        val deliverylocation: TextView =itemView.findViewById(R.id.dl)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
        }
        } }
