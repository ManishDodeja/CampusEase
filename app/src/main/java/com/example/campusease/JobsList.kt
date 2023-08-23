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
import com.squareup.picasso.Picasso

class JobsList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var jobArrayList:ArrayList<Jobs>
    private lateinit var databaseReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_list)


        recyclerView=findViewById(R.id.recyclerView_JobsList)
        recyclerView.layoutManager= LinearLayoutManager(this)
        jobArrayList=arrayListOf()
        databaseReference= FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Jobs")
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(datasnapshot in snapshot.children){
                        val job=datasnapshot.getValue(Jobs::class.java)
                        jobArrayList.add(job!!)
                    }
                    var adapter=JobsAdapter(jobArrayList, this@JobsList)
                    recyclerView.adapter=adapter
                    adapter.notifyDataSetChanged()


                    adapter.setOnItemClickListener(object: JobsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            //Toast.makeText(this@MainActivity, "You clicked on: $position", Toast.LENGTH_SHORT).show()
                            val intent= Intent(this@JobsList, InsideJob::class.java)
                            intent.putExtra("company",jobArrayList[position].company)
                            intent.putExtra("deadline", jobArrayList[position].deadline)
                            intent.putExtra("department", jobArrayList[position].department)
                            intent.putExtra("experience", jobArrayList[position].experience)
                            intent.putExtra("imageurl", jobArrayList[position].imageurl)
                            intent.putExtra("job_title", jobArrayList[position].job_title)
                            intent.putExtra("location", jobArrayList[position].location)
                            intent.putExtra("qualification", jobArrayList[position].qualification)
                            intent.putExtra("salary_range", jobArrayList[position].salary_range)
                            intent.putExtra("jobLink", jobArrayList[position].jobLink)

                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@JobsList,  error.toString(),Toast.LENGTH_SHORT).show()
            }

        })
    }
}