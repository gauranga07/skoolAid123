//relatively the same documentation as SchoolCardActivity
//this card activity is for angeneral task
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CardActivity extends AppCompatActivity {

    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager lm;
    DatabaseHelper myDb;
    ArrayList<Card> cards;
    private static String LOG_TAG = "CardViewActivity";
    private ImageButton delete,home;
    private Button open;
    private int posID = 0;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        delete = (ImageButton) findViewById(R.id.delete_button);
        open = (Button) findViewById(R.id.open_button);
        home = (ImageButton) findViewById(R.id.pinkHome);
        myDb = new DatabaseHelper(getApplicationContext());
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Cursor res = myDb.getAllDataGeneral();
        if (res.getCount() == 0) {

            Toast.makeText(getApplicationContext(), " Empty!", Toast.LENGTH_LONG).show();

        }


        cards = new ArrayList<Card>();
        FacultyPicked f = new FacultyPicked(getApplicationContext());
        int icon = f.getIcon();

        while (res.moveToNext()) {
            cards.add(new Card(res.getString(0), res.getString(1), getDate(res.getLong(2)), icon));

        }


        rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        mAdapter = new MyRecyclerViewAdapter(cards);
        rv.setAdapter(mAdapter);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card c;
                if(cards.size()>0) {
                    c = cards.get(posID);

                    boolean a = myDb.deleteItemGeneral(c.getId());

                    cards.remove(posID);


                    mAdapter.notifyItemRemoved(posID);
                }
                else
                    Toast.makeText(CardActivity.this, "ALL ITEMS DELETED", Toast.LENGTH_SHORT).show();
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == open) {
                    Card c;
                    if(cards.size()==0) {
                        Toast.makeText(CardActivity.this, "NO GENERAL TASKS AVAILABLE", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        c = cards.get(posID);
                        int id = c.getId();
                        String pos = Integer.toString(id);
                        Intent i = new Intent(getApplicationContext(), ShowAndUpdateGenTask.class);
                        i.putExtra("E", pos);
                        startActivity(i);
                    }

                }
            }
        });
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
        protected void onResume () {
            super.onResume();
            ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                    .MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    posID = position;
                    delete.setVisibility(View.VISIBLE);
                    open.setVisibility(View.VISIBLE);
                    Snackbar.make(findViewById(R.id.my_recycler_view), Integer.toString(position + 1) + ") " + cards.get(position).Title + " selected", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setDuration(250)
                            .show();
                }
            });

        }





        public String getDate ( long timeStamp){

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date netDate = (new Date(timeStamp));
                return sdf.format(netDate);
            } catch (Exception ex) {
                return "xx";
            }
        }





}