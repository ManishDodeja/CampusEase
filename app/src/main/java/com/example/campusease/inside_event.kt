package com.example.campusease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class inside_event : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_event)
        getEvents()
    }
    private fun getEvents(){
        //finding widgets
        val eventTitleTB=findViewById<TextView>(R.id.EventTitle_inside)
        val eventDateTB=findViewById<TextView>(R.id.EventDate_inside)
        val eventDayTB=findViewById<TextView>(R.id.EventDay_inside)
        val eventVenueTB= findViewById<TextView>(R.id.EventVenue_inside)
        val eventDescriptionTB=findViewById<TextView>(R.id.EventInfo_inside)
        val eventLinkTB=findViewById<TextView>(R.id.EventLink_inside)
        val eventTimeTB=findViewById<TextView>(R.id.EventTime_inside)

        //getting intents from previous screen
        val bundle:Bundle?=intent.extras!!
        val eventTitleS=bundle!!.getString("eventTitle")
        val eventDateS=bundle!!.getString("eventDate")
        val eventDescriptionS=bundle!!.getString("eventDescription")
        val eventDayS=bundle!!.getString("eventDay")
        val eventTimeS=bundle!!.getString("eventTime")
        val eventVenueS=bundle!!.getString("eventVenue")
        val eventLinkS=bundle!!.getString("eventLink")



        eventTitleTB.text=eventTitleS.toString()
        eventDescriptionTB.text=eventDescriptionS.toString()
        eventDateTB.text=eventDateS.toString()
        eventDayTB.text=eventDayS.toString()
        eventLinkTB.text=eventLinkS.toString()
        eventTimeTB.text=eventTimeS.toString()
        eventVenueTB.text=eventVenueS.toString()

    }

}