package com.example.teacherassis;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

public class TeacherTakeAttendanceActivity extends AppCompatActivity {

    FloatingActionButton takeAttendance;
     TextView courseName;
     EditText selectDate;
     Calendar calendar;
     int day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_take_attendance);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background3));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Take Attendance");
        takeAttendance=findViewById(R.id.idBtnTakeAttendance);
        courseName=findViewById(R.id.idTextViewCourseName);
        selectDate=findViewById(R.id.idSelectDate);
        calendar=Calendar.getInstance();
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        year=calendar.get(Calendar.YEAR);
        courseName.setText(getIntent().getStringExtra("course"));

        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectDate.getText().toString().isEmpty()){
                    selectDate.setError("Select date first");
                }else {
                    Intent intent=new Intent(TeacherTakeAttendanceActivity.this,TakeAttendanceActivity.class);
                    intent.putExtra("date",selectDate.getText().toString());
                    startActivity(intent);
                }

            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TeacherTakeAttendanceActivity.this,date,year,month,day).show();
                selectDate.setError(null);
            }
        });
    }

    DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            //m+1 because array list starts from zero intdex
            selectDate.setText(d+"-"+(m+1)+"-"+y);
            day=d;
            month=m;
            year=y;
        }
    };


    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
