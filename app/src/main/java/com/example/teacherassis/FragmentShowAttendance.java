package com.example.teacherassis;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentShowAttendance extends Fragment {

    View view;
    String date,course;
    ListView showAttendance;
    List<String > listStudent,listRollNo,listAStatus;
    DatabaseHelper databaseHelper;
    MyAdapter myAdapter;

    public FragmentShowAttendance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_fragment_show_attendance, container, false);
        showAttendance=view.findViewById(R.id.idListViewShowAtt);
        listStudent=new LinkedList<>();
        listRollNo=new LinkedList<>();
        listAStatus=new LinkedList<>();
        getStudents();
        getStudentRollNo();
        getStudentAStatus();
        Bundle date=this.getArguments();
        if(date!=null){
            this.date=date.getString("date");
            this.course=date.getString("course");
            Snackbar.make(getActivity().findViewById(R.id.home_layout),"date: "+this.date+"\nCourse: "+this.course,Snackbar.LENGTH_LONG).show();

        }

        myAdapter=new MyAdapter(getContext(),listStudent,listRollNo,listAStatus);
        showAttendance.setAdapter(myAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        databaseHelper=new DatabaseHelper(context);
    }

    //Get Students
    public void getStudents(){
        Cursor cursor=databaseHelper.retrieveData(DatabaseHelper.ATTENDANCE_TABLE_NAME,DatabaseHelper.A_S_NAME);
        if(cursor.moveToFirst()){
            do{
                listStudent.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    //get studetn roll no
    public void getStudentRollNo(){
        Cursor cursor=databaseHelper.retrieveData(DatabaseHelper.ATTENDANCE_TABLE_NAME,DatabaseHelper.A_S_ROLL);
        if(cursor.moveToFirst()){
            do{
                listRollNo.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    //get attendance status
    public void getStudentAStatus(){
        Cursor cursor=databaseHelper.retrieveData(DatabaseHelper.ATTENDANCE_TABLE_NAME,DatabaseHelper.A_STATUS);
        if(cursor.moveToFirst()){
            do{
                listAStatus.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }




}

class MyAdapter extends ArrayAdapter {

    List<String> studentName,rollNo,aStatus;

    public MyAdapter(Context context,List<String> studentName,List<String > rollNo,List<String > aStatus) {
        super(context,R.layout.custom_row_show_attendance,studentName );
        this.studentName=studentName;
        this.rollNo =rollNo;
        this.aStatus=aStatus;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.custom_row_show_attendance,null,true);
        TextView sName,rollno,astatus;
        sName=convertView.findViewById(R.id.idSName);
        rollno=convertView.findViewById(R.id.idRollNo);
        astatus=convertView.findViewById(R.id.idAStaus);
        sName.setText(studentName.get(position));
        rollno.setText(rollNo.get(position));
        astatus.setText(aStatus.get(position));
        return convertView;
    }
}