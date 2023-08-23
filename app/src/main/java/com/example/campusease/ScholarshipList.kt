package com.example.campusease

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ScholarshipList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scholarshipArrayList:ArrayList<Scholarships>
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scholarship_list)

        recyclerView=findViewById(R.id.Scholarship_RecyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this)
        scholarshipArrayList=arrayListOf()
        databaseReference= FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Scholarships")
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(datasnapshot in snapshot.children){
                        val scholarshipObj=datasnapshot.getValue(Scholarships::class.java)
                        scholarshipArrayList.add(scholarshipObj!!)
                        Log.d("scholarshipName", scholarshipObj.name.toString())
                    }
                    var adapter=ScholarshipAdapter(scholarshipArrayList, this@ScholarshipList)
                    recyclerView.adapter=adapter
                    adapter.notifyDataSetChanged()


                    adapter.setOnItemClickListener(object: ScholarshipAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            //Toast.makeText(this@MainActivity, "You clicked on: $position", Toast.LENGTH_SHORT).show()
                            val intent= Intent(this@ScholarshipList, InsideScholarship::class.java)
                            intent.putExtra("name",scholarshipArrayList[position].name)
                            intent.putExtra("deadline", scholarshipArrayList[position].deadline)
                            intent.putExtra("description", scholarshipArrayList[position].description)
                            intent.putExtra("eligibility", scholarshipArrayList[position].eligibility)
                            intent.putExtra("link", scholarshipArrayList[position].link)
                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ScholarshipList,  error.toString(),Toast.LENGTH_SHORT).show()
            }

        })
    }
}

