package com.example.campusease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ForgotPassword : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        database=FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students")
        emailEditText = findViewById(R.id.email_edittext)

        val cms =intent.getStringExtra("cms").toString()
        Log.d("Before submit button ", "ok ${cms}")
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            Log.d("inside button click", "ok")
            val dbref=database.child(cms).child("email")
            Log.d("dbref", "$dbref")
            if (email.isBlank()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("inside else", "ok")
                dbref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Toast.makeText(this@ForgotPassword, "inside ", Toast.LENGTH_SHORT).show()
                        val emailFromDB= snapshot.getValue(String::class.java).toString()
                        if (emailFromDB == email) {
                            Toast.makeText(this@ForgotPassword, "Email matched", Toast.LENGTH_SHORT).show()

                            // Email address has been verified, send password reset email
                            FirebaseAuth.getInstance().sendPasswordResetEmail("manesh.bscsf19@iba-suk.edu.pk")
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@ForgotPassword, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()

                                    } else {
                                        Toast.makeText(this@ForgotPassword, "Failed to send email to  $email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this@ForgotPassword, "Email is not verified ", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ForgotPassword, "Error ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}