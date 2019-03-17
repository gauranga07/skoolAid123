package com.example.kyle.uwitimemanagemeent;

public class SchoolCard {   //school data to be shown in the navigation bar
    String Title;
    String Startd;
    int photoId;
    String ID;


    SchoolCard(String id, String title, String d, int photoId) {
        this.ID = id;
        this.Title = title;
        this.Startd = d;
        this.photoId = photoId;

    }

    public int getId() {
        if (ID != null) {
            int i = Integer.parseInt(ID);
            return i;
        }
       return 0;

    }
}


