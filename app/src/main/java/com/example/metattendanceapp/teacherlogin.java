package com.example.metattendanceapp;
import android.os.Bundle;
import android.view.View;

import android.content.Intent;

import android.widget.Toolbar;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class teacherlogin extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

      String item;
      String message;
      Toolbar mToolbar;
    private static long back_pressed;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);


        //to get username from login page
        Bundle bundle1 = getIntent().getExtras();
        message = bundle1.getString("message");
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        mToolbar.setTitle("Teacher's| Dashboard |" + "(" + date + ")");

        TextView txtView = (TextView) findViewById(R.id.textView1);
        txtView.setText("Welcome :" + message);

        // Spinner click listener
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("SE-A");
        categories.add("SE-B");
        categories.add("TE");
        categories.add("BE");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner2.setAdapter(dataAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
         Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void previous_records(View view) {
        Bundle basket= new Bundle();
        basket.putString("class_selected", item);
        basket.putString("tid", message);


        Intent intent = new Intent(this, takeAttendance.class);
        intent.putExtras(basket);
        startActivity(intent);
    }

    public void takeAttendanceButton(View view) {
        Bundle basket= new Bundle();
        basket.putString("class_selected", item);
        basket.putString("tid", message);


        Intent intent = new Intent(this, teacher_attendanceSheet.class);
        intent.putExtras(basket);
        startActivity(intent);
    }

    public void logoutTeacher(View view) {
        Intent logoutTeacher=new Intent(teacherlogin.this,login_activity.class);
        logoutTeacher.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutTeacher);
    }
}
