package com.rajkumar.cam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class showData : AppCompatActivity() {
    private  lateinit var databse:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)
        val bundle: Bundle? = intent.extras
        val id = bundle?.get("ID")
        val date = bundle?.get("date")



        readData(id as String,date as String)




    }

    private fun readData(id: String, date: String) {
        val tvName:TextView = findViewById(R.id.tvName)
        val tvBranch:TextView = findViewById(R.id.tvBranch)
        val tvrollno:TextView = findViewById(R.id.tvrollno)
        val tvdatetime:TextView = findViewById(R.id.tvdatetime)
        val tvdatetimeEnd:TextView = findViewById(R.id.tvdatetimeEnd)
        val tvdate:TextView = findViewById(R.id.tvdate)
        databse = FirebaseDatabase.getInstance().getReference("iot")
        databse.child(id).get().addOnSuccessListener {
            if(it.exists()){
                val name = it.child("name").value
                val rollno = it.child("rollNo").value
                val branch = it.child("branch").value
                val date2  = it.child("date").value //  getting date
                Toast.makeText(this, "Successfully Read", Toast.LENGTH_SHORT).show()
                tvName.text = name.toString()
                tvBranch.text = branch.toString()
                tvrollno.text = rollno.toString()
                tvdatetime.text = date // time
                tvdate.text = date2.toString() // date


            }else{
                Toast.makeText(this, "User Doesn't Exit", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {

            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()

        }

    }
}