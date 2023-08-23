package com.example.campusease

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging


/*
    Comment Section for making my code readable in future.
    this is login screen where user is login with credentials or can signup to the app

 */



class MainActivity : AppCompatActivity() {
    //Variables to get text frm page and making database reference
    private lateinit var dbRef: DatabaseReference
    private lateinit var cmsTV:EditText
    private lateinit var passTV:EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //database access for student collection
        dbRef = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        val forgotPasswordTextView= findViewById<TextView>(R.id.forgotPass_textView)
        forgotPasswordTextView.isClickable=true

        forgotPasswordTextView.setOnClickListener {
            forgotPasswordFun()
        }

    }

    fun LogInClick(v: View){

        cmsTV=findViewById(R.id.username_txt)
        passTV=findViewById(R.id.editTextTextPassword)

        val cmsValue=cmsTV.text.toString().trim()
        val cmsPass=passTV.text.toString().trim()

        if(cmsValue.isEmpty()){
            cmsTV.error="Enter your CMS"
            return;
        }
        else if(cmsPass.isEmpty()){
            passTV.error="Enter your password"
            return;
        }
        else {
            dbRef.child("Students").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(cmsValue)) {
                        val student = snapshot.child(cmsValue).getValue(studentModel::class.java)
                        val pass = student!!.password.toString()
                        val able=student!!.approved

//                        Toast.makeText(this@MainActivity, "password from student object $pass",Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this@MainActivity, "password from Field $cmsPass",Toast.LENGTH_SHORT).show()

                        if (cmsPass == pass) {
                            if(able){
                                val tokens= FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("FCMTokens")
                                Log.d("Ref ", tokens.toString())
                                FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                                    val data=object{
                                        var token_ID = token.toString()
                                    }
                                    //Toast.makeText(this@MainActivity, "$token_ID", Toast.LENGTH_SHORT).show()

                                    tokens.child(cmsValue).setValue(data)
                                        .addOnSuccessListener {
                                            Toast.makeText(this@MainActivity, "Generated", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener{
                                            Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                                        }
                                }

                                Toast.makeText(this@MainActivity, "Welcome to the campus ease ${student.name} ", Toast.LENGTH_SHORT).show()
                                val intent= Intent(this@MainActivity, First_Screen::class.java)
                                intent.putExtra("name", student.name.toString())
                                intent.putExtra("cms", student.cms.toString())
                                startActivity(intent)

                            }
                            else{
                                Toast.makeText(this@MainActivity, "Still your Request is not Accepted by admin",Toast.LENGTH_SHORT).show()
                                return
                            }

                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Incorrect Password",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Incorrect Cms", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "${error.message}", Toast.LENGTH_SHORT).show()
                    return
                }
            })
        }
    }
    fun SignUpClick(v:View){
        val intent=Intent(this, Signup_Screen::class.java)
//        intent.putExtra("timetableList", timetableData)
        startActivity(intent)
    }

    fun forgotPasswordFun(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Your CMS!!")
        val input = EditText(this)
        input.setHint("Type here..!")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Submit") { dialog, which ->
            if(input.text.toString()!=null){
                val intent=Intent(this, SecurityQuestions::class.java)
                intent.putExtra("cms",input.text.toString())
                intent.putExtra("value","2")
                startActivity(intent)
            }
            else{
                Toast.makeText(this@MainActivity, "Enter Your CMS First", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        builder.show()

    }

}