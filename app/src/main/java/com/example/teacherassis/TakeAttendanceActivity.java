package com.example.teacherassis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class TakeAttendanceActivity extends AppCompatActivity {

    CustomArrayAdapter customAdapter;
    ListView listViewTakeAtten;
    Button cancel,save;
    DatabaseHelper databaseHelper;
    List<String> listStudents,listRollNO,listDates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background3));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper=new DatabaseHelper(this);
        listStudents=new LinkedList<>();
        listRollNO=new LinkedList<>();
        listDates=new LinkedList<>();
        cancel=findViewById(R.id.idBtnCancelAtten);
        save=findViewById(R.id.idBtnSaveAtte);
        listViewTakeAtten=findViewById(R.id.idlistViewTakeAttendance);
        getStudents();
        getStudentRollNo();
        customAdapter=new CustomArrayAdapter(getApplicationContext(),listStudents,listRollNO);
        listViewTakeAtten.setAdapter(customAdapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
              AlertDialog.Builder alertDilog=new AlertDialog.Builder(TakeAttendanceActivity.this);
              alertDilog.setCancelable(true);
              alertDilog.setMessage("Do you want to save data!");
              alertDilog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      if(attendanceValidate()){
                          Snackbar.make(findViewById(R.id.idLayout),"Duplicate date not allowed  select different date!",Snackbar.LENGTH_LONG).show();
                      }else {
                          setAttendanceRecord();
                      }
                  }
              });
              alertDilog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog=new AlertDialog.Builder(TakeAttendanceActivity.this);
                alertDialog.setMessage("Do you want to quit!");
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(TakeAttendanceActivity.this,TeacherTakeAttendanceActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                dialogInterface.dismiss();
                            }
                        });
                    }
                });
                alertDialog.show();
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

    //Get Roll No of Students
    public void getStudentRollNo(){
        Cursor cursor=databaseHelper.retrieveData(DatabaseHelper.STUDENT_TABLE_NAME,DatabaseHelper.S_ROLLNO);
        if(cursor.moveToFirst()){
            do{
                listRollNO.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    //validate Attendance to avoid duplicate date attendance
    public boolean attendanceValidate()
    {
        boolean check=false;
        getDateList();
        if(listDates.isEmpty()){
            check=false;
        }else {
            for(String c:listDates){
                if(this.getIntent().getStringExtra("date").equalsIgnoreCase(c)){
                    check =true;
                }else {
                    check=false;
                }
            }
        }
        return check;
    }

    //Dates from Database
    public void getDateList(){
        Cursor cursor=databaseHelper.retrieveData(DatabaseHelper.ATTENDANCE_TABLE_NAME,DatabaseHelper.A_DATE);
        if(cursor.moveToFirst()){
            do{
                listDates.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void setAttendanceRecord()
    {
        int i=0;
        while (i<listStudents.size()){
            String tn=databaseHelper.ATTENDANCE_TABLE_NAME;
            String c1=databaseHelper.A_S_NAME;
            String c2=databaseHelper.A_C_NAME;
            String c3=databaseHelper.A_DATE;
            String c4=databaseHelper.A_STATUS;
            String c1v=listStudents.get(i);
            String c2v="course_name";
            String c3v=this.getIntent().getStringExtra("date");
            String c4v;
            try {
                c4v=customAdapter.listAttenanceStatus.get(i);
            } catch (IndexOutOfBoundsException e){
                Snackbar.make(findViewById(R.id.idLayout),"Make sure you checked a/status of all Students",Snackbar.LENGTH_LONG).show();
                break;
            }

            if(databaseHelper.insertData(tn,c1,c1v,c2,c2v,c3,c3v,c4,c4v)){
                Snackbar.make(findViewById(R.id.idLayout),"Record Saved Successfully",Snackbar.LENGTH_LONG).show();
            }else {
                Snackbar.make(findViewById(R.id.idLayout),"Unknow Error",Snackbar.LENGTH_LONG).show();
            }
            i++;
        }
    }

}



/*-----------------Custom Adatper class -----------------*/
class CustomArrayAdapter extends ArrayAdapter<String > {
    List<String > name;
    List<String > rollNo;
    Context context;
    List<String> listAttenanceStatus;
    RadioGroup radioGroup;
    public CustomArrayAdapter(Context context,List<String> name,List<String> rollNo) {
        super(context,R.layout.custom_row_take_attendance,name);
        this.context=context;
        this.name=name;
        this.rollNo=rollNo;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        listAttenanceStatus=new LinkedList<>();
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.custom_row_take_attendance,null,true);
        TextView s_name=convertView.findViewById(R.id.idTextViewName);
        TextView roll_no=convertView.findViewById(R.id.idTextViewRollNo);
        radioGroup=convertView.findViewById(R.id.idRadioGroup);
        s_name.setText(name.get(position));
        roll_no.setText(rollNo.get(position));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String as=null;
                switch (i){
                    case R.id.idRBP:
                        as="P";
                        break;
                    case R.id.idRBA:
                        as="A";
                }
                try {
                    listAttenanceStatus.add(position,as);
                }catch (IndexOutOfBoundsException e){
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
                }

            }
        });

        return convertView;
    }



}
