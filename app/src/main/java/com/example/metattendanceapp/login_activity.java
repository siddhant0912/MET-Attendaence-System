package com.example.metattendanceapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class login_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String item;
    String userid,pass;
    private FirebaseAuth mAuth;
    Bundle basket;



    ProgressDialog mDialog;
    public static long back_pressed;
    DatabaseReference ref;
    EditText username;
    EditText password;
    String dbpassword;
    String dpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        Spinner spinner = findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(this);


        List<String> categories = new ArrayList<>();
        categories.add("Admin");
        categories.add("Teacher");
        categories.add("Student");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spin, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void login(View view) {
        userid = username.getText().toString();
        pass = password.getText().toString();
        mDialog=new ProgressDialog(this);
        mDialog.setMessage("Please Wait..."+userid);
        mDialog.setTitle("Loading");
        mDialog.show();
        basket = new Bundle();
        basket.putString("message", userid);

        ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbuser = ref.child(item).child(userid);
        dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dbchild = null;
                try {
                    if (item == "Admin") {
                        mDialog.dismiss();
                        dbpassword = dataSnapshot.getValue(String.class);
                        if(dbpassword == null){
                            String apass = Encrypt.encrypt(pass);
                            ref.child("Admin").child("Admin").setValue(apass);
                            Toast.makeText(getApplicationContext(), "Password For Admin Created", Toast.LENGTH_LONG).show();
                        }else {
                            String ap = Decrypt.decrypt(dbpassword);
                            verify(ap);
                        }
                    } else {
                        mDialog.dismiss();
                        if (item == "Student") {
                            dbchild = "spass";
                        }
                        if (item == "Teacher") {
                            dbchild = "tpass";
                        }

                        dbpassword = dataSnapshot.child( dbchild).getValue(String.class);
                        dpass =Decrypt.decrypt(dbpassword);
                        verify(dpass);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(login_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void verify(String dbpass){
        if(userid.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Username cannot be empty", Toast.LENGTH_LONG).show();
        }
        else
        if (item == "Teacher" && pass.equalsIgnoreCase(dbpass)) {

            mDialog.dismiss();
            Intent intent = new Intent(this, teacherlogin.class);
            intent.putExtras(basket);
            startActivity(intent);

        }

        else if (item == "Admin" && pass.equalsIgnoreCase(dbpass) ) {
            mDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Log in successful",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, admin.class);
            intent.putExtras(basket);
            startActivity(intent);


        }
        else if (item == "Student" && pass.equalsIgnoreCase(dbpass)) {
            mDialog.dismiss();
            Intent intent = new Intent(this, studentlogin.class);
            intent.putExtras(basket);
            startActivity(intent);
        }
        else if(! pass.equalsIgnoreCase(dbpass)){
            Toast.makeText(getApplicationContext(),"password:" +dbpass, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"UserId or Password is Incorrect", Toast.LENGTH_LONG).show();

        }
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



