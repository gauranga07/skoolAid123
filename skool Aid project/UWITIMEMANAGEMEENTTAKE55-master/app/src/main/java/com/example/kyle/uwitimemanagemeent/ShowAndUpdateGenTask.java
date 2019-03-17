package com.example.kyle.uwitimemanagemeent;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowAndUpdateGenTask extends AppCompatActivity implements View.OnClickListener {


    DatabaseHelper myDb;
    TextView title;
    EditText startDate, endDate, startTime, endTime, note;
    Button SaveandUpdate;
    Button start_T, start_D, end_T, end_D;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long sDate, sTime, eDate, eTime;
    String s = "";
    String n = "";
    int id;
    ArrayList<Card> cards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_update_gen_task);
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
        SaveandUpdate = (Button) findViewById(R.id.task_save);
        myDb=new DatabaseHelper(getApplicationContext());
        String posID=getIntent().getStringExtra("E");
        id=Integer.parseInt(posID);                                            ////id of the general item you want to update


        Cursor rGeneral=myDb.getGeneralDataBasedOnId(id);                     //find the general task in the database using the id
        rGeneral.moveToFirst();

        sDate=rGeneral.getLong(rGeneral.getColumnIndex("STARTDATE"));
        eDate=rGeneral.getLong(rGeneral.getColumnIndex("ENDDATE"));
        sTime=rGeneral.getLong(rGeneral.getColumnIndex("STARTTIME"));
        eTime=rGeneral.getLong(rGeneral.getColumnIndex("ENDTIME"));
        n=rGeneral.getString(rGeneral.getColumnIndex("NOTE"));

        startDate.setText(getDate(rGeneral.getLong(rGeneral.getColumnIndex("STARTDATE"))));
        endDate.setText(getDate(rGeneral.getLong(rGeneral.getColumnIndex("ENDDATE"))));
        startTime.setText(getTime(rGeneral.getLong(rGeneral.getColumnIndex("STARTTIME"))));
        endTime.setText(getTime(rGeneral.getLong(rGeneral.getColumnIndex("ENDTIME"))));
        note.setText(rGeneral.getString(rGeneral.getColumnIndex("NOTE")));


        start_T.setOnClickListener(this);
        start_D.setOnClickListener(this);
        end_T.setOnClickListener(this);
        end_D.setOnClickListener(this);


        //perform validation checks here
        SaveandUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //validation check to ensure the data is entered
                        if (startDate.getText().toString().trim().contains("Start Date") || endDate.getText().toString().contains("End Date") || startTime.getText().toString().contains("Start Time") || endTime.getText().toString().contains("End Time")) {
                            Toast.makeText(getApplicationContext(), "Data not Inserted.Please fill out Date and Time information", Toast.LENGTH_LONG).show();


                        }
                        if(sDate > eDate || sTime > eTime){
                            Toast.makeText(getApplicationContext(), "You must select an end date/time in the future", Toast.LENGTH_LONG).show();
                            startDate.setText(" ");
                            endDate.setText(" ");
                            startTime.setText(" ");
                            startTime.setText(" ");
                        }
                        else{

                            s = title.getText().toString();

                            if (note.getText().toString().trim().length() == 0) {
                                note.setText("No notes added.");
                                n = note.getText().toString();
                            }
                            else {
                                n = note.getText().toString();
                            }

                            AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                            Intent myIntent = new Intent(ShowAndUpdateGenTask.this,AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);
                            boolean isRepeat= false;

                            if(!isRepeat) {

                                manager.set(AlarmManager.RTC_WAKEUP, (eDate + eTime), pendingIntent);
                                Toast.makeText(getApplicationContext(), "Alarm Set for "+ Long.toString((eDate+eTime)-System.currentTimeMillis()), Toast.LENGTH_LONG).show();
                            }
                            else

                                manager.setRepeating(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtime()+3000,60*1000,pendingIntent);

                            myDb.updateDataGeneral(id,s, sDate, eDate, sTime, eTime, n);           //update general table in the database

                            Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_SHORT).show();


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
                                String z = startDate.getText().toString();

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = (Date) formatter.parse(z);
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
                                String z = endDate.getText().toString();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = (Date) formatter.parse(z);
                                eDate = date.getTime();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == start_T) {

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
                            sTime = t.getTime();
                            if (hourOfDay >= 12) {
                                startTime.setText(hourOfDay + ":" + minute + " pm");
                            } else {
                                startTime.setText(hourOfDay + ":" + minute + " am");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == end_T) {

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
                            eTime = t.getTime();
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

    public String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }
    public String getTime(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }


}
