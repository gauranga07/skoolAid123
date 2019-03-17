package com.example.kyle.uwitimemanagemeent;

import android.content.Context;
import android.view.View;

import java.io.FileInputStream;
import java.io.IOException;

public class FacultyPicked {

    Context c;

    public FacultyPicked(Context context) {
        this.c = context;
    }

    public int getIcon() {                                //returns id of the faculty icon of the faculty selected by the user
        int i=0;
        String b = "";
        try {
            FileInputStream i1 = c.openFileInput("faculty.txt");            //open the faculty file
            int size1 = i1.available();
            byte[] buffer1 = new byte[size1];
            i1.read(buffer1);
            i1.close();
            b = new String(buffer1);           //b set to the text in the file
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(b.equals("Faculty of Engineering")){                   //comparing b(faculty in the file) to the Faculty of Engineering.This is repeated for the other faculties
            i=R.drawable.engine;                                  //id of the engneering icon in the drawable file
        }
        if(b.equals("Faculty of Food and Agriculture")){
            i=R.drawable.food;
        }
        if(b.equals("Faculty of Science and Technology")){
            i=R.drawable.tech;
        }
        if(b.equals("Faculty of Medical Sciences")){
            i=R.drawable.med;
        }
        if(b.equals("Faculty of Social Sciences")){
            i=R.drawable.oc;
        }
        if(b.equals("aculty of Humanities and Education")){
            i=R.drawable.human;
        }
        if(b.equals("Faculty of Law")){
            i=R.drawable.la;
        }
        return i;
    }
}



