package com.example.kyle.uwitimemanagemeent;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;


public class WelcomeActivity extends AppCompatActivity {


    private Button back,calendar;
    private TextView tv;
    private UserSessionActivity session;
    private ImageView image;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_welcome);
        image=(ImageView)findViewById(R.id.imageView);
        back=(Button)findViewById(R.id.button3);
        back.setOnClickListener(new  Button_Clicker());
        myDb = new DatabaseHelper(getApplicationContext());
        calendar=(Button)findViewById(R.id.button2);
        calendar.setOnClickListener(new  Button_Clicker());

        FacultyPicked f=new FacultyPicked(getApplicationContext());
        int icon=f.getIcon();                     //gets the id of the faculty icon given the faculty that the user selects on the regiser screen
        image.setImageResource(icon);             //sets the image view on the welcome screen to the respective faculty icon

        String text="";
        try{
            FileInputStream i= openFileInput("name.txt");       //open name file
            int size=i.available();
            byte[] buffer= new  byte[size];
            i.read(buffer);
            i.close();
            text=new  String(buffer);                  //fetches the user name from the file and places it in text
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv=(TextView)findViewById(R.id.textView);
        tv.setText("Welcome "+ text);
        session = new UserSessionActivity(getApplicationContext());



    }
    class Button_Clicker implements Button.OnClickListener

    {

        @Override

        public void onClick(View v) {
            if (v == back) {                                         //if the back button is selected on the welcome screen then the tip table will need to be cleared since it will be pre loaded again with the same tips on the register screen
                myDb.deleteAllDataTip();
                session.writeLoginStatus(false);                     //login status set to false so its as if the user is accessing the app for the first time
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();

            }

            if(v==calendar){
                Intent i2 = new Intent(getApplicationContext(), DefaultMonthlyCalendarActivity.class);         //goes to calendar screen
                startActivity(i2);
            }
        }
    }

}
