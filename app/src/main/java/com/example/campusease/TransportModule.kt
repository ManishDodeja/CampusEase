package com.example.campusease

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class TransportModule : AppCompatActivity() {
    private lateinit var RVTransport: RecyclerView
    private lateinit var transportArrayList:ArrayList<TransportDataClass>
    private lateinit var listOfImages:ArrayList<Int>
    private lateinit var dbRef: DatabaseReference;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_module)
        RVTransport=findViewById(R.id.recyclerView_Transport)
        RVTransport.layoutManager= LinearLayoutManager(this)
        transportArrayList=arrayListOf()

        listOfImages= arrayListOf()
        listOfImages.add(R.drawable.dolphinsukkur)
        listOfImages.add(R.drawable.hira)
        listOfImages.add(R.drawable.hostel)
        listOfImages.add(R.drawable.mroad)
        listOfImages.add(R.drawable.oldsukkur)
        listOfImages.add(R.drawable.qasimpark)
        listOfImages.add(R.drawable.rohri)
        listOfImages.add(R.drawable.township)


        dbRef= FirebaseDatabase.getInstance("https://campuseasedatabase-d9f82-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Transport")
        getData()
    }
    fun showAlertDialog(context: Context, title: String,PT1:String, PT2:String,  DT1:String, DT2:String, DT3:String ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val title2="Transport time for "+title
        val message= "Pick Up Time (1st Shift) :"+PT1+"\n"+"Pick Up Time (2st Shift) :"+PT2+"\n"+"Drop Time (1st Shift) :"+DT1+"\n"+"Drop Time (2nd Shift) :"+DT2+"\n"+"Drop Time (3rd Shift) :"+DT3
        // Set the title and message for the dialog
        alertDialogBuilder.setTitle(title2)
        alertDialogBuilder.setMessage(message)

        // Set a positive button to close the dialog
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun getData(){
        dbRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(datasnapshot in snapshot.children){
                        val transportObject=datasnapshot.getValue(TransportDataClass::class.java)
                        transportArrayList.add(transportObject!!)
                    }
                    var adapter=TransportAdapter(transportArrayList, listOfImages, this@TransportModule)
                    RVTransport.adapter=adapter
                    adapter.notifyDataSetChanged()


                    adapter.setOnItemClickListener(object: TransportAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            showAlertDialog(this@TransportModule,transportArrayList[position].Area.toString(),transportArrayList[position].PtimeOne.toString(),transportArrayList[position].PtimeTwo.toString(),transportArrayList[position].DropTimeOne.toString(),transportArrayList[position].DropTimeTwo.toString(),transportArrayList[position].DropTimeThree.toString())
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TransportModule,  error.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

}