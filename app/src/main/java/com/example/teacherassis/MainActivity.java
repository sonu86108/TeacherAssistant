package com.example.teacherassis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

     DrawerLayout drawer;
     TextView name,email,course;
     View navHeader;
     NavigationView navigationView;
     MenuItem courses,teacher,students,takeAttendance;
     Menu nav_menu;
     SharedPreferences pref;
     DatabaseHelper databaseHelper;
     FragmentManager fm;
     FragmentTransaction ft;
    String email_id,password,category,nameText,courseText;

    public MainActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        //Changing Status Bar backgound color
        Window window=this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.holo_purple));
            */
          //Gettting drawer and navigation View references
         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
         //Getting navigation drawer menu items
        nav_menu=navigationView.getMenu();
        courses=nav_menu.findItem(R.id.nav_admin_Courses);
        teacher=nav_menu.findItem(R.id.nav_admin_teachers);
        students=nav_menu.findItem(R.id.nav_admin_students);
        takeAttendance=nav_menu.findItem(R.id.nav_teacher_takeAttendance);

        //Getting navigation header items
        navHeader=navigationView.getHeaderView(0);
        name=navHeader.findViewById(R.id.loginText);
        email=navHeader.findViewById(R.id.emailText);
        course=navHeader.findViewById(R.id.idCourseText);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        databaseHelper=new DatabaseHelper(this);

        //Getting SharedPreferences reference and editor and values
         pref=getSharedPreferences("user_details",MODE_PRIVATE);
         nameText=pref.getString("name",null);
        category=pref.getString("category",null);
        email_id=pref.getString("email_id",null);
        password=pref.getString("password",null);
        courseText=pref.getString("course",null);

        //database creation
        //this.databaseHelper();



        //Decision making for admin,teacher and student moduls
        if(category.contentEquals("Admin"))
        {
            this.admin();
        }else if (category.contentEquals("Teacher"))
        {
           this.teacher();
        }else
        {
             this.student();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void admin()
    {
        name.setText("Sonu Yadav");
        email.setText("sonu006191@gmail.com");
        course.setText(" ");
        //Adding Home Fragment
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.add(R.id.home_layout,new FragmentHome());
        ft.commit();
        //takeAttendance.setVisible(false);
        //setting listener to the navigation menu items
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //checking if nav menu item is checked state or not if not then make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                switch (menuItem.getItemId())
                {
                    case R.id.nav_admin_Courses:
                        Intent intent=new Intent(MainActivity.this,Admin_Course_Activity.class);

                        startActivity(intent);
                        break;
                    case R.id.nav_admin_teachers:
                        Intent intent1=new Intent(MainActivity.this,Admin_Teacher_Activity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_admin_students:
                        Intent intent2=new Intent(MainActivity.this,Admin_Student_Activity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_logout:
                        SharedPreferences pref=getSharedPreferences("user_details",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent3 =new Intent(MainActivity.this,Login_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_home:
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_teacher_takeAttendance:
                        Intent intentt=new Intent(MainActivity.this,TeacherTakeAttendanceActivity.class);
                        startActivity(intentt);
                        drawer.closeDrawers();
                        break;
                }
                return false;
            }
        });
    }

    private void teacher()
    {
        name.setText(nameText);
        email.setText(email_id);
        course.setText(courseText);
        //Adding Home Fragment
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.add(R.id.home_layout,new FragmentHome());
        ft.commit();
          courses.setVisible(false);
          teacher.setVisible(false);
          students.setVisible(false);
        //Setting listener on logout item to logout and start login activity
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //checking if the itme is in checked state or not ,if not  make it checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                drawer.closeDrawers();
                //check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.nav_teacher_takeAttendance:
                        Intent intent=new Intent(MainActivity.this,TeacherTakeAttendanceActivity.class);
                        intent.putExtra("course",courseText);
                        startActivity(intent);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_logout:
                        SharedPreferences pref=getSharedPreferences("user_details",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent3 =new Intent(MainActivity.this,Login_Activity.class);
                        startActivity(intent3);
                        break;

                }
                return true;
            }
        });
    }

    private void student() {

        name.setText(nameText);
        email.setText(email_id);
        course.setText(courseText);
        //Adding Home Fragment
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.add(R.id.home_layout,new FragmentHome());
        ft.commit();

        courses.setVisible(false);
        teacher.setVisible(false);
        students.setVisible(false);
        takeAttendance.setVisible(false);
        //Setting listener on logout item to logout and start login activity
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //checking if the itme is in checked state or not ,if not  make it checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                drawer.closeDrawers();
                //check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.nav_logout:
                        SharedPreferences pref=getSharedPreferences("user_details",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent3 =new Intent(MainActivity.this,Login_Activity.class);
                        startActivity(intent3);
                        break;

                }
                return true;
            }
        });

    }


}

