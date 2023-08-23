package com.example.campusease

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.database.*

class EventList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventsArrayList:ArrayList<Events>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        generateEvents()
    }

    fun generateEvents(){

        recyclerView=findViewById(R.id.event_RecyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this)
        eventsArrayList=arrayListOf()
        databaseReference= FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Events")

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(datasnapshot in snapshot.children){
                        val eventObj=datasnapshot.getValue(Events::class.java)
                        eventsArrayList.add(eventObj!!)
//                        Log.d("event", eventObj.eventDec.toString())
                    }
                    var adapter=EventAdapter(eventsArrayList, this@EventList)
                    recyclerView.adapter=adapter
                    adapter.notifyDataSetChanged()


                    adapter.setOnItemClickListener(object: EventAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            //Toast.makeText(this@MainActivity, "You clicked on: $position", Toast.LENGTH_SHORT).show()
                            val intent= Intent(this@EventList, inside_event::class.java)
                            intent.putExtra("eventTitle",eventsArrayList[position].eventTitle)
                            intent.putExtra("eventDate", eventsArrayList[position].date)
                            intent.putExtra("eventDescription", eventsArrayList[position].eventDec)
                            intent.putExtra("eventLink", eventsArrayList[position].eventLink)
                            intent.putExtra("eventVenue", eventsArrayList[position].venue)
                            intent.putExtra("eventDay", eventsArrayList[position].day)
                            intent.putExtra("eventTime", eventsArrayList[position].time)
                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EventList,  error.toString(),Toast.LENGTH_SHORT).show()
            }

        })




















    }
}