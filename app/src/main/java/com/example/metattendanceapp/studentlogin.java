package com.example.metattendanceapp;

import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.appcompat.app.AppCompatActivity;

public class studentlogin extends AppCompatActivity {

    String message;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    Toolbar mToolbar;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student");
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        message = bundle.getString("message");
        mToolbar =findViewById(R.id.ftoolbar);
        mToolbar.setTitle("Student's| Dashboard |" + "(" + date + ")");
        TextView txtView = findViewById(R.id.textView1);
        txtView.setText("Welcome :" + message);


    }

    public void view_attendance(View view) {

        Bundle basket = new Bundle();
        basket.putString("sid", message);


        Intent intent = new Intent(this, student_attendance_sheet.class);
        intent.putExtras(basket);
        startActivity(intent);

    }

    public void logoutStudent(View view) {
        Intent logoutStudent=new Intent(studentlogin.this,login_activity.class);
        logoutStudent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutStudent);
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

    public void viewupload(View view) {
        Bundle basket = new Bundle();
        basket.putString("sid", message);

        Intent intent = new Intent(this, NotesDownload.class);
        intent.putExtras(basket);
        startActivity(intent);

    }
}
