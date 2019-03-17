package com.example.kyle.uwitimemanagemeent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

public class AddSchoolTask extends AppCompatActivity implements View.OnClickListener{
    DatabaseHelper myDb;
    TextView title;
    EditText startDate,endDate,startTime,endTime,note,loc,classes;
    Button btnAddData;
    Button start_T,start_D,end_T,end_D;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long sDate,sTime,eDate,eTime;
    String s = "";
    String l=" ";
    String c=" ";
    String n = "";
    String date = "";
    CompactCalendarView compactCalendar;
    private static int titleCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_school_task);
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
        btnAddData = (Button)findViewById(R.id.task_save);

        date=getIntent().getStringExtra("EE");
        startDate.setText(date);
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
        start_T.setOnClickListener(this);
        start_D.setOnClickListener(this);
        end_T.setOnClickListener(this);
        end_D.setOnClickListener(this);

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //validation checks
                        if (startDate.getText().toString().trim().contains("Start Date") || endDate.getText().toString().contains("End Date") || startTime.getText().toString().contains("Start Time") || endTime.getText().toString().contains("End Time") || loc.getText().toString().trim().length() == 0 || classes.getText().toString().trim().length() == 0) {
                            Toast.makeText(getApplicationContext(), "Data not Inserted.Please fill all information", Toast.LENGTH_LONG).show();
                        }
                        else {
                            myDb = new DatabaseHelper(getApplicationContext());
                            s = title.getText().toString();
                            l = loc.getText().toString();
                            c = classes.getText().toString();

                            if (note.getText().toString().trim().length() == 0) {
                                note.setText("No notes added.");
                                n = note.getText().toString();
                            } else {
                                n = note.getText().toString();
                            }

                            myDb.insertDataSchoolTask(s, sDate, eDate, sTime, eTime, l, c, n);   //insert into databa
                            Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_LONG).show();


                            Intent i = new Intent(getApplicationContext(), DefaultMonthlyCalendarActivity.class);
                            startActivity(i);
                        }
                    }
                }
        );


    }
    @Override
    public void onClick(View v) {

        if (v == start_D) {

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
        if (v == end_D) {

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
        if (v == start_T ) {

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
        if (v == end_T ) {

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



}