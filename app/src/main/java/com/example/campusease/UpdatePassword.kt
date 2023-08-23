package com.example.campusease

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class UpdatePassword : AppCompatActivity() {
    private lateinit var newPass:EditText
    private lateinit var confPass:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        newPass=findViewById(R.id.NewPasswordEditText)
        confPass=findViewById(R.id.ConfirmPasswordEditText)
    }

    fun submitClick(v: View){
        val cms=intent.getStringExtra("cms").toString()
        Toast.makeText(this@UpdatePassword, "$cms", Toast.LENGTH_SHORT).show()
        Toast.makeText(this@UpdatePassword, "$cms", Toast.LENGTH_SHORT).show()

        val db = FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students/$cms")

        if(newPass.text.toString()==confPass.text.toString()){
            try {
                Toast.makeText(this@UpdatePassword, "Inside try", Toast.LENGTH_SHORT).show()
                db.child("password").setValue(newPass.text.toString())
                    .addOnCompleteListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Information")
                        builder.setMessage("Your Password has been changed!!")
                        builder.setPositiveButton("OK") { dialog, which ->
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                    .addOnFailureListener { err ->
                        Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_LONG).show()
                    }
            } catch (e: Exception) {
                Log.e("FirebaseError", "Error in Resetting Password : ${e.message}")
            }
        }
        else{
            Toast.makeText(this@UpdatePassword, "Else is running", Toast.LENGTH_SHORT).show()

        }



    }
}