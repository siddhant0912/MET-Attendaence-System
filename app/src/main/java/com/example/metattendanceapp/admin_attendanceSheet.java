package com.example.metattendanceapp;
 import androidx.appcompat.app.AppCompatActivity;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.ArrayAdapter;
 import android.widget.EditText;
 import android.widget.ListView;
 import android.widget.Spinner;
 import android.widget.Toast;
 import android.os.Bundle;
 import android.widget.Toolbar;

 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;

 import java.util.ArrayList;

public class admin_attendanceSheet extends AppCompatActivity {

    ListView listView;
    Spinner class_name;
    String classes;
    EditText date;
    ArrayList<String> Userlist = new ArrayList<>();
    ArrayList<String> Studentlist = new ArrayList<String>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    DatabaseReference dbStudent;
    String required_date;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance_sheet);
        mToolbar=findViewById(R.id.ftoolbar);
        mToolbar.setTitle("Admin Dashboard : "+"("+date+")");
        listView = findViewById(R.id.list);
        class_name = findViewById(R.id.spinner5);
        date = (EditText) findViewById(R.id.date);

        classes = class_name.getSelectedItem().toString();



    }

    public void display_list(final ArrayList<String> userlist) {
        Studentlist.clear();
        required_date = date.getText().toString();
        dbAttendance = ref.child("attendance");
        dbAttendance.child(required_date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Studentlist.add("    SID                    "+"p1  "+"p2  "+"p3  "+"p4  "+"p5  "+"p6  "+"p7  "+"p8");
                for (Object sid : userlist) {


                    String p1=dataSnapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p2=dataSnapshot.child(sid.toString()).child("p2").getValue().toString().substring(0,1);
                    String p3=dataSnapshot.child(sid.toString()).child("p3").getValue().toString().substring(0,1);
                    String p4=dataSnapshot.child(sid.toString()).child("p4").getValue().toString().substring(0,1);
                    String p5=dataSnapshot.child(sid.toString()).child("p5").getValue().toString().substring(0,1);
                    String p6=dataSnapshot.child(sid.toString()).child("p6").getValue().toString().substring(0,1);
                    String p7=dataSnapshot.child(sid.toString()).child("p7").getValue().toString().substring(0,1);
                    String p8=dataSnapshot.child(sid.toString()).child("p8").getValue().toString().substring(0,1);
                    Studentlist.add(dataSnapshot.child(sid.toString()).getKey().toString()+"   "+p1+"    "+p2+"    "+p3+"    "+p4+"    "+p5+"    "+p6+"    "+p7+"    "+p8); //add result into array list
                }

                list(Studentlist);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }

        });




    }




    public void viewlist(View v){

        Userlist.clear();
        dbStudent = ref.child("Student");
        dbStudent.orderByChild("classes").equalTo(class_name.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(dsp.child("sid").getValue().toString()); //add result into array list
                }
                display_list(Userlist);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }

        });




    }
    public void list(ArrayList<String> studentlist){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, studentlist);

        listView.setAdapter(adapter);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
