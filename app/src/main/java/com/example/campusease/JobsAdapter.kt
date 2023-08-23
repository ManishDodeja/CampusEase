package com.example.campusease

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class JobsAdapter(private val jobList:ArrayList<Jobs>, private val context: Context): RecyclerView.Adapter<JobsAdapter.JobsViewHolder>(){
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener=listener
    }

    class JobsViewHolder(itemView: View, listener:onItemClickListener): RecyclerView.ViewHolder(itemView){
        val company_name=itemView.findViewById<TextView>(R.id.title)
        val job_Title=itemView.findViewById<TextView>(R.id.JobTitle)
        val job_image=itemView.findViewById<ImageView>(R.id.jobImage)

        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.jobrow,parent,false)
        return JobsViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.company_name.text=jobList[position].company
        holder.job_Title.text=jobList[position].job_title
        Glide.with(context).load(jobList[position].imageurl).into(holder.job_image)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }
}
