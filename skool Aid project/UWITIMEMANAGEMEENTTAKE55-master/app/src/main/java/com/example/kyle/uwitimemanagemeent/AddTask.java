package com.example.kyle.uwitimemanagemeent;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {
    private Button school;
    private Button gym;
    private Button party;
    private Button general;
    Intent i,i2;
    String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        school = findViewById(R.id.school_btn);
        gym = findViewById(R.id.gym_btn);
        party = findViewById(R.id.party_btn);
        general = findViewById(R.id.general_btn);
        date=getIntent().getStringExtra("Extra");         //obtain date from the DefaultMonthlyCalendar screen.This date can contain a date selected on the calendar to be used as the start date or the text "Start date"

        i = new Intent(this,AddTaskDetails.class);
        i2= new Intent(this,AddSchoolTask.class);

        school.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i2.putExtra("EE", date);
                      startActivity(i2);                    //once school button is selected then the user is forwarded to the AddSchoolTask screen
            }
        });
        gym.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),  "Feature not available", Toast.LENGTH_SHORT).show();
            }
        });
        party.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),  "Feature not available", Toast.LENGTH_SHORT).show();
            }
        });
        general.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i.putExtra("EE", date);
                startActivity(i);                                       //once school button is selected then the user is forwarded to the Add General Task screen
            }
        });
    }
}
