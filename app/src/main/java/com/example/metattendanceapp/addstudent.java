package com.example.metattendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class addstudent extends AppCompatActivity{

    EditText Sname;
    EditText Sid,spassword;
    String sname,sid,classname,spass;
    Spinner classes;
    DatabaseReference databaseStudent;
    ListView listViewstudent;
    ArrayAdapter adapter;
    List<Student> studentList;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);

        databaseStudent = FirebaseDatabase.getInstance().getReference("Student");
        listViewstudent = findViewById(R.id.listviewstudent);
        studentList =new ArrayList<>();
        Sname =   findViewById(R.id.editText1);
        Sid =   findViewById(R.id.editText3);
        classes =  findViewById(R.id.spinner3);
        spassword =  findViewById(R.id.editText4);
        mToolbar= findViewById(R.id.ftoolbar);
        mToolbar.setTitle("Add/Remove Student");


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
               for(DataSnapshot studentsnap : dataSnapshot.getChildren())
               {
                   Student student = studentsnap.getValue(Student.class);
                   studentList.add(student);
               }
               show_students adaptor = new show_students(addstudent.this, studentList);
               listViewstudent.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void addStudent(View view) {


        if (!(TextUtils.isEmpty(Sid.getText().toString()))) {

            sname = Sname.getText().toString();
            sid = Sid.getText().toString();
            classname = classes.getSelectedItem().toString();
            spass = spassword.getText().toString();

            Student student =new Student(sname ,sid,classname,spass );
            databaseStudent.child(sid).setValue(student);
            Toast.makeText(getApplicationContext(),"student added successfully", Toast.LENGTH_LONG).show();
            finish();

        }else {
            Toast.makeText(getApplicationContext(),"fields cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    public void removeStudent(View view) {

        if (!TextUtils.isEmpty(Sid.getText().toString())) {
            sid = Sid.getText().toString();
            databaseStudent.child(sid).setValue(null);
            Toast.makeText(getApplicationContext(),"student removed successfully", Toast.LENGTH_LONG).show();
            finish();

        }else {
            Toast.makeText(getApplicationContext(),"id cannot be empty", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
