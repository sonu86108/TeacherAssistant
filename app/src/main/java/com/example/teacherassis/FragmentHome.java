package com.example.teacherassis;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    View view;
    Spinner spinnerCourse;
    List<String> listCourses;
    EditText selectDate;
    Button search;
    DatabaseHelper databaseHelper;
    DataHelper dataHelper;
    ArrayAdapter<String> spinnerArrayAdapter;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int day,month,year;


    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home, container, false);
        databaseHelper=new DatabaseHelper(getContext());
        dataHelper=DataHelper.getInstance(getContext());
        spinnerCourse=view.findViewById(R.id.idSpinnerCourse);
        selectDate=view.findViewById(R.id.idDate);
        search=view.findViewById(R.id.idBtnAttSearch);
        calendar=Calendar.getInstance();
        //Getting Date
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        year=calendar.get(Calendar.YEAR);
        //Course spinner
        listCourses=dataHelper.getCourses();
        spinnerArrayAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,listCourses);
        spinnerCourse.setAdapter(spinnerArrayAdapter);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog=new DatePickerDialog(getContext(),date,year,month,day);
                datePickerDialog.show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle date=new Bundle();
                date.putString("date",selectDate.getText().toString());
                date.putString("course",spinnerCourse.getSelectedItem().toString());
                FragmentShowAttendance fs=new FragmentShowAttendance();
                fs.setArguments(date);
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction().addToBackStack("c");
                ft.replace(R.id.home_layout,fs);
                ft.commit();
            }
        });




        return view;
    }

    DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            selectDate.setText(d+"-"+(m+1)+"-"+y);
            day=d;
            month=m;
            year=y;
        }
    };
}
