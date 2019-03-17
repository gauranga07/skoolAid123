package com.example.kyle.uwitimemanagemeent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SchoolCardActivity extends AppCompatActivity {

    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager lm;

    DatabaseHelper myDb;
    ArrayList<SchoolCard> cards;
    private static String LOG_TAG = "SchoolCardViewActivity";
    private ImageButton delete,home;
    private Button open;
    private int posID=0;
    ImageView i;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_card);
        delete = (ImageButton)findViewById(R.id.delete_button);
        home = (ImageButton)findViewById(R.id.pinkHome);
        open = (Button)findViewById(R.id.open_button);
        myDb = new DatabaseHelper(getApplicationContext());
        i=(ImageView)findViewById(R.id.imageViewlogo);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Cursor res = myDb.getAllDataSchool();
        if(res.getCount() == 0) {             //database empty

            Toast.makeText(getApplicationContext(),"Empty!",Toast.LENGTH_LONG).show();

        }


        FacultyPicked f=new FacultyPicked(getApplicationContext());
        int a=f.getIcon();                                         //get icon of faculty selected

        cards = new ArrayList<SchoolCard>();
        while (res.moveToNext()) {        //move through each school task adding it to the card arraylist
            cards.add(new SchoolCard(res.getString(0),res.getString(1),getDate(res.getLong(2)),a));

        }

        rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        mAdapter = new MyRecyclerViewAdapter2(cards);
        rv.setAdapter(mAdapter);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SchoolCard c;
                if(cards.size()>0) {
                    c = cards.get(posID);

                    boolean a = myDb.deleteItemSchool(c.getId());           //delete item from database using the id


                    cards.remove(posID);                                //remove card

                    mAdapter.notifyItemRemoved(posID);
                }
                else
                    Toast.makeText(SchoolCardActivity.this, "ALL ITEMS DELETED", Toast.LENGTH_SHORT).show();
            }});
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == open) {
                    SchoolCard c;
                    if(cards.size()==0) {
                        Toast.makeText(SchoolCardActivity.this, "NO SCHOOL TASK AVAILABLE", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        c = cards.get(posID);
                        int id = c.getId();                         //get id of school task
                        String pos = Integer.toString(id);
                        Intent i = new Intent(getApplicationContext(), ShowAndUpdateSchTask.class);
                        i.putExtra("R", pos);
                        startActivity(i);                  //open activity to show individual school task
                    }


                } }});

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == home) {
                    Intent i = new Intent(getApplicationContext(), DefaultMonthlyCalendarActivity.class);
                    startActivity(i);
                }

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter2) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter2
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                posID = position;
                delete.setVisibility(View.VISIBLE);
                open.setVisibility(View.VISIBLE);
                Snackbar.make(findViewById(R.id.my_recycler_view) , Integer.toString(position+1)+") "+cards.get(position).Title+" selected", Snackbar.LENGTH_LONG)          //when card selected it display the position of the card item in the list
                        .setAction("Action", null)
                        .setDuration(250)
                        .show();

            }
        });

    }




    public String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

}


