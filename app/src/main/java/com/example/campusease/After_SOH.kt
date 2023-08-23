package com.example.campusease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class After_SOH : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_soh)
    }

    fun jobsClick(v: View){
        val intent= Intent(this, JobsList::class.java)
        startActivity(intent)
    }

    fun scholarshipClick(v:View){
        val intent=Intent(this, ScholarshipList::class.java)
        startActivity(intent)
    }

    fun EventClick(v:View){
        val intent=Intent(this, EventList::class.java)
        startActivity(intent)
    }
}