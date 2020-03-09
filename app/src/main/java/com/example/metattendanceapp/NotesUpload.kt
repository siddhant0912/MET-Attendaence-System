package com.example.metattendanceapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NotesUpload : AppCompatActivity() {
    private var mStorageRef: StorageReference? = null
    lateinit var uri : Uri
    val PDF : Int = 0
    var Flag:Boolean ?=null
    val DOCX : Int = 1
    var Doc: Button ?= null
    var noti:TextView ?= null
    var NotesDes:TextView?=null
    var PDf: Button ?=null
    var tclass:String ?= null
    var tid:String ?= null
    var mDialog:ProgressDialog ?= null
    private lateinit var db:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        db= FirebaseDatabase.getInstance().reference
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_upload)
        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads")
        Flag = false
        Doc = findViewById(R.id.PDOC)
        PDf = findViewById(R.id.FPDF)
        noti = findViewById(R.id.Fname)
        NotesDes=findViewById(R.id.NotesDes)

        Doc!!.setOnClickListener(View.OnClickListener {

            view:View?  -> val intent =Intent()
            intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"select Docx"),DOCX)
        })
        PDf!!.setOnClickListener(View.OnClickListener {
            view :View? -> val intent =Intent()
            intent.setType("application/pdf")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"select Docx"),PDF)
        })

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        noti = findViewById(R.id.Fname)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PDF) {
                uri = data!!.getData()
                noti!!.setText("Selected File:"+uri.lastPathSegment)
                val notDes =(NotesDes!!.text).toString()
                upload(notDes)
               }else if(requestCode ==DOCX){
                uri = data!!.getData()
                noti!!.setText("Selected File:"+uri.lastPathSegment)
                val notDes =(NotesDes!!.text).toString()
                upload(notDes)
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun upload(notesDes:String) {
        var mReference = mStorageRef!!.child(uri.lastPathSegment)

        val bundle1 = intent.extras
        tclass = bundle1.getString("class_selected").toString()
        tid= bundle1.getString("tid").toString()
        mDialog = ProgressDialog(this)
        mDialog!!.setMessage("Uploading Please Wait.....")
        mDialog!!.setTitle("Loading")
        mDialog!!.show()


        try {
            mReference.putFile(uri).addOnSuccessListener{

                taskSnapshot: UploadTask.TaskSnapshot? -> var url:Task<Uri> = taskSnapshot!!.getStorage().getDownloadUrl()
                while (!url.isSuccessful());
                   val downloadUrl: Uri? = url.getResult()
                val up = UploadDetails(tid!!, tclass!!,downloadUrl.toString(),notesDes)
                val file = System.currentTimeMillis().toString()
                db.child("Uploads").child(file).setValue(up)
                Toast.makeText(this, "File Successfully Uploaded", Toast.LENGTH_LONG).show()
                Flag =true
                mDialog!!.dismiss()
            }
        }catch (e:Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            mDialog!!.dismiss()
        }
    }
}