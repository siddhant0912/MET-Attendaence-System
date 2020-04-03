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
        Spinner spinner2 = findViewById(R.id.spinner2);

        Bundle bundle1 = getIntent().getExtras();
        assert bundle1 != null;
        message = bundle1.getString("message");
        mToolbar=findViewById(R.id.ftoolbar);
        mToolbar.setTitle("Teacher's| Dashboard |" + "(" + date + ")");

        TextView txtView =  findViewById(R.id.textView1);
        txtView.setText("Welcome :" + message);
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        List<String> categories = new ArrayList<String>();
        categories.add("SE-A");
        categories.add("SE-B");
        categories.add("TE");
        categories.add("BE");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
         Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }
    public void previous_records(View view) {
        Bundle basket= new Bundle();
        basket.putString("class_selected", item);
        basket.putString("tid", message);


        Intent intent = new Intent(this, teacher_attendanceSheet.class);
        intent.putExtras(basket);
        startActivity(intent);
    }

    public void takeAttendanceButton(View view) {
        Bundle basket= new Bundle();
        basket.putString("class_selected", item);
        basket.putString("tid", message);


        Intent intent = new Intent(this, takeAttendance.class);
        intent.putExtras(basket);
        startActivity(intent);
    }
    public void uploadNotesAc(View view){
        Bundle basket = new Bundle();
        basket.putString("class_selected",item);
        basket.putString("tid", message);

        Intent intent = new Intent(this, NotesUpload.class);
        intent.putExtras(basket);
        startActivity(intent);
    }

    public void logoutTeacher(View view) {
        Intent logoutTeacher=new Intent(teacherlogin.this,login_activity.class);
        logoutTeacher.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutTeacher);
    }
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            System.exit(0);
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
