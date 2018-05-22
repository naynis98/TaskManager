package com.example.a16033774.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Start version with 1
    // increment by 1 whenever db schema changes.
    private static final int DATABASE_VER = 1;

    private static final String DATABASE_NAME = "taskMamager.db";

    private static final String TABLE_TASK = "task";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_TASK +  "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT )";
        Log.i("info" ,createTableSql);

        db.execSQL(createTableSql);
        Log.i("info" ,"created tables");

    }

    //creating the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        // Create table(s) again
        onCreate(db);
    }

    //inserting data
    public void insertTask(String description, String name){

        // Get an instance of the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // We use ContentValues object to store the values for
        //  the db operation
        ContentValues values = new ContentValues();
        // Store the column name as key and the description as value
        values.put(COLUMN_DESCRIPTION, description);
        // Store the column name as key and the date as value
        values.put(COLUMN_NAME, name);
        // Insert the row into the TABLE_TASK
        db.insert(TABLE_TASK, null, values);
        // Close the database connection
        db.close();
    }

    public ArrayList<String> getTaskContent() {
        // Create an ArrayList that holds String objects
        ArrayList<String> tasks = new ArrayList<String>();
        // Select all the tasks' description
        String selectQuery = "SELECT " + COLUMN_DESCRIPTION
                + " FROM " + TABLE_TASK;

        // Get the instance of database to read
        SQLiteDatabase db = this.getReadableDatabase();
        // Run the SQL query and get back the Cursor object
        Cursor cursor = db.rawQuery(selectQuery, null);

        // moveToFirst() moves to first row
        if (cursor.moveToFirst()) {
            // Loop while moveToNext() points to next row
            //  and returns true; moveToNext() returns false
            //  when no more next row to move to
            do {
                // Add the task content to the ArrayList object
                //  0 in getString(0) return the data in the first
                //  column in the Cursor object. getString(1)
                //  return second column data and so on.
                //  Use getInt(0) if data is an int
                tasks.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        // Close connection
        cursor.close();
        db.close();

        return tasks;
    }

    //return the database records in the form of task objects
    public ArrayList<String> getTasks() {
        ArrayList<String> tasks = new ArrayList<String>();
        String columns[] = {COLUMN_ID,COLUMN_NAME,COLUMN_DESCRIPTION};
        String selectQuery = "SELECT " + COLUMN_ID + ", "
                + COLUMN_DESCRIPTION + ", "
                + COLUMN_NAME
                + " FROM " + TABLE_TASK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String description = cursor.getString(1);
                String name = cursor.getString(2);
                tasks.add("id : "+ id + " name : " + name + " descripton : " + description);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

}
