package com.example.campusease

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ScholarshipAdapter(private val scholarshipList:ArrayList<Scholarships>, private val context: Context) : RecyclerView.Adapter<ScholarshipAdapter.ScholarshipsViewHolder>(){
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener=listener
    }

    class ScholarshipsViewHolder(itemView: View, listener:onItemClickListener): RecyclerView.ViewHolder(itemView){
        val scholarship_name=itemView.findViewById<TextView>(R.id.scholarshipName_textView)
        val scholarship_eligibility=itemView.findViewById<TextView>(R.id.scholarshipEligibility_textView)
        val scholarship_deadline=itemView.findViewById<TextView>(R.id.scholarshipDeadline_textView)
        val scholarship_image=itemView.findViewById<ImageView>(R.id.scholarshipImage)
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScholarshipsViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.scholarshiprow,parent,false)
        return ScholarshipsViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ScholarshipsViewHolder, position: Int) {
        holder.scholarship_name.text=scholarshipList[position].name
        holder.scholarship_eligibility.text=scholarshipList[position].eligibility
        holder.scholarship_deadline.text=scholarshipList[position].deadline
        Log.d("image link", scholarshipList[position].imageurl.toString())
        Glide.with(context).load(scholarshipList[position].imageurl).into(holder.scholarship_image)


    }

    override fun getItemCount(): Int {
        return scholarshipList.size
    }
}