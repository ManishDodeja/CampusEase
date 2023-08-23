package com.example.campusease

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class unEnrollActivity : AppCompatActivity() {

    private lateinit var coursesUnenrollRV: RecyclerView
    private lateinit var searchView:SearchView
    private lateinit var listOfCourses: ArrayList<courseClass>
    private lateinit var adapter2: CourseUnenrollAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_un_enroll)
        coursesUnenrollRV=findViewById(R.id.coursesUnenrollRecyclerView)
        searchView=findViewById(R.id.search_view)
        searchView.clearFocus()
        listOfCourses= arrayListOf()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
        updateRecyclerView()
    }

    private fun filterList(text: String?) {
        val listOfItem= arrayListOf<courseClass>()
        for (item in listOfCourses){
            if(item.courseName?.toLowerCase()!!.contains(text!!.toLowerCase())){
                listOfItem.add(item)
            }
        }
        if(listOfItem.isEmpty()){
            Toast.makeText(this@unEnrollActivity, "No Data Found", Toast.LENGTH_SHORT).show()
        }else{
            adapter2.setFilteredList(listOfItem)
        }
    }

//   private fun fetchDataFromFirebase() {
//        coroutineScope.launch {
//            try {
//                val courses = fetchStudentCourses()
//
//                // Fetch the timetable data for the courses
//                val timetableData = fetchTimetableData(courses)
//
//                // Update the RecyclerView with the data
//                //updateRecyclerView(timetableData)
//            } catch (e: Exception) {
//                Toast.makeText(this@unEnrollActivity, "${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    private suspend fun fetchStudentCourses(): ArrayList<String> = withContext(Dispatchers.IO) {
//        studentCMS = intent.getStringExtra("cms").toString()
//        val coursesArrayList = ArrayList<String>()
//        dbRef.child("Students").child(studentCMS).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Toast.makeText(this@unEnrollActivity, "inside students ", Toast.LENGTH_SHORT).show()
//                // Check if the student exists in the database
//                if (snapshot.exists()) {
//                    val student = snapshot.getValue(studentModel::class.java)
//                    // Add each course from the student's courses to the coursesArrayList
//                    student?.courses?.let {
//                        coursesArrayList.addAll(it)
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@unEnrollActivity, "Error in adding courses to course array list", Toast.LENGTH_SHORT).show()
//            }
//        })
//        coursesArrayList
//    }

//    private suspend fun fetchTimetableData(courses: ArrayList<String>): List<courseClass> = withContext(Dispatchers.IO) {
//        val deferredList = courses.map { course ->
//            async {
//                Toast.makeText(this@unEnrollActivity,"inside timetable", Toast.LENGTH_SHORT).show()
//                val timetableRef = FirebaseDatabase.getInstance().getReference("timetable")
//                val courseTimetableSnapshot = timetableRef.child(course).get().await()
//                val timetableData = courseTimetableSnapshot.getValue(Timetable::class.java)
//                timetableData
//            }
//        }
//        var i=0;
//        val resultList = deferredList.awaitAll()
//        val timetableDataList = ArrayList<courseClass>(resultList.size)
//        resultList.forEach { result ->
//            if (result is Timetable) {
//                val courseCode=courses.get(i)
//                val obj=courseClass(courseCode, result.course_name)
//                timetableDataList.add(obj)
//                i++;
//            }
//        }
//        updateRecyclerView(timetableDataList)
//        timetableDataList
//    }

    private fun updateRecyclerView() {
        Log.d("Inside method", "Okay")
        listOfCourses = intent.getSerializableExtra("ListOfCourses") as ArrayList<courseClass>
        val studentCMS=intent.getStringExtra("cms")

        try{
            Log.d("Calling adapter", "Okay")
            adapter2=CourseUnenrollAdapter(listOfCourses,  this@unEnrollActivity)
            Log.d("Adapter Called", "Okay")
            coursesUnenrollRV.layoutManager=LinearLayoutManager(this@unEnrollActivity)
            coursesUnenrollRV.adapter=adapter2
            adapter2.setOnItemClickListener(object:CourseUnenrollAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val builder = AlertDialog.Builder(this@unEnrollActivity)

                    builder.setTitle("Unenroll Course")

                    builder.setMessage("Are You sure!! \n You want to Unenroll?")

                    builder.setPositiveButton("Unenroll") { dialog, which ->
                        val courseCode = listOfCourses[position].courseCode!!.toString()
                        // Remove course from the student's Firebase Realtime Database
                        val courseRef = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students").child(studentCMS!!).child("courses")
                        courseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val coursesList = dataSnapshot.getValue<MutableList<String>>()
                                val index = coursesList?.indexOf(courseCode) ?: -1
                                if (index >= 0) {
                                    // Remove the value at the found index
                                    val coursesListUpdate = shiftIndextoLast(index, coursesList!!)
                                    courseRef.setValue(coursesListUpdate)
                                        .addOnSuccessListener {
                                            Toast.makeText(this@unEnrollActivity, "Your Course have been removed Successfully", Toast.LENGTH_SHORT)
                                                .show()
                                            val intent = Intent(this@unEnrollActivity, First_Screen::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { exception ->
                                            Toast.makeText(this@unEnrollActivity, "Error", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                } else {
                                    Toast.makeText(this@unEnrollActivity, "Value not found", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@unEnrollActivity, "${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    builder.setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()

                }
            })
            adapter2.notifyDataSetChanged()
        }
        catch(ex:Exception){
            Log.d("exception", ex.message.toString())
        }
    }
    fun shiftIndextoLast(index: Int, courses: MutableList<String>): MutableList<String> {
        courses.removeAt(index) // remove course at given index
        for (i in 0 until courses.size) {
            courses[i] = "${courses[i]}" // update index of each course
            Log.d(i.toString(), courses[i])
        }
        return courses
    }

























//    private fun getStudentData(){
//        studentCMS= intent.getStringExtra("cms").toString()
//        val coursesArrayList= mutableListOf<String>()
//        dbRef.child("Students").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot)
//            {
//                Toast.makeText(this@unEnrollActivity,"inside onDataChange for student",Toast.LENGTH_SHORT).show()
//                // check for student with id CMS (which is login currently)
//                if(snapshot.hasChild(studentCMS)) {
//                    //creating student model class object and get the data of particular student
//                    val student = snapshot.child(studentCMS!!).getValue(studentModel::class.java)
//                    // take each course from student.courses and add it to courseArrayList
//                    for (course in student!!.courses!!){
//                        coursesArrayList.add(course)
//                    }
//                }
//                getTimetableData(coursesArrayList)
//            }//Datachange finished
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@unEnrollActivity, "Error in adding courses to course array list", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

//    private fun getTimetableData(coursesArrayList:MutableList<String>): MutableList<courseClass>{
//        // getting course data from timetable
//        //here we will access timetable and
//        val unEnrollCoursesarrayList= mutableListOf <courseClass>()
//        dbRef.child("Timetable").addValueEventListener(object :ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot)
//            {
//                if(snapshot.exists()) {
//                    for(course in coursesArrayList){
//                        if(snapshot.hasChild(course)){
//                            val courseData=snapshot.child(course).getValue<Timetable>()
//                            val courseClassObj=courseClass(
//                                course,
//                                courseData!!.course_name
//                            )
//                            unEnrollCoursesarrayList.add(courseClassObj!!)
//                        }
//                    }
//                    //coursesUnenrollRV.adapter?.notifyDataSetChanged()
//                }
//                updateRecyclerView(unEnrollCoursesarrayList)
//            }//Datachange finished
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@unEnrollActivity, "error in getting time table", Toast.LENGTH_SHORT).show()
//            }
//        })
//        return unEnrollCoursesarrayList
//    }

//    private fun updateRecyclerView(unEnrollCoursesarrayList: MutableList<courseClass>){
//        var adapter=CourseUnenrollAdapter(unEnrollCoursesarrayList as ArrayList<courseClass>, this@unEnrollActivity)
//        coursesUnenrollRV.layoutManager=LinearLayoutManager(this@unEnrollActivity)
//        coursesUnenrollRV.adapter=adapter
//        adapter.notifyDataSetChanged()
//    }timetableData as ArrayList<courseClass>


    class CourseUnenrollAdapter( private var listOfCourses:ArrayList<courseClass> , private val context: Context): RecyclerView.Adapter<CourseUnenrollAdapter.unEnrollCourseViewHolder>(){

        private lateinit var mListener: onItemClickListener
        interface onItemClickListener{
            fun onItemClick(position:Int)
        }

        fun setOnItemClickListener(listener:onItemClickListener){
            mListener=listener
        }

        fun setFilteredList(filteredList:ArrayList<courseClass>){
            listOfCourses=filteredList
            notifyDataSetChanged()
        }

        //i have removed the listener from paramters
        class unEnrollCourseViewHolder(itemView: View,listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
            val courseCode=itemView.findViewById<TextView>(R.id.Unenroll_courseCode)
            val courseName=itemView.findViewById<TextView>(R.id.Unenroll_courseName)
            val unEnrollButton = itemView.findViewById<Button>(R.id.UnenrollButton)



            init{
                unEnrollButton.setOnClickListener{
                    listener.onItemClick(adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): unEnrollCourseViewHolder {
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.unenrollcourse,parent,false)
            return unEnrollCourseViewHolder(itemView,mListener)
        }


        override fun onBindViewHolder(holder: unEnrollCourseViewHolder, position: Int) {
            try{
                holder.courseCode.text= listOfCourses[position].courseCode!!.toString()
                holder.courseName.text= listOfCourses[position].courseName!!.toString()


//                holder.unEnrollButton.setOnClickListener{
//                    val builder = AlertDialog.Builder(context)
//
//                    builder.setTitle("Unenroll Course")
//
//                    builder.setMessage("Are You sure!! \n You want to Unenroll?")
//
//                    builder.setPositiveButton("Unenroll") { dialog, which ->
//                        val courseCode = listOfCourses[position].courseCode!!.toString()
//                        // Remove course from the student's Firebase Realtime Database
//                        val courseRef = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students").child(studentCMS!!).child("courses")
//                        courseRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                val coursesList = dataSnapshot.getValue<MutableList<String>>()
//                                val index = coursesList?.indexOf(courseCode) ?: -1
//                                if (index >= 0) {
//                                    // Remove the value at the found index
//                                    val coursesListUpdate = shiftIndextoLast(index, coursesList!!)
//                                    courseRef.setValue(coursesListUpdate)
//                                        .addOnSuccessListener {
//                                            Toast.makeText(context, "removed", Toast.LENGTH_SHORT)
//                                                .show()
//                                            val intent = Intent(this, First_Screen::class.java)
//                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                            startActivity(intent)
//                                        }
//                                        .addOnFailureListener { exception ->
//                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
//                                                .show()
//                                        }
//                                } else {
//                                    Toast.makeText(context, "Value not found", Toast.LENGTH_SHORT)
//                                        .show()
//                                }
//                            }
//                            override fun onCancelled(error: DatabaseError) {
//                                Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
//                            }
//                        })
//                    }
//                    builder.setNegativeButton("Cancel") { dialog, which ->
//                        dialog.dismiss()
//                    }
//                    val dialog = builder.create()
//                    dialog.show()
//
//
//
//
//                }
            }
            catch(ex: Exception){
                Log.d("Exception is here", ex.message.toString())
            }
        }

        override fun getItemCount(): Int {
            try{
                return listOfCourses?.size ?: 0
            }
            catch(ex:Exception){
                Log.d("Exception in GIC", ex.message.toString())
                return 0
            }
        }
    }

}