package com.example.campusease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class InsideScholarship : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_scholarship)
        getData()
    }
    private fun getData(){
        // finding widgets
        val scholarshipNameTB=findViewById<TextView>(R.id.ScholarshipName_InsideTextView)
        val scholarshipEligibilityTB=findViewById<TextView>(R.id.ScholarshipEligiblity_InsideTextView)
        val scholarshipDescriptionTB=findViewById<TextView>(R.id.ScholarshipDescription_InsideTextView)
        val scholarshipDeadlineTB=findViewById<TextView>(R.id.ScholarshipDeadline_InsideTextView)
        val scholarshipLinkTB=findViewById<TextView>(R.id.ScholarshipLink_InsideTextView)




        val bundle:Bundle?=intent.extras!!
        val nameS=bundle!!.getString("name")
        val deadlineS=bundle!!.getString("deadline")
        val descriptionS=bundle!!.getString("description")
        val linkS=bundle!!.getString("link")
        val eligibilityS=bundle!!.getString("eligibility")



        scholarshipNameTB.text=nameS.toString()
        scholarshipDeadlineTB.text=deadlineS.toString()
        scholarshipDescriptionTB.text=descriptionS.toString()
        scholarshipLinkTB.text=linkS.toString()
        scholarshipEligibilityTB.text=eligibilityS.toString()

    }
}