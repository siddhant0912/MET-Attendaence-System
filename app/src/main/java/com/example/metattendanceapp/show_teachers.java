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

public class show_teachers extends ArrayAdapter<Teacher> {

    private Activity context;
    private List<Teacher> teacherList;

    public show_teachers(Activity context, List<Teacher> teacherList){
        super(context, R.layout.activity_show_teachers,teacherList);
        this.context=context;
        this.teacherList=teacherList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_show_teachers,null,true);
        TextView textviewtid = (TextView) listViewItem.findViewById(R.id.textViewtid);
        TextView textViewtname = (TextView)  listViewItem.findViewById(R.id.textViewstname);
        TextView textviewstclass = (TextView) listViewItem.findViewById(R.id.textViewstclasses);
        TextView textviewsubject = (TextView) listViewItem.findViewById(R.id.textViewsubject);
        TextView textviewpassowrd = (TextView)listViewItem.findViewById(R.id.textViewpassword);

        Teacher teacher = teacherList.get(position);
        textviewtid.setText(teacher.getTid());
        textViewtname.setText(teacher.getTname());
        textviewstclass.setText(teacher.getClasses());
        textviewpassowrd.setText(teacher.getdectpass());
        textviewsubject.setText(teacher.getSubject());

        return   listViewItem;
    }
}
