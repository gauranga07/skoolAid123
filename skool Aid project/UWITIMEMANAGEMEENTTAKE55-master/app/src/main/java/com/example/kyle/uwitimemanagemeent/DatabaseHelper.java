package com.example.kyle.uwitimemanagemeent;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Database1.db";
    /////////General task
    public static final String TABLE_NAME = "task";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "STARTDATE";
    public static final String COL_4 = "ENDDATE";
    public static final String COL_5 = "STARTTIME";
    public static final String COL_6 = "ENDTIME";
    public static final String COL_7 = "NOTE";

   /////////tip table
    public static final String TABLE_NAME2 = "tips";
    public static final String TIP_COL_1 = "ID";
    public static final String TIP_COL_2 = "TIP";


    /////////school tip
    public static final String TABLE_NAME3= "schoolTask";
    public static final String SCH_COL_1 = "ID";
    public static final String SCH_COL_2 = "TITLE";
    public static final String SCH_COL_3 = "STARTDATE";
    public static final String SCH_COL_4 = "ENDDATE";
    public static final String SCH_COL_5 = "STARTTIME";
    public static final String SCH_COL_6 = "ENDTIME";
    public static final String SCH_COL_7 = "LOCATION";
    public static final String SCH_COL_8 = "CLASS";
    public static final String SCH_COL_9 = "NOTE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TITLE TEXT, " +
                "STARTDATE INTEGER," +
                "ENDDATE INTEGER," +
                "STARTTIME INTEGER," +
                "ENDTIME INTEGER," +
                "NOTE TEXT" +
                ")");


        db.execSQL("create table " + TABLE_NAME2 +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TIP TEXT " +
                ")");

        db.execSQL("create table " + TABLE_NAME3 +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "TITLE TEXT, " +
                "STARTDATE INTEGER," +
                "ENDDATE INTEGER," +
                "STARTTIME INTEGER," +
                "ENDTIME INTEGER," +
                "LOCATION TEXT,"+
                "CLASS TEXT,"+
                "NOTE TEXT" +
                ")");


    }

   @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME3);
        onCreate(db);
    }


    public boolean insertDataGeneralTask(String title,long startdate,long enddate,long starttime, long endtime,String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,startdate);
        contentValues.put(COL_4,enddate);
        contentValues.put(COL_5,starttime);
        contentValues.put(COL_6,endtime);
        contentValues.put(COL_7,note);



        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertTipData(String tip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIP_COL_2,tip);



        long result = db.insert(TABLE_NAME2,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    public boolean insertDataSchoolTask(String title,long startdate,long enddate,long starttime, long endtime,String location,String Class,String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCH_COL_2,title);
        contentValues.put(SCH_COL_3,startdate);
        contentValues.put(SCH_COL_4,enddate);
        contentValues.put(SCH_COL_5,starttime);
        contentValues.put(SCH_COL_6,endtime);
        contentValues.put(SCH_COL_7,location);
        contentValues.put(SCH_COL_8,Class);
        contentValues.put(SCH_COL_9,note);

        long result = db.insert(TABLE_NAME3,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllDataGeneral() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getAllDataTip() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2,null);
        return res;
    }

    public Cursor getTipDataBasedOnId(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query(TABLE_NAME2, new String[] {TIP_COL_2}, TIP_COL_1+"==" + id, null, null, null, null);
        return res;

    }

    public Cursor getGeneralDataBasedOnId(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query(TABLE_NAME, new String[] {COL_3,COL_4,COL_5,COL_6,COL_7}, COL_1+"==" + id, null, null, null, null);
        return res;

    }

    public Cursor getSchoolDataBasedOnId(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query(TABLE_NAME3, new String[] {SCH_COL_3,SCH_COL_4,SCH_COL_5,SCH_COL_6,SCH_COL_7,SCH_COL_8,SCH_COL_9}, SCH_COL_1+"==" + id, null, null, null, null);
        return res;

    }


    public Cursor getAllDataSchool() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME3,null);
        return res;
    }

    public boolean deleteItemGeneral(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_1 + "==" + id, null) > 0;
    }

    public boolean deleteItemTip(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, TIP_COL_1 + "==" + id, null) > 0;
    }

    public boolean deleteItemSchool(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME3, SCH_COL_1 + "==" + id, null) > 0;
    }

/*
    public Cursor getAllDataId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = "TABLE_NAME";
        String[] columns = {"*"};
        String selection = "COL_1 =?";
        String[] selectionArgs = {"id"};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        return cursor;
    }



    public boolean updateItem(int id,String title,long startdate,long enddate,long starttime, long endtime,String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2,title);
        cv.put(COL_3,startdate);
        cv.put(COL_4,enddate);
        cv.put(COL_5,starttime);
        cv.put(COL_6,endtime);
        cv.put(COL_7,note);


        db.update(TABLE_NAME, cv, "id="+id, null);


        long result = db.insert(TABLE_NAME,null ,cv);
        if(result == -1)
            return false;
        else
            return true;
    }
    */

    public boolean updateDataGeneral(int id,String title,long startdate,long enddate,long starttime, long endtime,String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,startdate);
        contentValues.put(COL_4,enddate);
        contentValues.put(COL_5,starttime);
        contentValues.put(COL_6,endtime);
        contentValues.put(COL_7,note);

        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] {String.valueOf(id)});
        return true;
    }


    public boolean updateDataSchool(int id,String title,long startdate,long enddate,long starttime, long endtime,String location,String Class,String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCH_COL_1,id);
        contentValues.put(SCH_COL_2,title);
        contentValues.put(SCH_COL_3,startdate);
        contentValues.put(SCH_COL_4,enddate);
        contentValues.put(SCH_COL_5,starttime);
        contentValues.put(SCH_COL_6,endtime);
        contentValues.put(SCH_COL_7,location);
        contentValues.put(SCH_COL_8,Class);
        contentValues.put(SCH_COL_9,note);

        db.update(TABLE_NAME3, contentValues, "ID = ?",new String[] {String.valueOf(id)});
        return true;
    }


    public Integer deleteDataGeneral (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
       // db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
      //  onCreate(db);
    }


    public Integer deleteDataTip (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, "ID = ?",new String[] {id});
        // db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        //  onCreate(db);
    }

    public void deleteAllDataTip (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("create table " + TABLE_NAME2 +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TIP TEXT " +
                ")");
    }

    public Integer deleteDataSchool (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME3, "ID = ?",new String[] {id});
        // db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        //  onCreate(db);
    }


}


