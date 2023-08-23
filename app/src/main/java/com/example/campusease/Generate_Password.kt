package com.example.campusease

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Generate_Password : AppCompatActivity() {
    lateinit var pass:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_password)
        pass=findViewById(R.id.password_text)

    }

    fun onPassSubmit(v: View){
        val cmsValue=intent.getStringExtra("cms").toString()
        val studentObj=intent.getSerializableExtra("studentObj") as studentModel

        val db = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students/$cmsValue")
        try {
            db.child("password").setValue(pass.text.toString())
                .addOnCompleteListener {
                    studentObj.password=pass.text.toString()
                    Toast.makeText(this, "Password Inserted Successfully", Toast.LENGTH_LONG).show()
                    val coursesArrayList=intent.getStringArrayListExtra("coursesList")
                    val intent=Intent(this, SecurityQuestions::class.java)
                    intent.putExtra("cms", cmsValue)
                    intent.putExtra("value","1")
                    intent.putStringArrayListExtra("coursesList", coursesArrayList)
                    startActivity(intent)
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error adding Password to Database: ${e.message}")
        }



    }
}