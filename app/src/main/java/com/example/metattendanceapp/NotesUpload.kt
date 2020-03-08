package com.example.metattendanceapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class NotesUpload : AppCompatActivity() {
    private var mStorageRef: StorageReference? = null
    lateinit var alertDialog: AlertDialog
    lateinit var uri : Uri
    val PDF : Int = 0
    val DOCX : Int = 1
    var Doc: Button ?= null
    var noti:TextView ?= null
    var PDf: Button ?=null
    var storage:FirebaseStorage ?=null
    var class_selected:String ?= null
    var teacher_id:String ?= null
    var db:DatabaseReference ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle1 = intent.extras
        db= FirebaseDatabase.getInstance().getReference("Uploads")
        class_selected = bundle1.getString("class_selected")
        teacher_id = bundle1.getString("tid")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_upload)
        storage = FirebaseStorage.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads")

        Doc = findViewById(R.id.PDOC)
        PDf = findViewById(R.id.FPDF)
        noti = findViewById(R.id.Fname)
        Doc!!.setOnClickListener(View.OnClickListener {
            view:View?  -> val intent =Intent()
            intent.setType("application/docx")
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        noti = findViewById(R.id.Fname)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PDF) {
               uri = data!!.data
                noti!!.text =uri.toString()
                upload()
               }else if(requestCode ==DOCX){
                uri = data!!.data
                noti!!.text =uri.toString()
                upload()
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun upload() {
        var mReference = mStorageRef!!.child(uri.lastPathSegment)

        try {
            mReference.putFile(uri).addOnSuccessListener{
                taskSnapshot: UploadTask.TaskSnapshot? -> var url = mReference!!.downloadUrl
               // val up = UploadDetails(teacher_id!!, class_selected!!, url)
                //db!!.child(teacher_id!!).setValue(up)
                Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_LONG).show()
            }
        }catch (e:Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}