package com.example.teacherassis;

import android.app.Dialog;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
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

import java.util.ArrayList;
import java.util.LinkedList;

public class Admin_Course_Activity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    String selected_list_item;
    TextView course;
    EditText enterCourse;
    FloatingActionButton add_course;
    Button save_course,edit_course;
    ListView course_list_view;
    Spinner select_teacher,selectTeacherEdit;
    LinkedList<String > course_list,teacher_list;
    ArrayAdapter<String> courseAdapter,teacherAdapter;
    Dialog addCourseDialog,editCourseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__course);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background3));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage Courses");
        databaseHelper=new DatabaseHelper(getApplicationContext());
        course_list=new LinkedList<>();
        teacher_list=new LinkedList<>();
        clearTeacherList();
        add_course=findViewById(R.id.id_add_button);
        course_list_view=findViewById(R.id.id_course_listView);

        //Add course dialog
        addCourseDialog=new Dialog(this);
        addCourseDialog.setContentView(R.layout.custom_dialog_add_course);
        save_course=addCourseDialog.findViewById(R.id.idButtonSaveCourse);
        enterCourse=addCourseDialog.findViewById(R.id.id_enter_course);
        addCourseDialog.setCanceledOnTouchOutside(true);
        select_teacher=addCourseDialog.findViewById(R.id.id_spinner_select_teacher);

        //Edit course dialog
        editCourseDialog=new Dialog(this);
        editCourseDialog.setContentView(R.layout.custom_dialog_edit_course);
        editCourseDialog.setCanceledOnTouchOutside(true);
        course=editCourseDialog.findViewById(R.id.id_text_course);
        edit_course=editCourseDialog.findViewById(R.id.idButtonSaveCourseEdit);
        selectTeacherEdit=editCourseDialog.findViewById(R.id.id_spinner_select_new_teacher);
        //Default value for



        //set courses in listView
        this.getCourses();
        this.getTeachers();
        courseAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,course_list);
        course_list_view.setAdapter(courseAdapter);
        registerForContextMenu(course_list_view);

        //Set teachers in spineers
        teacherAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,teacher_list);
        add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourseDialog.show();
                select_teacher.setAdapter(teacherAdapter);

            }
        });
         //Dialog add course button listener for save button
        save_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedTeacher;
                if(select_teacher.getSelectedItem().toString().equalsIgnoreCase("Select Teacher")){
                    selectedTeacher="n/a";

                }else {
                    selectedTeacher=select_teacher.getSelectedItem().toString();
                }
                if(enterCourse.getText().toString().trim().isEmpty()){
                    enterCourse.setError("Enter Course Name!");
                }else if(courseValidate())
                {
                    enterCourse.setError("Course Already Exists!");

                }else if(databaseHelper.insertData(DatabaseHelper.COURSE_TABLE_NAME, DatabaseHelper.COURSE_NAME,DatabaseHelper.COURSE_TEACHER,enterCourse.getText().toString().trim(),selectedTeacher))
                {
                    enterCourse.setText("");
                    course_list.clear();
                    getCourses();
                    courseAdapter.notifyDataSetChanged();
                    addCourseDialog.dismiss();
                }else{
                    enterCourse.setError("Course Already Exists");
                }

            }
        });

        //Dialog edit course button listener for save button
        edit_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCourse();
            }
        });
    }

    //implementing method for click event on app bar navigation button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
     //Context menu inflation
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_course_admin,menu);
    }

    //context menu item click listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Getting instance of Selected list item adapter view
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        selected_list_item= course_list_view.getItemAtPosition(menuInfo.position).toString();
        switch (item.getItemId()){
            case R.id.idContextMenuDeleteCourseAdmin:
                    if(databaseHelper.deleteData(DatabaseHelper.COURSE_TABLE_NAME,DatabaseHelper.COURSE_NAME,selected_list_item)>0){
                        course_list.clear();
                        getCourses();
                        courseAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show();
                    }
                    break;
            case R.id.idContextMenuEditCourseAdmin:


               course.setText(selected_list_item);
                selectTeacherEdit.setAdapter(teacherAdapter);
                editCourseDialog.show();
                break;

        }
        return true;

    }

    //Retriving courses from database and show them in list view
    public void getCourses()
    {


        Cursor cursor = databaseHelper.retrieveData(DatabaseHelper.COURSE_TABLE_NAME,DatabaseHelper.COURSE_NAME);
        if (cursor.moveToFirst())
        {
            do{
                String course=cursor.getString(0);
                 course_list.add(course);
            }while (cursor.moveToNext());
        }
        cursor.close();

    }
   //Getting teacher list  data
    public void getTeachers()
    {
        Cursor cursor=databaseHelper.retrieveData(DatabaseHelper.TEACHER_TABLE_NAME,DatabaseHelper.T_NAME);
        if(cursor.moveToFirst())
        {
            do {
                teacher_list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    //Update course
    public void updateCourse()
    {

        Boolean check= databaseHelper.updateData(DatabaseHelper.COURSE_TABLE_NAME,DatabaseHelper.COURSE_TEACHER,selectTeacherEdit.getSelectedItem().toString(),DatabaseHelper.COURSE_NAME,selected_list_item);
        if(check)
        {
            Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT).show();
            editCourseDialog.dismiss();
        }


    }



    //Returns true if course name already exists otherwise returns false
    private boolean courseValidate() {
        boolean check=false;
        getCourses();
        if(course_list.isEmpty()){
            check=false;
        }
        else {
            for (String c : course_list) {
                if (enterCourse.getText().toString().trim().equalsIgnoreCase(c)) {
                    check=true;
                } else {
                    check=false;

                }

            }
        }

      return check;
    }

    //clear course and set default selected item
    public void clearTeacherList()
    {
        teacher_list.clear();
        teacher_list.add("Select Teacher");
    }



}

