package com.example.campusease

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.google.firebase.database.*

class After_TimeTable : AppCompatActivity() {
    //variables declartion
    private lateinit var dbRef: DatabaseReference
    private lateinit var timetableArrayList:ArrayList<Timetable>
    private lateinit var courseArrayList:ArrayList<String>
    private lateinit var DayList: ArrayList<EachDay>

    //days declartion
    private lateinit var monday:ImageButton
    private lateinit var tuesday: ImageButton
    private lateinit var wednesday:ImageButton
    private lateinit var thursday:ImageButton
    private lateinit var friday:ImageButton
    private lateinit var saturday:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_time_table)



        monday=findViewById(R.id.day_monday)
        tuesday=findViewById(R.id.day_tuesday)
        wednesday=findViewById(R.id.day_wednesday)
        thursday=findViewById(R.id.day_thursday)
        friday=findViewById(R.id.day_friday)
        saturday=findViewById(R.id.day_saturday)

        DayList = arrayListOf()
        // timetable array list initilization
        timetableArrayList= arrayListOf()
        // course Array List initilization
        courseArrayList= arrayListOf()
        dbRef = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()

        //timetable functionality
        getTimeTable()

    }

    fun getTimeTable()
    {

        //connectivity with firebase main root
        //get the cms from previous activity and get the course for that cms and get that records only
        val bundle:Bundle?=intent.extras!!
        val cmsValue=bundle!!.getString("cms")
        // root have child Students
        dbRef.child("Students").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                // check for student with id CMS (which is login currently)
                if(snapshot.hasChild(cmsValue.toString())) {
                    //creating student model class object and get the data of particular student
                    val student = snapshot.child(cmsValue!!).getValue(studentModel::class.java)
                    // take each course from student.courses and add it to courseArrayList
                    for (course in student!!.courses!!){
                        Toast.makeText(this@After_TimeTable, course.toString(),Toast.LENGTH_SHORT)
                        courseArrayList.add(course)
                    }
                }
            }//Datachange finished

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@After_TimeTable, "Error in adding courses to course array list", Toast.LENGTH_SHORT).show()
            }
        })

        //here we will access timetable and
        dbRef.child("Timetable").addValueEventListener(object :ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                if(snapshot.exists()) {
                    for(course in courseArrayList){
                        if(snapshot.hasChild(course.toString())){
                            val timetable=snapshot.child(course.toString()).getValue(Timetable::class.java)
                            timetableArrayList.add(timetable!!)
                        }
                    }
                    clickDays(timetableArrayList)
                }
            }//Datachange finished
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@After_TimeTable, "error in getting time table", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun clickDays(timetableArrayList:ArrayList<Timetable>){

        monday.setOnClickListener{
            for(tt in timetableArrayList){
                DayClick(tt, "Monday")
            }
            val intent=Intent(this@After_TimeTable, TimeTableZone::class.java)
            intent.putExtra("DayList", DayList)
            startActivity(intent)

        }
        tuesday.setOnClickListener{
            for(tt in timetableArrayList){
                DayClick(tt, "Tuesday")
            }
            val intent=Intent(this@After_TimeTable, TimeTableZone::class.java)
            intent.putExtra("DayList", DayList)
            startActivity(intent)
        }
        wednesday.setOnClickListener{
            for(tt in timetableArrayList){
                DayClick(tt, "Wednesday")
            }
            val intent=Intent(this@After_TimeTable, TimeTableZone::class.java)
            intent.putExtra("DayList", DayList)
            startActivity(intent)
        }
        thursday.setOnClickListener{
            for(tt in timetableArrayList){
                DayClick(tt, "Thursday")
            }
            val intent=Intent(this@After_TimeTable, TimeTableZone::class.java)
            intent.putExtra("DayList", DayList)
            startActivity(intent)
        }
        friday.setOnClickListener{
            for(tt in timetableArrayList){
                DayClick(tt, "Friday")
            }
            val intent=Intent(this@After_TimeTable, TimeTableZone::class.java)
            intent.putExtra("DayList", DayList)
            startActivity(intent)
        }
        saturday.setOnClickListener{
            for(tt in timetableArrayList){
                DayClick(tt, "Saturday")
            }
            val intent=Intent(this@After_TimeTable, TimeTableZone::class.java)
            intent.putExtra("DayList", DayList)
            startActivity(intent)
        }
        DayList.clear()
    }
    fun DayClick(tt:Timetable,day:String){

        if(tt.DayOne!!.day.toString() == day){
            val data= EachDay(
                tt.course_name.toString(),
                tt.course_teacher.toString(),
                tt.DayOne.day.toString(),
                tt.DayOne.time.toString(),
                tt.DayOne.venue.toString()
            )
            DayList.add(data)
        }
        if(tt.DayTwo!!.day.toString() == day){
            val data= EachDay(
                tt.course_name.toString(),
                tt.course_teacher.toString(),
                tt.DayTwo.day.toString(),
                tt.DayTwo.time.toString(),
                tt.DayTwo.venue.toString()
            )
            DayList.add(data)
        }
    }

    override fun onResume() {
        DayList.clear()
        super.onResume()
    }
    data class EachDay(val course_name:String, val course_teacher:String,val day:String, val time:String, val venue:String):java.io.Serializable

}