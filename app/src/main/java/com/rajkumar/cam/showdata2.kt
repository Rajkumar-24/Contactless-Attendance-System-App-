package com.rajkumar.cam

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class showdata2 : AppCompatActivity() {

    lateinit var calendar2: Calendar
    lateinit var simpleDateFormat2: SimpleDateFormat


    lateinit var date2:String
    private lateinit var name:String
    private lateinit var rollno:String
    private lateinit var id:String
    private lateinit var branch:String
    private lateinit var startTime:String
    private lateinit var endTime:String
    private lateinit var dateDate:String
    private  lateinit var databse: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showdata2)
        val bundle: Bundle? = intent.extras
        id = bundle?.get("ID").toString()
        //var date2 = bundle?.get("date2")
        calendar2 = Calendar.getInstance()
        simpleDateFormat2 = SimpleDateFormat(" HH:mm:ss", Locale.US)

        date2  = simpleDateFormat2.format(calendar2.time.time)// time2


        val dateend = date2


        // sending data time
        databse = FirebaseDatabase.getInstance().getReference("iot")
        databse.child(id as String).child("outTime").setValue(dateend)
        readData(id , dateend )
//        sendData()
    }
    private fun readData(id: String, dateend: String) {
        val tvName: TextView = findViewById(R.id.tvName)
        val tvBranch: TextView = findViewById(R.id.tvBranch)
        val tvrollno: TextView = findViewById(R.id.tvrollno)
        val tvdatetime: TextView = findViewById(R.id.tvdatetime)
        val tvdatetimeEnd: TextView = findViewById(R.id.tvdatetimeEnd)
        val tvdate:TextView = findViewById(R.id.tvdate)
        databse = FirebaseDatabase.getInstance().getReference("iot")
        databse.child(id).get().addOnSuccessListener {
            if(it.exists()){

                name = it.child("name").value.toString()
                rollno = it.child("rollNo").value.toString()
                branch = it.child("branch").value.toString()
                startTime = it.child("inTime").value.toString()
                endTime = it.child("outTime").value.toString()
//                dateDate = it.child("date").value.toString()
                Toast.makeText(this, "Successfully Read", Toast.LENGTH_SHORT).show()
                tvName.text = name.toString()
                tvBranch.text = branch.toString()
                tvrollno.text = rollno.toString()
                tvdatetime.text = startTime.toString()
                tvdatetimeEnd.text = endTime.toString()
                tvdate.text = dateDate
                sendData()


            }else{
                Toast.makeText(this, "User Doesn't Exit", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {

            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()

        }

    }
    private fun sendData(){
        val url = "https://script.google.com/macros/s/AKfycbxtdKgPmc-U9WIwtvU4uey2NrD7HIAXWro73SFTgqRzWYbJvwze/exec"
        val stringRequest = object :StringRequest(Request.Method.POST,url,
            Response.Listener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                val params= HashMap<String,String>()
                params["Date"] = dateDate
                params["Id"]= id
                params["Name"]= name
                params["Branch"]= branch
                params["RollNo"]= rollno
                params["InTime"]= startTime
                params["OutTime"]= endTime
                return params
            }
        }
        val socketTimeOut = 50000 // u can change this .. here it is 5 seconds


        val retryPolicy: RetryPolicy =
            DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = retryPolicy
        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }
}