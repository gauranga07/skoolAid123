package com.example.kyle.uwitimemanagemeent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShowAndUpdateSchTask extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper myDb;
    TextView title;
    EditText startDate,endDate,startTime,endTime,note,loc,classes;
    Button update;
    Button start_T,start_D,end_T,end_D;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long sDate,sTime,eDate,eTime;
    String s = "";           //school title
    String l=" ";             //location
    String c=" ";            //class
    String n = "";          //note
    String date = "";
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_and_update_sch_task);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        title = (TextView)findViewById(R.id.general_title);
        startDate = (EditText)findViewById(R.id.task_startDate);
        endDate = (EditText)findViewById(R.id.task_endDate);
        startTime = (EditText)findViewById(R.id.task_startTime);
        endTime = (EditText)findViewById(R.id.task_endTime);
        loc=(EditText)findViewById(R.id.locationEditText) ;
        classes=(EditText)findViewById(R.id.classEditText);
        start_T= findViewById(R.id.Tstart);
        start_D= findViewById(R.id.Dstart);
        end_T= findViewById(R.id.Tend);
        end_D= findViewById(R.id.Dend);
        note = findViewById(R.id.task_note);
        update = (Button)findViewById(R.id.task_save);

        myDb=new DatabaseHelper(getApplicationContext());
        String posID=getIntent().getStringExtra("R");
        id=Integer.parseInt(posID);                               //id of the school item you want to update


        Cursor rGeneral=myDb.getSchoolDataBasedOnId(id);         //find the school task in the database using the id
        rGeneral.moveToFirst();

        sDate=rGeneral.getLong(rGeneral.getColumnIndex("STARTDATE"));  //set startDate to the long startDate type retrieved from database
        eDate=rGeneral.getLong(rGeneral.getColumnIndex("ENDDATE"));    //set endDate to the long endDate type retrieved from database
        sTime=rGeneral.getLong(rGeneral.getColumnIndex("STARTTIME"));   //set startTime to the long startTime type retrieved from database
        eTime=rGeneral.getLong(rGeneral.getColumnIndex("ENDTIME"));     //set endTime to the long end time type retrieved from database
        l=rGeneral.getString(rGeneral.getColumnIndex("LOCATION"));      //set location to the location retrieved from database
        c=rGeneral.getString(rGeneral.getColumnIndex("CLASS"));          //set class to the class retrieved from database
        n=rGeneral.getString(rGeneral.getColumnIndex("NOTE"));            //set note to the note retrieved from database



        //the following set all the edit text to the respective data retrieved from the database so the user can see all the previous data they had
        //entered for that task and can then proceed to change whichever data they want
        startDate.setText(getDate(rGeneral.getLong(rGeneral.getColumnIndex("STARTDATE"))));
        endDate.setText(getDate(rGeneral.getLong(rGeneral.getColumnIndex("ENDDATE"))));
        startTime.setText(getTime(rGeneral.getLong(rGeneral.getColumnIndex("STARTTIME"))));
        endTime.setText(getTime(rGeneral.getLong(rGeneral.getColumnIndex("ENDTIME"))));
        loc.setText(rGeneral.getString(rGeneral.getColumnIndex("LOCATION")));
        classes.setText(rGeneral.getString(rGeneral.getColumnIndex("CLASS")));
        note.setText(rGeneral.getString(rGeneral.getColumnIndex("NOTE")));
        //end

        start_T.setOnClickListener(this);
        start_D.setOnClickListener(this);
        end_T.setOnClickListener(this);
        end_D.setOnClickListener(this);

        update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //validation check to ensure all the data is entered excluding "NOTE"
                        if (startDate.getText().toString().trim().contains("Start Date") || endDate.getText().toString().contains("End Date") || startTime.getText().toString().contains("Start Time") || endTime.getText().toString().contains("End Time") || loc.getText().toString().trim().length() == 0 || classes.getText().toString().trim().length() == 0) {
                            Toast.makeText(getApplicationContext(), "Data not Inserted.Please fill all information", Toast.LENGTH_LONG).show();
                        }
                        else {

                            s = title.getText().toString();
                            l = loc.getText().toString();
                            c = classes.getText().toString();

                            if (note.getText().toString().trim().length() == 0) {   //if no note added
                                note.setText("No notes added.");
                                n = note.getText().toString();
                            } else {
                                n = note.getText().toString();
                            }

                             myDb.updateDataSchool(id,s, sDate, eDate, sTime, eTime, l, c, n);  //insert data into the school table


                            Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_LONG).show();


                            Intent i = new Intent(getApplicationContext(), DefaultMonthlyCalendarActivity.class);
                            startActivity(i);     //jump to the calendar screen to show updated task
                        }
                    }
                }
        );
    }


    @Override
    public void onClick(View v) {

        if (v == start_D) {              //once the start date button is clicked then a calendar pops up to select a start date

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);



                            try {
                                String z=startDate.getText().toString();

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = (Date)formatter.parse(z);
                                sDate = date.getTime();



                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == end_D) {             //once the end date button is clicked then a calendar pops up to select a end date

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            endDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            try {
                                String z=endDate.getText().toString();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = (Date)formatter.parse(z);
                                eDate = date.getTime();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == start_T ) { //once the start time button is clicked then a clock pops up to select a start time

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            Time t = Time.valueOf(Integer.toString(hourOfDay)+":"+Integer.toString(minute)+":00");
                            sTime = t.getTime();
                            if(hourOfDay >=12) {
                                startTime.setText(hourOfDay + ":" + minute + " pm");
                            }
                            else {
                                startTime.setText(hourOfDay + ":" + minute + " am");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == end_T ) { //once the endtime  button is clicked then a calendar pops up to select a end time

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            Time t = Time.valueOf(Integer.toString(hourOfDay)+":"+Integer.toString(minute)+":00");
                            eTime = t.getTime();
                            if(hourOfDay >=12) {
                                endTime.setText(hourOfDay + ":" + minute + " pm");
                            }
                            else {
                                endTime.setText(hourOfDay + ":" + minute + " am");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


    public String getDate(long timeStamp){          //convert long date to string

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public String getTime(long timeStamp){    // convert long time to string

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

}
