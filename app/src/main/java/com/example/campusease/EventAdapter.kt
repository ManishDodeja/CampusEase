package com.example.campusease

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private val eventList:ArrayList<Events>,private val context: Context) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener=listener
    }

    class EventViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val event_name=itemView.findViewById<TextView>(R.id.EventName_list)
        val event_date=itemView.findViewById<TextView>(R.id.EventDate_list)

        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.eventrow,parent,false)
        return EventViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.event_name.text=eventList[position].eventTitle
        holder.event_date.text=eventList[position].date
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}