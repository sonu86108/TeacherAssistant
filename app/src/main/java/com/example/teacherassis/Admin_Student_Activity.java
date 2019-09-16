package com.example.teacherassis;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class Admin_Student_Activity extends AppCompatActivity {
     ListView listViewStudent;
     Button saveStudent;
     FloatingActionButton addStudent;
     EditText enterStudent,enterEmail,enterPass,enterRoll;
     TextView textOnAboveSpinner;
     List<String> listStudents,listCourses;
     ArrayAdapter<String > adapterListViewStudents,adapterSpinnerCourses;
     DatabaseHelper databaseHelper;
     Dialog dialogAddStudent;
     Spinner spinnerCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__student_);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background3));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage Students");
        listViewStudent=findViewById(R.id.idListVStudent);
        addStudent=findViewById(R.id.idBtnAddNStudent);
        listStudents=new LinkedList<>();
        listCourses=new LinkedList<>();
        clearCourses();
        databaseHelper=new DatabaseHelper(this);
        //GETING STUDENTS RECORDS TO LINKEDLIST
        getStudents();
        adapterListViewStudents=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listStudents);
        listViewStudent.setAdapter(adapterListViewStudents);

        //Dialog to Add a new Student
        dialogAddStudent=new Dialog(this);
        dialogAddStudent.setContentView(R.layout.custom_dialog_add_student);
        dialogAddStudent.setCanceledOnTouchOutside(true);
        enterStudent=dialogAddStudent.findViewById(R.id.idStudent);
        enterRoll=dialogAddStudent.findViewById(R.id.idRoll);
        enterEmail=dialogAddStudent.findViewById(R.id.idEmail);
        enterPass=dialogAddStudent.findViewById(R.id.idPassword);
        saveStudent=dialogAddStudent.findViewById(R.id.idBtnSaveStudent);
        textOnAboveSpinner=dialogAddStudent.findViewById(R.id.textOnAboveSpinner);
        spinnerCourses=dialogAddStudent.findViewById(R.id.idSpinnerCourseList);
        getCourses();
        adapterSpinnerCourses=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listCourses);
        spinnerCourses.setAdapter(adapterSpinnerCourses);


        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddStudent.show();
            }
        });

        saveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String student,rollno,email,pass;
                student=enterStudent.getText().toString().trim();
                rollno=enterRoll.getText().toString().trim();
                email=enterEmail.getText().toString().trim();
                pass=enterPass.getText().toString().trim();

                     if(student.isEmpty()){
                         enterStudent.setError("Enter Student Name");
                     }else if (rollno.isEmpty()){
                         enterRoll.setError("Enter Roll No");
                     }else if (email.isEmpty()){
                         enterEmail.setError("Enter email");
                     }else if (pass.isEmpty()){
                         enterPass.setError("Enter Password");
                     }else if (spinnerCourses.getSelectedItem().toString().equalsIgnoreCase("Select Course")){
                         textOnAboveSpinner.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                         textOnAboveSpinner.setError("Select A Course");
                     }else if(studentValidate()){
                         enterStudent.setError("Student Already Exists");
                     }else if(databaseHelper.insertData(DatabaseHelper.STUDENT_TABLE_NAME,DatabaseHelper.S_NAME,student,DatabaseHelper.S_ROLLNO,rollno,DatabaseHelper.S_EMAIL,email,DatabaseHelper.S_PASS,pass,DatabaseHelper.S_COURSE,spinnerCourses.getSelectedItem().toString())){
                         listStudents.clear();
                         getStudents();
                         adapterListViewStudents.notifyDataSetChanged();
                         enterStudent.setText("");
                         enterRoll.setText("");
                         enterEmail.setText("");
                         enterPass.setText("");
                         dialogAddStudent.dismiss();
                     } else {
                       Toast.makeText(Admin_Student_Activity.this, "Unknown Error...", Toast.LENGTH_SHORT).show();
                   }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //Get Students records
    public void getStudents(){
        Cursor cursor=databaseHelper.retrieveData(DatabaseHelper.STUDENT_TABLE_NAME,DatabaseHelper.S_NAME);
        if(cursor.moveToFirst()){
            do{
                listStudents.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void getCourses(){

        Cursor cursor=databaseHelper.retrieveData(DatabaseHelper.COURSE_TABLE_NAME,DatabaseHelper.COURSE_NAME);
        if(cursor.moveToFirst()){
            do{
                listCourses.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    private boolean studentValidate() {
        boolean check=false;
        if (listStudents.isEmpty()) {
            check=false;
        } else {
            for (String c : listStudents) {
                if (enterStudent.getText().toString().trim().equalsIgnoreCase(c)) {
                    check=true;
                } else {
                    check=false;

                }

            }
        }
        return check;
    }

    public void clearCourses()
    {
       listCourses.clear();
       listCourses.add("Select Course");
    }
}
