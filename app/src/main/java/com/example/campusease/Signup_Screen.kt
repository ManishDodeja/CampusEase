package com.example.campusease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Signup_Screen : AppCompatActivity() {
    lateinit var student_name: EditText
    lateinit var student_cms: EditText
    lateinit var student_email: EditText
    lateinit var student_dept: Spinner
    lateinit var student_sem:Spinner
    lateinit var student_sec: EditText

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_screen)


        student_dept=findViewById(R.id.spinner)
        val objects= resources.getStringArray(R.array.Departments)
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,objects)
        student_dept.adapter=adapter


        student_sem=findViewById(R.id.spinner2)
        val objects2=resources.getStringArray(R.array.Semesters)
        val adapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,objects2)
        student_sem.adapter=adapter2

        student_cms=findViewById(R.id.CMS_txt)
        student_name=findViewById(R.id.Name_txt)
        student_email=findViewById(R.id.Email_txt)
        student_sec=findViewById(R.id.Section_txt)

        dbRef = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students")
        val btn=findViewById<Button>(R.id.button)
        btn.setOnClickListener{
            onSubmit()
        }

    }
    private fun onSubmit(){
        val selectedDept=student_dept.getItemAtPosition(student_dept.selectedItemPosition)
        val selectedSem= student_sem.getItemAtPosition(student_sem.selectedItemPosition)
        val cms = student_cms.text.toString().trim()
        Toast.makeText(this, cms, Toast.LENGTH_SHORT).show()
        val name=student_name.text.toString().trim()
        val email =student_email.text.toString().trim()
        val section=student_sec.text.toString().uppercase().trim()
        //Toast.makeText(this, selectedDept.toString(), Toast.LENGTH_SHORT).show()

        // here i have to create the firebase database where i have to insert the data in application

        if(name.isEmpty()){
            student_name.error="Enter your name please"
            return;
        }
        if(cms.isEmpty()){
            student_cms.error="Enter your cms please"
            return
        }
        if(email.isEmpty()){
            student_email.error="Enter your email please"
            return;
        }
        if(section.isEmpty()){
            student_sec.error="Enter your Section please"
            return;
        }
        if(selectedDept.toString()=="Select Department"){

            Toast.makeText(this, "Select your Department", Toast.LENGTH_SHORT).show()
            return;
        }
        if(selectedSem.toString()=="Select Semester"){
            Toast.makeText(this, "Select your Semester", Toast.LENGTH_SHORT).show()
            return;
        }
        else {
            val studentObj = studentModel(
                cms,
                name,
                email,
                selectedDept.toString(),
                selectedSem.toString(),
                section,
                password = null,
                courses = null,
                approved = false
            )
            try {
                dbRef.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                          if(snapshot.hasChild(cms)){
                              Toast.makeText(this@Signup_Screen, "This CMS is already Registered", Toast.LENGTH_SHORT).show()
                              student_cms.error="This CMS is Already Registered"
                              return;
                          }
                        else{
                              dbRef.child(cms).setValue(studentObj)
                                  .addOnCompleteListener {
                                      Toast.makeText(this@Signup_Screen, "Data Inserted Successfully", Toast.LENGTH_LONG).show()
                                      val intent = Intent(this@Signup_Screen, Add_Courses::class.java)
                                      intent.putExtra("cms",cms)
                                      intent.putExtra("studentObj",studentObj)
                                      intent.putExtra("email", email)
                                      startActivity(intent)

                                  }
                                  .addOnFailureListener { err ->
                                      Toast.makeText(this@Signup_Screen, "Error ${err.message}", Toast.LENGTH_LONG).show()
                                  }
                          }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("FirebaseError", "Error adding student data to Firebase: ${error.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("FirebaseError", "Error adding student data to Firebase: ${e.message}")
            }






        }
    }
}