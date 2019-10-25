package com.example.metattendanceapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class addteacher extends AppCompatActivity{
    ListView listViewteacher;
    List<Teacher> teacherList;
    EditText Tname;
    EditText Tid;
    EditText subject,tpassword;
    String tname,tid,sub,classname,tpass;
    Spinner classes;
    DatabaseReference databaseTeacher;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteacher);

        databaseTeacher = FirebaseDatabase.getInstance().getReference("Teacher");
        listViewteacher = (ListView) findViewById(R.id.listViewteachers);
        teacherList =new ArrayList<>();
        Tname =  (EditText) findViewById(R.id.editText1);
        Tid =  (EditText) findViewById(R.id.editText3);
        subject =  (EditText) findViewById(R.id.editText4);
        classes = (Spinner) findViewById(R.id.spinner3);
        tpassword =  (EditText) findViewById(R.id.editText5);
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        mToolbar.setTitle("Add/Remove Teacher");


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseTeacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherList.clear();
                for(DataSnapshot teachsnap : dataSnapshot.getChildren()){
                    Teacher teacher = teachsnap.getValue(Teacher.class);
                    teacherList.add(teacher);

                }
                show_teachers adaptor = new show_teachers(addteacher.this, teacherList);
                listViewteacher.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addTeacher(View view) {
        tname = Tname.getText().toString();
        tid = Tid.getText().toString();
        sub = subject.getText().toString();
        classname = classes.getSelectedItem().toString();
        tpass = tpassword.getText().toString();

        if (!(TextUtils.isEmpty(Tid.getText().toString()))) {
            // String id = databaseTeacher.push().getKey();
            Teacher teacher =new Teacher(tname ,tid ,sub ,classname,tpass);
            databaseTeacher.child(tid).setValue(teacher);
            Toast.makeText(getApplicationContext(),"Teacher added successfully", Toast.LENGTH_LONG).show();
            finish();

        }else {
            Toast.makeText(getApplicationContext(),"fields cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    public void removeTeacher(View view) {

        if (!TextUtils.isEmpty(Tid.getText().toString())) {
            tid = Tid.getText().toString();
            databaseTeacher.child(tid).setValue(null);
            Toast.makeText(getApplicationContext(),"Teacher removed successfully", Toast.LENGTH_LONG).show();
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
