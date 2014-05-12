package musicalbum.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String ARRANGEMENTS_TABLE = "arrangements";

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_COMPOSER = "composer";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_PATH = "path";
    public static final String ARRANGEMENT_ID = "id";
    private static final String DATABASE_NAME = "arrangements.db";
    private static final int DATABASE_VERSION = 3;

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null,DATABASE_VERSION);

    }

    @Override

    public void onCreate(SQLiteDatabase database) {

        database.execSQL("CREATE TABLE IF NOT EXISTS "
                + ARRANGEMENTS_TABLE + " (" +  ARRANGEMENT_ID

                + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                +  COLUMN_TITLE + " TEXT unique, "

                +  COLUMN_COMPOSER + " TEXT, "

                +  COLUMN_NOTES + " TEXT, "

                + COLUMN_PATH + " TEXT);");

    }


    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ARRANGEMENTS_TABLE);
        onCreate(db);
    }

    public ArrayList<String> getTitles() {

        ArrayList<String> mStrings = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c;

        if (db != null) {

            c = db.query(ARRANGEMENTS_TABLE, new String[]{COLUMN_TITLE}, null, null,null,null, null);

            int count = c.getCount();

            c.moveToFirst();

            for (int i = 0; i < count; i++)

            {
                String temp="";

                temp = c.getString(c.getColumnIndex(COLUMN_TITLE));

                mStrings.add(i,temp);

                if(i!=count-1)
                {
                    c.moveToNext();
                }
                //int x =3;
            }
        //int y = 4;

            c.close();

            db.close();
            return mStrings;

        }

        return mStrings;

    }

    public ArrayList<String> getComposers() {

        ArrayList<String> mStrings = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c;

        if (db != null) {

            c = db.query(ARRANGEMENTS_TABLE, new String[]{COLUMN_COMPOSER}, null, null,null,null, null);

            int count = c.getCount();

            c.moveToFirst();

            for (int i = 0; i < count; i++)

            {
                String temp="";

                temp = c.getString(c.getColumnIndex(COLUMN_COMPOSER));

                mStrings.add(i, temp);

                if(i!=count-1)
                {
                    c.moveToNext();
                }
                //int x =3;
            }
            //int y = 4;

            c.close();

            db.close();
            return mStrings;

        }

        return mStrings;

    }

    public ArrayList<String> getNotes() {

        ArrayList<String> mStrings = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c;

        if (db != null) {

            c = db.query(ARRANGEMENTS_TABLE, new String[]{COLUMN_NOTES}, null, null,null,null, null);

            int count = c.getCount();

            c.moveToFirst();

            for (int i = 0; i < count; i++)

            {
                String temp="";

                temp = c.getString(c.getColumnIndex(COLUMN_NOTES));

                mStrings.add(i, temp);

                if(i!=count-1)
                {
                    c.moveToNext();
                }
                //int x =3;
            }
            //int y = 4;

            c.close();

            db.close();
            return mStrings;

        }

        return mStrings;

    }

    public ArrayList<String> getPaths() {

        ArrayList<String> mStrings = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c;

        if (db != null) {

            c = db.query(ARRANGEMENTS_TABLE, new String[]{COLUMN_PATH}, null, null,null,null, null);

            int count = c.getCount();

            c.moveToFirst();

            for (int i = 0; i < count; i++)

            {
                String temp="";

                temp = c.getString(c.getColumnIndex(COLUMN_PATH));

                mStrings.add(i, temp);

                if(i!=count-1)
                {
                    c.moveToNext();
                }
                //int x =3;
            }
            //int y = 4;

            c.close();

            db.close();
            return mStrings;

        }

        return mStrings;

    }




    public boolean insert (String title, String composer, String notes, String path) {
        SQLiteDatabase db = this.getWritableDatabase();

        //add the following line before call to fxn

          ContentValues insertValues = new ContentValues();

        //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
        insertValues.put(COLUMN_TITLE,title);
        insertValues.put(COLUMN_COMPOSER,composer);
        insertValues.put(COLUMN_NOTES,notes);
        insertValues.put(COLUMN_PATH,path);

        db.insert(ARRANGEMENTS_TABLE,null, insertValues);
        db.close();

         return true;


   }

    public boolean update(String oldtitle, String newtitle, String composer, String notes)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //add the following line before call to fxn

        ContentValues insertValues = new ContentValues();

        //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
        insertValues.put(COLUMN_TITLE,newtitle);
        insertValues.put(COLUMN_COMPOSER,composer);
        insertValues.put(COLUMN_NOTES,notes);



        assert db != null;
        db.update(ARRANGEMENTS_TABLE,insertValues,COLUMN_TITLE+"='"+oldtitle+"'",null);

        return true;
    }
      
    public boolean remove_item(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        assert db != null;
        db.execSQL("DELETE FROM " + ARRANGEMENTS_TABLE + " WHERE " + COLUMN_TITLE + "='" + title + "'");

        return true;
    }
}