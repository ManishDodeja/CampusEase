package com.example.campusease

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class First_Screen : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private var number: Long = 0
    private var sName = ""
    private var sCMS = ""
    private lateinit var db: DatabaseReference
    private lateinit var listOfStudentTimetable:ArrayList<Timetable>;
    private lateinit var courseArrayList: ArrayList<String>;
    private lateinit var listOfCourses:ArrayList<courseClass>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)
        val bundle: Bundle? = intent.extras!!
        db = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app")
                .reference

        sName = bundle!!.getString("name").toString()
        sCMS = bundle.getString("cms").toString()

        generateDrawer()
    }

    fun cdcClick(v: View) {
        val intent = Intent(this, After_SOH::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun transportClick(v:View){
        val intent=Intent(this,TransportModule::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun timeTableClick(v: View) {
        val bundle: Bundle? = intent.extras!!
        val cmsValue = bundle!!.getString("cms")

        val intent = Intent(this@First_Screen, After_TimeTable::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("cms", cmsValue)
        startActivity(intent)
    }

    private fun getStudentCourses() : ArrayList<Timetable>{
        courseArrayList= arrayListOf()
        listOfStudentTimetable= arrayListOf()
        listOfCourses= arrayListOf()
        // root have child Students
        db.child("Students").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                // check for student with id CMS (which is login currently)
                if(snapshot.hasChild(sCMS)) {
                    //creating student model class object and get the data of particular student
                    val student = snapshot.child(sCMS!!).getValue(studentModel::class.java)
                    // take each course from student.courses and add it to courseArrayList
                    for (course in student!!.courses!!){
                        courseArrayList.add(course)
                    }
                }
            }//Datachange finished

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@First_Screen, "Error in adding courses to course array list", Toast.LENGTH_SHORT).show()
            }
        })
        //val timetableArrayList = arrayListOf<Timetable>()
        //here we will access timetable and
        db.child("Timetable").addValueEventListener(object :ValueEventListener
        {

            override fun onDataChange(snapshot: DataSnapshot)
            {
                if(snapshot.exists()) {
                    for(course in courseArrayList){
                        if(snapshot.hasChild(course)){
                            val timetable=snapshot.child(course).getValue(Timetable::class.java)
                            if (timetable != null) {
                                //listOfStudentTimetable.add(timetable)
                                val courseItem=courseClass(course, timetable.course_name)
                                listOfCourses.add(courseItem)
                            }

                        }
                    }
                }
            }//Datachange finished
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@First_Screen, "error in getting time table", Toast.LENGTH_SHORT).show()
            }
        })
        return listOfStudentTimetable
    }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@First_Screen)

        builder.setTitle("Log Out")

        builder.setMessage("Are You sure!! \n Do you want to logout?")

        builder.setPositiveButton("Log out") { dialog, which ->
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun generateDrawer() {
        // getting drawer layout and navigation view
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        // setting headerview with student which will login to the app
        val headerView = navView.getHeaderView(0)
        val studentName: TextView = headerView.findViewById(R.id.student_name)
        val studentCms: TextView = headerView.findViewById(R.id.student_cms)
        studentName.text = sName
        studentCms.text = sCMS

        toggle =
            ActionBarDrawerToggle(this@First_Screen, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                // enroll a course is selected from drawer
                R.id.nav_enroll -> {
                    showEnrollDialog()
                }

                R.id.nav_logout -> {
                    val builder = AlertDialog.Builder(this@First_Screen)

                    builder.setTitle("Log Out")

                    builder.setMessage("Are You sure!! \n Do you want to logout?")

                    builder.setPositiveButton("Log out") { dialog, which ->
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    builder.setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }

                R.id.nav_unenroll -> {
                    showUnEnrollDialog()
                }

                R.id.nav_feedback -> {
                    val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
                    builder.setTitle("Enter Feedback to developer")

                    // Set up the input
                    val input = EditText(this)

                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setHint("Type Feedback")
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    builder.setView(input)

                    // Set up the buttons
                    builder.setPositiveButton(
                        "Send",
                        DialogInterface.OnClickListener { dialog, which ->
                            // Here you get get input text from the Edittext
                            val Feedback = input.text.toString()
                            db.child("Feedback").child("$sCMS").setValue(Feedback)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@First_Screen,
                                        "Feedback sent Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@First_Screen,
                                        "Error in Sending Feedback ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("exception ", it.message.toString())
                                }
                        })
                    builder.setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                    builder.show()

                }

                R.id.nav_share -> {
                    val appMsg: String = "Hey! Check out this app :- " +
                            "https://play.google.com/store/apps/details?id=com.example.campusease"
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_TEXT, appMsg)
                    intent.type = "text/plain"
                    startActivity(intent)
                }

                R.id.nav_changePass -> {
                    getPreviousPassword()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showEnrollDialog(){
        //the code of fragments and the data base will be done here
        // when user click enroll the pop up should open and take input for course code
        val dialog = InputDialog(this)
        dialog.show()
        var inputValue = " "
        val btn = dialog.findViewById<Button>(R.id.SubmitCourseCodeButton)
        btn.setOnClickListener {

            inputValue = dialog.getInputValue().toString()
            dialog.dismiss()
            //this is the method where i have checked from the timetable collection that course exist or not
            checkCourse(inputValue)
        }
    }

    private fun addCourse(inputValue:String){
        val courseRef = db.child("Students").child(sCMS).child("courses")
        courseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                number = snapshot.childrenCount
                try {
                    courseRef.child(number.toString()).setValue(
                        inputValue,
                        object : DatabaseReference.CompletionListener {
                            override fun onComplete(
                                error: DatabaseError?,
                                ref: DatabaseReference
                            ) {
                                if (error != null) {
                                        // Handle the error here
                                } else {
                                    Toast.makeText(
                                            this@First_Screen,
                                            "Added",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                    onResume()
                                }
                            }
                        }
                    )
                } catch (e: Exception) {
                    Log.e(
                        "FirebaseError",
                        "Error adding student data to Firebase: ${e.message}"
                    )
                }
            }//Datachange finished

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@First_Screen,
                    "error in getting time table",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun checkCourse(course:String): Boolean{
        val timetableRef=db.child("Timetable")
        var returnValue=false
        timetableRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                returnValue = snapshot.hasChild(course)
                if (courseArrayList.contains(course)){
                    Toast.makeText(this@First_Screen,"Course Already Enrolled", Toast.LENGTH_SHORT).show()
                }
                else if(returnValue){
                    addCourse(course)
                }
                else{
                    Toast.makeText(this@First_Screen,"Course doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@First_Screen,
                    "error in checking course",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return returnValue;
    }

    private fun showUnEnrollDialog() {
        try{
            val intent=Intent(this@First_Screen,unEnrollActivity::class.java)
            intent.putExtra("ListOfCourses", listOfCourses)
            intent.putExtra("cms", sCMS)
            startActivity(intent)
        }
        catch(ex:Exception){
            Toast.makeText(this@First_Screen, ex.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun getPreviousPassword() {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Type Old Password")
        val input = EditText(this)
        input.setHint("Enter Old Pass")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Submit") { dialog, which ->
            val oldPass = input.text.toString()

            getPasswordFromFirebase { password, exception ->
                if (exception != null) {
                    Toast.makeText(this@First_Screen, exception.message, Toast.LENGTH_SHORT).show()
                } else {
                    if (oldPass == password) {
                        UpdateNewPassword()
                    } else {
                        Toast.makeText(this@First_Screen, "Not Matched", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }

        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    fun UpdateNewPassword() {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("New Password")
        val input = EditText(this)
        input.setHint("Enter New Password")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Update") { dialog, which ->
            val NewPass = input.text.toString()
            db.child("Students").child(sCMS).child("password").setValue(NewPass)
                .addOnSuccessListener {
                    Toast.makeText(this@First_Screen, "Password changed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@First_Screen, "Failed " + it.message, Toast.LENGTH_SHORT)
                        .show()
                }
        }

        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }


    private fun getPasswordFromFirebase(callback: (String?, Exception?) -> Unit) {
        var password: String? = null
        db.child("Students").child("$sCMS").child("password").get().addOnSuccessListener { dataSnapshot ->
            password = dataSnapshot.getValue(String::class.java)
            callback(password, null)
        }.addOnFailureListener { exception ->
            callback(null, exception)
        }
    }

    override fun onResume() {
        super.onResume()
        getStudentCourses()
    }


}

