package com.example.kyle.uwitimemanagemeent;

public class Card {    //general task details to be displayed
    String Title;
    String Startd;
    int photoId;
    String ID;


    Card(String id,String title, String d, int photoId) {
        this.ID=id;
        this.Title = title;
        this.Startd = d;
        this.photoId = photoId;



    }

    public int getId(){           //get id of task
        if (ID != null) {
            int i = Integer.parseInt(ID);
            return i;
        }
        return 0;
    }
}

