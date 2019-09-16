package com.example.teacherassis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Login_Activity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    DataHelper dataHelper;
    Spinner sel_category;
    Button login;
    TextView textAboveSpinner;
    EditText email_id,pass;
    String name,course;
    ArrayAdapter<String> adapter;
    Intent intent;
    private final String[] category={"Select Category","Admin","Teacher","Student"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        //Adding back navigation button in action bar
        /*assert getSupportActionBar() != null;  //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */
        getSupportActionBar().hide();
        dataHelper=DataHelper.getInstance(this);
        login=findViewById(R.id.login);
        sel_category=findViewById(R.id.spinner);
        textAboveSpinner=findViewById(R.id.textView5);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,category);
        sel_category.setAdapter(adapter);
        email_id=findViewById(R.id.teacherID);
        pass=findViewById(R.id.pass);
        intent=new Intent(this,MainActivity.class);
        //Session Management
        pref=getSharedPreferences("user_details",MODE_PRIVATE);
        editor=pref.edit();
        if(pref.contains("email_id") && pref.contains("password") && pref.contains("category"))
        {
            /*
             intent.putExtra("name",pref.getString("name",null));
            intent.putExtra("category",pref.getString("email_id",null));
            intent.putExtra("email_id",pref.getString("password",null));
            intent.putExtra("pass",pref.getString("category",null));
            */
            startActivity(intent);
            finish();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        //Username and password validation
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(email_id.getText().toString().isEmpty()){
                   email_id.setError("Enter Email id");
               }else if(pass.getText().toString().isEmpty()){
                   pass.setError("Enter password");
               }else if(sel_category.getSelectedItem().toString().equalsIgnoreCase("Select Category")){
                   textAboveSpinner.setError("Select Category");
                   textAboveSpinner.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
               }else if(adminValidate()){
                   editor.putString("email_id",email_id.getText().toString());
                   editor.putString("password",pass.getText().toString());
                   editor.putString("category",sel_category.getSelectedItem().toString());
                   editor.commit();
                   startActivity(intent);
                   finish();
               }else if(teacherValidate()){
                   editor.putString("name",name);
                   editor.putString("course",course);
                   editor.putString("email_id",email_id.getText().toString());
                   editor.putString("password",pass.getText().toString());
                   editor.putString("category",sel_category.getSelectedItem().toString());
                   editor.commit();
                   startActivity(intent);
                   finish();

               }else if(studentValidate()){
                   editor.putString("name",name);
                   editor.putString("course",course);
                   editor.putString("email_id",email_id.getText().toString());
                   editor.putString("password",pass.getText().toString());
                   editor.putString("category",sel_category.getSelectedItem().toString());
                   editor.commit();
                   startActivity(intent);
                   finish();

               }else {
                   email_id.setError("Wrong login Credentials");
                   pass.setError("Wrong login Credentials");
               }

            }
        });


    }

    private boolean adminValidate(){
        if(sel_category.getSelectedItem().toString().equalsIgnoreCase(category[1]) && email_id.getText().toString().equalsIgnoreCase("admin") && pass.getText().toString().equalsIgnoreCase("admin"))
            return  true;
        else
            return false;
    }

    private boolean teacherValidate() {
        Cursor cursor = dataHelper.retrieveData(DatabaseHelper.TEACHER_TABLE_NAME, DatabaseHelper.T_NAME,DatabaseHelper.T_COURSE, DatabaseHelper.T_EMAIL,email_id.getText().toString().trim().toLowerCase(), DatabaseHelper.T_PASS, pass.getText().toString().trim().toLowerCase());
        if (sel_category.getSelectedItem().toString().equalsIgnoreCase("teacher") && cursor.moveToFirst()) {
            name=cursor.getString(0);
            course=cursor.getString(1);
            cursor.close();
            return true;
        } else {
            return false;

        }
    }


    private boolean studentValidate() {
        Cursor cursor = dataHelper.retrieveData(DatabaseHelper.STUDENT_TABLE_NAME, DatabaseHelper.S_NAME,DatabaseHelper.S_COURSE, DatabaseHelper.S_EMAIL, email_id.getText().toString().trim().toLowerCase(), DatabaseHelper.S_PASS, pass.getText().toString().trim().toLowerCase());
        if (sel_category.getSelectedItem().toString().equalsIgnoreCase("student") && cursor.moveToFirst()) {
            name=cursor.getString(0);
            course=cursor.getString(1);
            cursor.close();
            return true;
        } else {
            return false;

        }
    }
}
