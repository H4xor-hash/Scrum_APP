package com.example.mylogin;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private Context main_context;

    private static String DB_PATH;
    private static String DB_NAME = "nima.db";
    private static int DB_VERSION = 1;
    private static String DB_TBL_NAME = "theme";


    private SQLiteDatabase db;
    //متد سازنده
    public DatabaseHandler( Context con )
    {
        super(con, DB_NAME, null, DB_VERSION);

        main_context = con;

        DB_PATH = con.getCacheDir().getPath() + "/" + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /* do nothing */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        /* do nothing */
    }

    //بررسی اینکه دیتابیس در حافظه وجود دارد یا نه
    public boolean dbExists()
    {
        File f = new File( DB_PATH );
        if( f.exists() )
            return true;
        else
            return false;
    }

    //کپی دیتابیس در کش
    private boolean copyDB()
    {
        try
        {
            FileOutputStream out = new FileOutputStream( DB_PATH );

            InputStream in = main_context.getAssets().open( DB_NAME );

            byte[] buffer = new byte[1024];
            int ch;

            while( ( ch = in.read( buffer) ) > 0 )
            {
                out.write( buffer , 0 , ch);
            }

            out.flush();
            out.close();
            in.close();

            return true;
        }
        catch( Exception e )
        {
            /* do nothing */
            return false;
        }
    }

    //باز کردن دیتابیس
    public void open()
    {
        if( dbExists() )
        {
            try
            {
                File temp = new File( DB_PATH );

                db = SQLiteDatabase.openDatabase(
                        temp.getAbsolutePath() , null , SQLiteDatabase.OPEN_READWRITE
                );
            }
            catch(Exception e)
            {
                /* do nothing */
            }
        }
        else
        {
            if( copyDB() )
                open();
        }
    }
    //بستن دیتابیس
    @Override
    public synchronized void close()
    {
        db.close();
    }

    //بروز رسانی اطلاعات دیتابیس
    public boolean setBookVisitState( String id , String state )
    {
        ContentValues cv = new ContentValues();
        cv.put("color", state);

        long result = db.update(DB_TBL_NAME , cv , " id = ?" , new String[] { id });
        //   db.execSQL("UPDATE theme SET color='test' WHERE id=0 ");

        if( result < 1 )
            return false;
        else
            return true;
    }




    //خواندن اطلاعات از دیتابیس

    public List<HashMap<String , Object>> getTableOfContent()
    {
        Cursor result = db.rawQuery( "SELECT * FROM " + DB_TBL_NAME, null );

        List<HashMap<String , Object>> all_data = new ArrayList<>();

        while( result.moveToNext() )
        {
            HashMap<String , Object> temp = new HashMap<>();

            temp.put("id", result.getString(0));
            temp.put( "color" , result.getString( 1 ) );


            all_data.add( temp );
        }

        return all_data;
    }

//    //حذف رکورد از دیتابیس
//    public boolean deleteData( String id )
//    {
//
////NO NEED !!!!!!!!!!!!!!!!!!!! NO NEED AT ALL!!!!!!!!!!!!!!
//        SQLiteDatabase db = this.getWritableDatabase();
////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//
//        long result = db.delete(TBL_NAME, "Id = ?", new String[]{id});
//
//        if( result == 0 )
//            return false;
//        else
//            return true;
//    }
//    //درج اطلاعات
//    public boolean insertData( String name,String age )
//    {
//        ContentValues values = new ContentValues();
//
//        values.put(COL_NAME, name);
//        values.put(COL_NAME, age);
//
//        long result=db.insert(YOUR_TABLE, null, values);
//        if( result == 0 )
//            return false;
//        else
//            return true;
//    }


}
