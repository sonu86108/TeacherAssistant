package com.example.teacherassis;

import android.app.Dialog;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class Admin_Teacher_Activity extends AppCompatActivity {

    EditText enterTeacher,enterEmail,enterPass;
    Button  saveTeacher;
    FloatingActionButton add_teacher_btn;
    ListView listView;
    LinkedList<String> teacherLickedList;
    DatabaseHelper databaseHelper;
    DataHelper dataHelper;
    Dialog addTeacherDialog;
    ArrayAdapter<String> listAdapter,spinnerAdapter;
    TextView textAboveSpinner;
    Spinner selectCourse;
    List<String> courselist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__teacher);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background3));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manager Teachers");

        //Add Teacher Dialog
        addTeacherDialog = new Dialog(this);
        addTeacherDialog.setContentView(R.layout.custom_dialog_add_teacher);
        enterTeacher = addTeacherDialog.findViewById(R.id.idStudent);
        enterEmail=addTeacherDialog.findViewById(R.id.idEmail);
        enterPass=addTeacherDialog.findViewById(R.id.idPassword);
        saveTeacher = addTeacherDialog.findViewById(R.id.idBtnSaveStudent);
        selectCourse=addTeacherDialog.findViewById(R.id.selectCourse);
        textAboveSpinner=addTeacherDialog.findViewById(R.id.textSelectCourseAbove);
        teacherLickedList = new LinkedList<>();

        databaseHelper = new DatabaseHelper(getApplicationContext());
        dataHelper=DataHelper.getInstance(this);
        add_teacher_btn = findViewById(R.id.id_add_teacher_btn);
        listView = findViewById(R.id.id_list_view);

        //set Courses to spinner
        courselist=dataHelper.getCourses();
        spinnerAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,courselist);
        selectCourse.setAdapter(spinnerAdapter);

        this.getTeachers();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teacherLickedList);
        listView.setAdapter(listAdapter);
        registerForContextMenu(listView);

        //On Add Teacher click
        add_teacher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTeacherDialog.show();
            }
        });

        //Save button click dialog add teacher
        saveTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterTeacher.getText().toString().isEmpty()) {
                    enterTeacher.setError("Enter Teacher name");
                }else if (enterEmail.getText().toString().isEmpty()){
                    enterEmail.setError("Enter email");
                }else if (enterPass.getText().toString().isEmpty()){
                    enterPass.setError("enter pass");
                }else if (selectCourse.getSelectedItem().toString().equalsIgnoreCase("Select Course")){
                    textAboveSpinner.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    textAboveSpinner.setError("Select A Course");
                }else if (validateTeacher()) {
                        enterTeacher.setError("Teacher Already Exists");
                }else if(databaseHelper.insertData(DatabaseHelper.TEACHER_TABLE_NAME,DatabaseHelper.T_NAME,enterTeacher.getText().toString().trim(),DatabaseHelper.T_EMAIL,enterEmail.getText().toString().trim(),DatabaseHelper.T_COURSE,selectCourse.getSelectedItem().toString(),DatabaseHelper.T_PASS,enterPass.getText().toString().trim())){
                    teacherLickedList.clear();
                    getTeachers();
                    listAdapter.notifyDataSetChanged();
                    enterTeacher.setText("");
                    enterEmail.setText("");
                    enterPass.setText("");
                    addTeacherDialog.dismiss();
                }else {
                    enterEmail.setError("Already Exitst");
                }
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_teacher_admin,menu);
        //AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.findItem(R.id.idContextMenuEditTeacher).setVisible(false);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String selectedTeache=listView.getItemAtPosition(info.position).toString();
        if(item.getItemId()==R.id.idContextMenuTeacherDelter){
            databaseHelper.deleteData(DatabaseHelper.TEACHER_TABLE_NAME,DatabaseHelper.T_NAME,selectedTeache);
            teacherLickedList.clear();
            getTeachers();
            listAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void getTeachers() {
        Cursor cursor = databaseHelper.retrieveData(DatabaseHelper.TEACHER_TABLE_NAME, DatabaseHelper.T_NAME);
        if (cursor.moveToFirst()) {
            do {
                teacherLickedList.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private boolean validateTeacher()
    {
      boolean check=false;
      if(teacherLickedList.isEmpty()){
            check=false;
        }else {
            for(String s:teacherLickedList){
                if(enterTeacher.getText().toString().trim().equalsIgnoreCase(s))
                {
                  check=true;
                }else {
                    check=false;
                }
            }
      }
      return check;
    }
}
