package com.example.teacherassis;

import android.content.ContentValues;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{

    //Database schema
    public static final int DATABASE_VERSTION=1;
    public static final String DATABASE_NAME="teacher_assis.db";


    //COURSE TABLE DEFINITION
    public static final String COURSE_TABLE_NAME="courses_table";
    public static final String ID="id";
    public static final String COURSE_NAME="course_name";
    public static final String COURSE_TEACHER="course_teacher";

    //TEACHER TABLE DEFINITION
    public static final String TEACHER_TABLE_NAME="teachers_table";
    public static final String T_Id="id";
    public static final String T_NAME="teacher_name";
    public static final String T_EMAIL="teacher_email";
    public static final String T_COURSE="teacher_course";
    public static final String T_PASS="teacher_pass";
    //public static final String T_COURSE="teacher_course";

    //STUDENT TABLE DEFINITION
    public static final String STUDENT_TABLE_NAME="student_table";
    public static final String S_ID="id";
    public static final String S_NAME="student_name";
    public static final String S_ROLLNO="student_roll";
    public static final String S_EMAIL="student_email";
    public static final String S_PASS="student_pass";
    public static final String S_COURSE="student_course";

    //ATTENDANCE TABLE DEFINITON
    public static final String ATTENDANCE_TABLE_NAME="attendance_table";
    public static final String A_ID="id";
    public static final String A_S_NAME="a_s_name";
    public static final String A_S_ROLL ="a_s_roolno";
    public static final String A_C_NAME="a_c_name";
    public static final String A_DATE="a_date";
    public static final String A_STATUS="a_status";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSTION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
       String create_course_table="CREATE TABLE "+COURSE_TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COURSE_NAME+" TEXT UNIQUE ,"+COURSE_TEACHER+" TEXT )";
       String create_teacher_table="CREATE TABLE "+TEACHER_TABLE_NAME+" ("+T_Id+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+T_NAME+" TEXT ,"+T_EMAIL+" TEXT UNIQUE,"+T_COURSE+" TEXT, "+T_PASS+" TEXT )";
       String create_student_table="CREATE TABLE "+STUDENT_TABLE_NAME+" ("+S_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+S_NAME+" TEXT ,"+S_ROLLNO+" TEXT ,"+S_EMAIL+" TEXT UNIQUE,"+S_PASS+" TEXT,"+S_COURSE+" TEXT)";
       String create_attendance_table="CREATE TABLE "+ATTENDANCE_TABLE_NAME+" ("+A_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+A_S_NAME+" TEXT ,"+ A_S_ROLL +" TEXT,"+A_C_NAME+" TEXT ,"+A_DATE+" TEXT,"+A_STATUS+" TEXT)";
       sqLiteDatabase.execSQL(create_course_table);
       sqLiteDatabase.execSQL(create_teacher_table);
       sqLiteDatabase.execSQL(create_student_table);
       sqLiteDatabase.execSQL(create_attendance_table);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+COURSE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TEACHER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+STUDENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ATTENDANCE_TABLE_NAME);
        onCreate(sqLiteDatabase);

    }


    //Insert data in single column
    public boolean insertData(String table_name,String column_name,String column_value)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(column_name,column_value);
        long result=db.insert(table_name,null,contentValues);
        db.close();
        //To check wheather data is inserted in databse
        if(result== -1){
            return false;
        }else {
            return true;
        }

    }

    //Insert data in two column
    public boolean insertData(String table_name,String colum_1,String column_2,String column_1_value,String column_2_value)
    {
        long result=-1;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(colum_1,column_1_value);
        contentValues.put(column_2,column_2_value);
        try{
            result=db.insert(table_name,null,contentValues);
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
        }

        db.close();

        if(result==-1)
           return false;
       else
           return true;

    }

    //Insert data in three column
    public boolean insertData(String table_name,String c1,String c1v,String c2,String c2v,String c3,String c3v){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(c1,c1v);
        contentValues.put(c2,c2v);
        contentValues.put(c3,c3v);
        long result=db.insert(table_name,null,contentValues);
        db.close();
        //To check wheather data is inserted in databse
        if(result== -1){
            return false;
        }else {
            return true;
        }
    }

    //insert Data in four column
    public boolean insertData(String table_name,String c1,String c1v,String c2,String c2v,String c3,String c3v,String c4,String c4v){
        long result=-1;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(c1,c1v);
        contentValues.put(c2,c2v);
        contentValues.put(c3,c3v);
        contentValues.put(c4,c4v);
        try {
            result=db.insert(table_name,null,contentValues);
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
        }

        db.close();
        //To check wheather data is inserted in databse
        if(result== -1){
            return false;
        }else {
            return true;
        }
    }

    //Insert Data in five column
    public boolean insertData(String table_name,String c1,String c1v,String c2,String c2v,String c3,String c3v,String c4,String c4v,String c5,String c5v){
        long result=-1;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(c1,c1v);
        contentValues.put(c2,c2v);
        contentValues.put(c3,c3v);
        contentValues.put(c4,c4v);
        contentValues.put(c5,c5v);
        try {
            result=db.insert(table_name,null,contentValues);
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
        }

        db.close();
        //To check wheather data is inserted in databse
        if(result== -1){
            return false;
        }else {
            return true;
        }
    }


    //get  single  column data
    public Cursor retrieveData(String table_name,String colum_name)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT "+colum_name+" FROM "+table_name;
        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    //get two columnData
    public Cursor retrieveData(String table_name,String c1,String conditon_c1,String contion_c1v,String contion_c2,String contion_c2v)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT "+c1+" FROM "+table_name+" WHERE "+conditon_c1+"=? AND "+contion_c2+"=?",new String[]{contion_c1v,contion_c2v});
        return cursor;
    }
     //check and access two column
    public Cursor retrieveData(String table_name,String c1,String c2,String conditon_c1,String contion_c1v,String contion_c2,String contion_c2v)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT "+c1+","+c2+" FROM "+table_name+" WHERE "+conditon_c1+"=? AND "+contion_c2+"=?",new String[]{contion_c1v,contion_c2v});
        return cursor;
    }

    //Update data
    public boolean updateData(String table_name,String column2change,String column2value,String colum_c,String column_c_value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column2change, column2value);
        long result = db.update(table_name, contentValues, colum_c + " =?", new String[]{column_c_value});
        if (result == -1)
        {
            return false;
        }else {
            return true;
        }

    }

    //Delete data
    public int deleteData(String table,String colum,String columValue)
    {
        SQLiteDatabase db=getWritableDatabase();
        int i=db.delete(table,colum+"=?",new String[]{columValue});
        return i;
    }

}


