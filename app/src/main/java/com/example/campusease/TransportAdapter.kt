package com.example.campusease

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TransportAdapter(private val transportArrayList:ArrayList<TransportDataClass>,private val listOfImages:ArrayList<Int>, private val context: Context):RecyclerView.Adapter<TransportAdapter.TransportViewHolder>() {
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener=listener
    }

    class TransportViewHolder(itemView: View, listener:onItemClickListener): RecyclerView.ViewHolder(itemView){
//        val company_name=itemView.findViewById<TextView>(R.id.title)
//        val job_Title=itemView.findViewById<TextView>(R.id.JobTitle)
//        val job_image=itemView.findViewById<ImageView>(R.id.jobImage)

          val areaName=itemView.findViewById<TextView>(R.id.AreaText)
          val areaImage=itemView.findViewById<ImageView>(R.id.AreaImage)
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransportViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.transport_row,parent,false)
        return TransportViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: TransportViewHolder, position: Int) {
//        holder.company_name.text=jobList[position].company
//        holder.job_Title.text=jobList[position].job_title
//        Glide.with(context).load(jobList[position].imageurl).into(holder.job_image)

        holder.areaName.text=transportArrayList[position].Area
        Glide.with(context).load(listOfImages[position]).into(holder.areaImage)
    }

    override fun getItemCount(): Int {
        return transportArrayList.size
    }

}