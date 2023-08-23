package com.example.campusease

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Add_Courses : AppCompatActivity() {

    lateinit var add: Button
    lateinit var submit:Button
    lateinit var size: EditText
    lateinit var list: LinearLayout
    lateinit var context: Context
    private lateinit var cmsValue:String
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_courses)
        add=findViewById(R.id.AddCourse_Btn)
        submit=findViewById(R.id.Submit_Btn)
        size=findViewById(R.id.editText)
        list=findViewById(R.id.container)
        dbRef=FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        cmsValue= intent.getStringExtra("cms").toString()
        context=this

    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@Add_Courses)

        builder.setTitle("Information")
        builder.setMessage("You want to leave the page?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val studentRef = dbRef.child("Students")
            studentRef.child("$cmsValue").removeValue()
                .addOnSuccessListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    fun AddCourse(v: View){
        val view=layoutInflater.inflate(R.layout.dynamic_edit_text,null, false)
        val cross: ImageView = view.findViewById(R.id.cross2) as ImageView
        View.VISIBLE.also { cross.visibility = it }

        cross.setOnClickListener(View.OnClickListener {
            list.removeView(view)
        })
        list.addView(view)
    }

    fun OnSubmit(v:View){
        val coursesList=ArrayList<String>()
        val childCount=list.childCount
        for(i in 0 until childCount){
            if(list.getChildAt(i) is LinearLayoutCompat){
                val ll :LinearLayoutCompat= list.getChildAt(i) as LinearLayoutCompat
                for(j in 0 until ll.childCount){
                    if(ll.getChildAt(j) is EditText){
                        val et:EditText=ll.getChildAt(j) as EditText
                        if(et.id==R.id.editText){
                            coursesList.add(et.text.toString())
                        }
                    }
                }
            }
        }
        //get the cms from previous activity
        val studentObj=intent.getSerializableExtra("studentObj") as studentModel
        val email=intent.getStringExtra("email").toString()



        //creating database ref for adding courses
        val db = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students/$cmsValue")

        try {
            db.child("courses").setValue(coursesList)
                .addOnCompleteListener {
                    studentObj.courses=coursesList
                    val intent= Intent(this, Generate_Password::class.java)
                    intent.putExtra("studentObj", studentObj)
                    intent.putExtra("cms", cmsValue)
                    intent.putExtra("email", email)
                    intent.putStringArrayListExtra("coursesList",coursesList)
                    startActivity(intent)
                    Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error adding student data to Firebase: ${e.message}")
        }



    }

}