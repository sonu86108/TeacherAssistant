package com.example.teacherassis;

import android.content.Context;
import android.database.Cursor;

import java.util.LinkedList;
import java.util.List;

//---------------class DataHelper-------//
class DataHelper extends DatabaseHelper{
    private static DataHelper instanc=null;
    private DataHelper(Context context) {
        super(context);
    }

    public static DataHelper getInstance(Context context){
        if(instanc==null){
            instanc=new DataHelper(context);
        }
        return instanc;
    }

    public List<String> getCourses(){
        List<String> courseList=new LinkedList<>();
        courseList.add("Select Course");
        Cursor cursor=retrieveData(COURSE_TABLE_NAME,COURSE_NAME);
        if(cursor.moveToFirst()){
            do{
                courseList.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return courseList;
    }


}
