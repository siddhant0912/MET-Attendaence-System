package com.example.metattendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toolbar;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Objects;

public class admin extends AppCompatActivity {
    DatabaseReference ref;
    DatabaseReference dbStudent;
    DatabaseReference dbAttendance;
    DatabaseReference dbadmin;
    Toolbar mToolbar;
    private static long back_pressed;

    //ArrayList Studentlist = new ArrayList<>();

    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.admin);
        mToolbar=findViewById(R.id.ftoolbar);
        mToolbar.setTitle("Admin Dashboard : "+"("+date+")");
        mToolbar.setTitleTextColor(0xFFFFFFFF);
        ref = FirebaseDatabase.getInstance().getReference();
        dbStudent = ref.child("Student");
        dbAttendance = ref.child("attendance");
    }

    public void AddTeacherButton(View view) {
        Intent intent = new Intent(this, addteacher.class);
        startActivity(intent);
    }

    public void AddStudentButton(View view) {
        Intent intent = new Intent(this, addstudent.class);
        startActivity(intent);
    }


    public void CreateAttendance(View view) {

        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot  dataSnapshot) {
                String sid,P1="-",P2="-",P3="-",P4="-",P5="-",P6="-",P7="-",P8="-";
                Attendance_sheet a = new Attendance_sheet(P1,P2,P3,P4,P5,P6,P7,P8);

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    sid= Objects.requireNonNull(dsp.child("sid").getValue()).toString();
                    dbAttendance.child(date).child(sid).setValue(a);

                }
                Toast.makeText(getApplicationContext(),"successfully created "+date+" db", Toast.LENGTH_LONG).show();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void attendanceRecord(View view) {
        Intent intent = new Intent(this, admin_attendanceSheet.class);
        startActivity(intent);

    }

    public void changepassword(View view) {
        dbadmin=ref.child("Admin");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Type your new password");
        final LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.changepassword, null);
        final EditText password=add_menu_layout.findViewById(R.id.newpassword);
        alertDialog.setView(add_menu_layout);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(password.getText().toString()))
                {
                    dbadmin.child("Admin").setValue(password.getText().toString());
                    Toast.makeText(admin.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(admin.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                }



            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void logout(View view) {


        Intent logout=new Intent(admin.this,login_activity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logout);
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

