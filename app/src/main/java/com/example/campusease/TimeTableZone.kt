package com.example.campusease

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TimeTableZone : AppCompatActivity() {
    lateinit var recyclerView:RecyclerView
    lateinit var dayList:ArrayList<After_TimeTable.EachDay>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_zone)

        val bundle:Bundle?=intent.extras!!
        dayList=bundle!!.getSerializable("DayList") as ArrayList<After_TimeTable.EachDay>

        //getting recycler view object
        recyclerView=findViewById(R.id.timetableRecyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this@TimeTableZone)
        val adapter=TimetableAdapter(dayList,this@TimeTableZone)
        recyclerView.adapter=adapter

    }
    class TimetableAdapter(private val dayList:ArrayList<After_TimeTable.EachDay>, private val context: Context): RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>(){

        class TimetableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val course_name=itemView.findViewById<TextView>(R.id.Course_NameTV)
            val teacher_name=itemView.findViewById<TextView>(R.id.Teacher_NameTV)
            val time=itemView.findViewById<TextView>(R.id.Course_TimeTV)
            val venue=itemView.findViewById<TextView>(R.id.Course_VenueTV)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.classrow,parent,false)
            return TimetableViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
            holder.course_name.text=dayList[position].course_name
            holder.teacher_name.text=dayList[position].course_teacher
            holder.time.text=dayList[position].time
            holder.venue.text=dayList[position].venue
        }

        override fun getItemCount(): Int {
            return dayList.size
        }
    }
}