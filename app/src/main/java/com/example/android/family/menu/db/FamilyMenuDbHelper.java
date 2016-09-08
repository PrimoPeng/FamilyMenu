package com.example.android.family.menu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.android.family.menu.model.FamilyMenu;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/7.
 */
public class FamilyMenuDbHelper extends SQLiteOpenHelper {

    private static FamilyMenuDbHelper familyMenuDbHelper;

    public static FamilyMenuDbHelper getInstance(Context context){
        if (null == familyMenuDbHelper) {
            familyMenuDbHelper = new FamilyMenuDbHelper(context);
        }
        return familyMenuDbHelper;
    }

    private static final String TEXT_TYPE = " TEXT ";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FamilyMenuEntry.TABLE_NAME + " (" +
                    FamilyMenuEntry._ID + " INTEGER PRIMARY KEY," +
                    FamilyMenuEntry.COLUMN_NAME_MENU_NAME + TEXT_TYPE + COMMA_SEP +
                    FamilyMenuEntry.COLUMN_NAME_MENU_URI + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FamilyMenuEntry.TABLE_NAME;

    public static abstract class FamilyMenuEntry implements BaseColumns {
        public static final String TABLE_NAME = "FamilyMenuEntry";
        public static final String COLUMN_NAME_MENU_NAME = "menuName";
        public static final String COLUMN_NAME_MENU_URI = "menuUri";
    }

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FamilyMenu.db";

    public FamilyMenuDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertData(FamilyMenu familyMenu) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FamilyMenuEntry.COLUMN_NAME_MENU_NAME, familyMenu.menuName);
        values.put(FamilyMenuEntry.COLUMN_NAME_MENU_URI, familyMenu.menuUri);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = -1l;
        newRowId = db.insert(
                FamilyMenuEntry.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public void deleteData(String rowId) {
        SQLiteDatabase db = getWritableDatabase();
        // Define 'where' part of query.
        String selection = FamilyMenuEntry._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(rowId)};
        // Issue SQL statement.
        db.delete(FamilyMenuEntry.TABLE_NAME, selection, selectionArgs);
    }

    public ArrayList<FamilyMenu> queryData() {
        SQLiteDatabase db = getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FamilyMenuEntry._ID,
                FamilyMenuEntry.COLUMN_NAME_MENU_NAME,
                FamilyMenuEntry.COLUMN_NAME_MENU_URI
        };
        // How you want the results sorted in the resulting Cursor
            /*String sortOrder =
                    FeedEntry.COLUMN_NAME_UPDATED + " DESC";*/
        Cursor c = db.query(
                FamilyMenuEntry.TABLE_NAME,  // The table to query
                projection,                  // The columns to return
                null,                        // The columns for the WHERE clause
                null,                        // The values for the WHERE clause
                null,                        // don't group the rows
                null,                        // don't filter by row groups
                null                         // The sort order
        );
        ArrayList<FamilyMenu> familyMenus = new ArrayList<FamilyMenu>();
        if (familyMenus.size() > 0) {
            familyMenus.clear();
        }
        while (c.moveToNext()) {
            FamilyMenu familyMenu1 = new FamilyMenu();
            familyMenu1.menuName = c.getString(c.getColumnIndex(FamilyMenuEntry.COLUMN_NAME_MENU_NAME));
            familyMenu1.menuUri = c.getString(c.getColumnIndex(FamilyMenuEntry.COLUMN_NAME_MENU_URI));
            familyMenus.add(familyMenu1);
        }
        return familyMenus;
    }
}


