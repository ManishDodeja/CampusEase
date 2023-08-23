package com.example.campusease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class InsideJob : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_job)
        getData()

    }
    fun getData(){
        // finding widgets
        val companyImage=findViewById<ImageView>(R.id.company_image)
        val companyTB=findViewById<TextView>(R.id.companyName_Textview)
        val jobTitleTB=findViewById<TextView>(R.id.job_titleTextview)
        val deadlineTB=findViewById<TextView>(R.id.deadline_Textview)
        val departmentTB=findViewById<TextView>(R.id.department_Textview)
        val experienceTB=findViewById<TextView>(R.id.experience_Textview)
        val locationTB=findViewById<TextView>(R.id.location_Textview)
        val salaryRangeTB=findViewById<TextView>(R.id.salary_Textview)
        val qualificationTB=findViewById<TextView>(R.id.qualification_Textview)
        val jobLinkTB=findViewById<TextView>(R.id.linkToApply_Textview)


        val bundle:Bundle?=intent.extras!!
        val company=bundle!!.getString("company")
        val deadline=bundle!!.getString("deadline")
        val department=bundle!!.getString("department")
        val imageurl=bundle!!.getString("imageurl")
        val job_title=bundle!!.getString("job_title")
        val qualification=bundle!!.getString("qualification")
        val salaryRange=bundle!!.getString("salary_range")
        val location=bundle!!.getString("location")
        val experience=bundle!!.getString("experience")
        val jobLink=bundle!!.getString("jobLink")

        companyTB.text=company.toString()
        jobTitleTB.text=job_title.toString()
        deadlineTB.text=deadline.toString()
        departmentTB.text=department.toString()
        qualificationTB.text=qualification.toString()
        salaryRangeTB.text=salaryRange.toString()
        locationTB.text=location.toString()
        experienceTB.text=experience.toString()
        jobTitleTB.text=jobLink.toString()
        Glide.with(this).load(imageurl).into(companyImage)

    }
}