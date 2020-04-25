package com.example.metattendanceapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
public class show_students extends ArrayAdapter<Student> {

    private Activity context;
    private List<Student> studentlist;

    show_students(Activity context, List<Student> studentlist){
        super(context, R.layout.activity_show_students,studentlist);
        this.context = context;
        this.studentlist =studentlist;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_show_students,null,true);
        TextView textviewsid = (TextView) listViewItem.findViewById(R.id.textViewsid);
        TextView textviewsname = (TextView) listViewItem.findViewById(R.id.textViewsname);
        TextView textviewsclass = (TextView) listViewItem.findViewById(R.id.textViewsclass);
        TextView textViewspass = (TextView)listViewItem.findViewById(R.id.textViewspass);

        Student student = studentlist.get(position);
        textviewsid.setText(student.getSid());
        textviewsname.setText(student.getSname());
        textViewspass.setText(student.getDecpass());
        textviewsclass.setText(student.getClasses());

        return   listViewItem;
    }
}
