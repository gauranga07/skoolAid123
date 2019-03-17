package com.example.kyle.uwitimemanagemeent;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
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

public class AddTaskDetails extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper myDb;
    TextView title;
    EditText startDate, endDate, startTime, endTime, note;
    Button btnAddData, btnviewAll;
    Button start_T, start_D, end_T, end_D;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long sDate, sTime, eDate, eTime;
    String s = "";   //title
    String n = "";   //note

    String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_details);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        title = (TextView) findViewById(R.id.general_title);
        startDate = (EditText) findViewById(R.id.task_startDate);
        endDate = (EditText) findViewById(R.id.task_endDate);
        startTime = (EditText) findViewById(R.id.task_startTime);
        endTime = (EditText) findViewById(R.id.task_endTime);
        start_T = findViewById(R.id.Tstart);
        start_D = findViewById(R.id.Dstart);
        end_T = findViewById(R.id.Tend);
        end_D = findViewById(R.id.Dend);
        note = findViewById(R.id.task_note);
        btnAddData = (Button) findViewById(R.id.task_save);
        btnviewAll = (Button) findViewById(R.id.all);

        date=getIntent().getStringExtra("EE");          //get date that the user selects on the calendar or if the user selects the image button it obtains "Start date"
        startDate.setText(date);                              //set start date edit text to the date obtained

        if(!(startDate.getText().toString().equals("Start Date"))) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = null;
            try {
                date1 = (Date) formatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sDate = date1.getTime();
        }



        //check if button or edit text is selected
        start_T.setOnClickListener(this);
        start_D.setOnClickListener(this);
        end_T.setOnClickListener(this);
        end_D.setOnClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //validation check to ensure user enter all the data with the exception of note
                        if (startDate.getText().toString().trim().contains("Start Date") || endDate.getText().toString().contains("End Date") || startTime.getText().toString().contains("Start Time") || endTime.getText().toString().contains("End Time")) {
                            Toast.makeText(getApplicationContext(), "Data not Inserted.Please fill out Date and Time information", Toast.LENGTH_LONG).show();
                        }
                        if(sDate > eDate || sTime > eTime){
                            Toast.makeText(getApplicationContext(), "You must select an end date/time in the future", Toast.LENGTH_LONG).show();
                            startDate.setText(" ");
                            endDate.setText(" ");
                            startTime.setText(" ");
                            endTime.setText(" ");
                        }
                        else{
                            myDb = new DatabaseHelper(getApplicationContext());
                            s = title.getText().toString();

                            if (note.getText().toString().trim().length() == 0) {
                                note.setText("No notes added.");
                                n = note.getText().toString();
                            } else {
                                n = note.getText().toString();
                            }

                            AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                            Intent myIntent = new Intent(AddTaskDetails.this,AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);
                            boolean isRepeat= false;

                            if(!isRepeat) {

                                manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000, pendingIntent);
                                Toast.makeText(getApplicationContext(), "Alarm Set for "+ Long.toString((eDate+eTime)-System.currentTimeMillis()), Toast.LENGTH_LONG).show();
                            }
                            else

                                manager.setRepeating(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtime()+3000,60*1000,pendingIntent);



                            myDb.insertDataGeneralTask(s, sDate, eDate, sTime, eTime, n); // insert into database



                                Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();


                            Intent i = new Intent(getApplicationContext(), DefaultMonthlyCalendarActivity.class);
                            startActivity(i);                      //start the calendar activity

                        }

                    }
                }
        );

    }


    @Override
    public void onClick(View v) {

        if (v == start_D || v==startDate) {      //if user clicks on the start date button or on the start date edit text

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
                                String z = startDate.getText().toString();

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = (Date) formatter.parse(z);
                                sDate = date.getTime();                                                       //get the long value of the start date and set sDate to this value


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == end_D || v== endDate) {                  //if user clicks on the end date button or on the end date edit text

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
                                String z = endDate.getText().toString();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = (Date) formatter.parse(z);
                                eDate = date.getTime();                                           //get the long value of the end date and set eDate to this value

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == start_T || v==startTime) {        //if user clicks on the start time button or on the start time edit text

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
                            Time t = Time.valueOf(Integer.toString(hourOfDay) + ":" + Integer.toString(minute) + ":00");
                            sTime = t.getTime();                                                //get the long value of the start time and set sTime to this value
                            if (hourOfDay >= 12) {
                                startTime.setText(hourOfDay + ":" + minute + " pm");
                            } else {
                                startTime.setText(hourOfDay + ":" + minute + " am");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == end_T || v==endTime) {         ////if user clicks on the end time button or on the end time edit text

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
                            Time t = Time.valueOf(Integer.toString(hourOfDay) + ":" + Integer.toString(minute) + ":00");
                            eTime = t.getTime();                       //get the long value of the end time and set eTime to this value
                            if (hourOfDay >= 12) {
                                endTime.setText(hourOfDay + ":" + minute + " pm");
                            } else {
                                endTime.setText(hourOfDay + ":" + minute + " am");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }



}