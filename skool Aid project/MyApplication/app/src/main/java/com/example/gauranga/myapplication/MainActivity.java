package com.example.gauranga.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    ListView listView;
    MediaPlayer mediaPlayer;
    ArrayList<String> ids;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }

        }else {
                doStuff();

        }

    }

    public void getMusic(){

        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);

        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songIds = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do{
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentIds = songCursor.getString(songIds);
                arrayList.add(currentTitle+"\n"+currentArtist+"\n "+ currentIds+"\n");
                ids.add(currentIds);
            }while(songCursor.moveToNext());
        }

    }

    public void doStuff(){

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        ids = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        mediaPlayer = new MediaPlayer();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Uri myUri = Uri.fromFile(new File(ids.get(position)).getAbsoluteFile());
                Toast.makeText(getApplicationContext(),"uri" +myUri.toString(),Toast.LENGTH_SHORT).show();
//                try {
//                    mediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                try {
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    }
                    //mediaPlayer.setDataSource(ids.get(position));
                    mediaPlayer.setDataSource(getApplicationContext(),myUri);
                    //mediaPlayer = MediaPlayer.create(getApplicationContext(), myUri);
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setMyPermissionRequest(int requestCode, String[]permissions,int[]grantResults){
        switch (requestCode){
            case MY_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"Permission Granted!",Toast.LENGTH_SHORT).show();

                        doStuff();
                    }
                    else Toast.makeText(this," No permission Granted!",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}
