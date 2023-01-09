package com.rajkumar.cam

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val CAMERA_REQUEST_CODE = 101
class MainActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    lateinit var calendar: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var date:String
    lateinit var dateDAte:String

    lateinit var calendar2: Calendar
    lateinit var simpleDateFormat2: SimpleDateFormat
    lateinit var date2:String
    private lateinit var database : DatabaseReference
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<user>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermission()
        codeScanner()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun codeScanner(){

        val scanner_view: CodeScannerView = findViewById(R.id.scanner_view)
        var tv_textView:TextView = findViewById(R.id.tv_textView)
        val btnView:Button = findViewById(R.id.btnView)
        val btnView2:Button = findViewById(R.id.btnView2)

        btnView.setOnClickListener {
            val intent = Intent(this,showData::class.java)
        }
        codeScanner = CodeScanner(this,scanner_view)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    tv_textView.text = it.text
                    val code = it.text


                    startActivity(intent)

                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main","camera not working ${it.message}")
                }
            }
        }
        
        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }

        // timeg
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat( "HH:mm:ss", Locale.US)
        simpleDateFormat2 = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        dateDAte = simpleDateFormat2.format(calendar.getTime()) //date
      ///  date  = simpleDateFormat.format(calendar.getTime())// time






        //btn
        btnView.setOnClickListener {
            val code = tv_textView.text

            // sending data
//            val data_time = date


            database = FirebaseDatabase.getInstance().getReference("iot")
            database.child(code as String).child("inTime").setValue(date) // pushing time
            database.child(code  as String).child("date").setValue(dateDAte) //pushing date
            val intent = Intent(this,showData::class.java)
            intent.putExtra("ID",code)
            intent.putExtra("date",date)
            startActivity(intent)
        }
        btnView2.setOnClickListener {
            val code = tv_textView.text
            val intent = Intent(this,showdata2::class.java)
            intent.putExtra("ID",code)
            startActivity(intent)

        }

    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun setupPermission(){
        val permission:Int = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        if(permission!= PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {


        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty()||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "give camera permission", Toast.LENGTH_SHORT).show()
                }
                else{
                    //done
                }
            }
        }

    }
}