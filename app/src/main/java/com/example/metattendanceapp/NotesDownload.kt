package com.example.metattendanceapp

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_notes_download.*
import kotlinx.android.synthetic.main.shownotes.view.*
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class NotesDownload : AppCompatActivity() {
    var dwnldList = ArrayList<DownloadDetails>()
    var mToolbar: Toolbar? = null
    var dbUploads:DatabaseReference ?= null
    var classes: String? = null
    var class_name: Spinner? = null
    var ref = FirebaseDatabase.getInstance().reference
    var date = getDateInstance().format(Date())
    var dwnldBu:Button ?=null
    val STORAGE_PERMISSION_CODE =1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_download)
        mToolbar = findViewById(R.id.ftoolbar)
        dwnldBu = findViewById(R.id.buDownload)
        mToolbar?.title = "Student's| Dashboard |($date)"
        class_name = findViewById<View>(R.id.spinDownload) as Spinner
        classes = class_name?.selectedItem.toString()
    }

    inner class NotesDownloadAdapter(var list: ArrayList<DownloadDetails>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.shownotes, null)
            val myuploads = list[position]
            myView.textViewtid.text = myuploads.FileName
            myView.textViewstname.text = myuploads.UpBy

            myView.buDownload.setOnClickListener(View.OnClickListener {
                val url = myuploads.link
                val filename = myuploads.FileName
                Toast.makeText(applicationContext,"Downloading.....", Toast.LENGTH_LONG ).show()
                DownloadFile(url,filename)

            })



            return  myView
        }

        override fun getItem(position: Int): Any {
           return  list[position]
        }

        override fun getItemId(position: Int): Long {
            return  position.toLong()
        }

        override fun getCount(): Int {
            return  list.size
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun viewlist1(view: View) {
        dwnldList.clear()
        dbUploads = ref.child("Uploads")
        dbUploads!!.orderByChild("forclass").equalTo(class_name?.getSelectedItem().toString()).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (dsp:DataSnapshot? in p0.children) {

                    val  FileName = dsp?.child("fileName")?.getValue().toString()
                    val uploadedBy = dsp?.child("uploadedBy")?.getValue().toString()
                    val link =dsp?.child("url")?.getValue().toString()
                    dwnldList.add(DownloadDetails(FileName,uploadedBy,link))
                    //Toast.makeText(applicationContext,"Element: ",Toast.LENGTH_LONG ).show()
                }
                val  notesDownloadAdapter = NotesDownloadAdapter(dwnldList)
                lvDownloads.adapter = notesDownloadAdapter
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun DownloadFile(url:String?, fname:String?){
       if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M){
           if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

               requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
           }else{
               startdownloading(url, fname)
           }
       }else{
           startdownloading(url, fname)
       }

    }

    private fun startdownloading(url: String?, filename:String?) {
        val request =DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The File is Downloading....")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS," $filename")


        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as  DownloadManager
        manager.enqueue(request)


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_PERMISSION_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   Toast.makeText(applicationContext,"Click On Download", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}


