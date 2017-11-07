package ca.bcit.ass3.katz_kao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Lel on 2017-10-31.
 */

public class PotluckDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "potluck.db";
    private static final int DB_VERSION = 2;
    private Context ctx;

    public PotluckDbHelper(Context text) {
        super(text, DB_NAME, null, 1);
        this.ctx = text;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateMyDatabase(sqLiteDatabase, i, i1);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(getDeleteDetailTableSql());
        db.execSQL(getDeleteMasterTableSql());
        db.execSQL(getCreateMasterTableSql());
        db.execSQL(getCreateDetailTableSql());
        try {
            insertEvent(db, new Event("Halloween Party", new Date()));
            insertEvent(db, new Event("Christmas Party", new Date()));
            insertEvent(db, new Event("New Years Party", new Date()));
            insertItem(db, new Item("Beer", "6 pack", 5), 1);
            insertItem(db, new Item("Cookie", "24 Box", 2), 1);
        } catch (SQLException sqle) {
            String msg = "[MyPlanetDbHelper / updateMyDatabase/insertCountry] DB unavailable";
            msg += "\n\n" + sqle.toString();
            Toast t = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
            t.show();
        }
    }


    private String getCreateMasterTableSql() {
        String sql= "";
        sql += "CREATE TABLE EVENT_MASTER (";
        sql += "_eventId INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += "EVENT_NAME TEXT, ";
        sql += "EVENT_DATE INTEGER";
        sql += ");";
        return sql;
    }

    private String getDeleteMasterTableSql() {
        String sql = "";
        sql += "DROP TABLE IF EXISTS EVENT_MASTER;";
        return sql;
    }

    private String getCreateDetailTableSql() {
        String sql= "";
        sql += "CREATE TABLE EVENT_DETAIL (";
        sql += "_detailId INTEGER PRIMARY KEY AUTOINCREMENT,";
        sql += "ItemName TEXT,";
        sql += "ItemUnit TEXT,";
        sql += "ItemQuantity INTEGER,";
        sql += "eventId INTEGER,";
        sql += "FOREIGN KEY(eventId) REFERENCES EVENT_MASTER(_eventId)";
        sql += ");";
        return sql;
    }

    private String getDeleteDetailTableSql() {
        String sql = "";
        sql += "DROP TABLE IF EXISTS EVENT_DETAIL;";
        return sql;
    }

    private void insertEvent(SQLiteDatabase db, Event evnt) {
        ContentValues values = new ContentValues();
        values.put("EVENT_NAME", evnt.getEventName());
        values.put("EVENT_DATE", evnt.getMilliDate());
        db.insert("EVENT_MASTER", null, values);
    }

    private void insertItem(SQLiteDatabase db, Item item, int id) {
        ContentValues values = new ContentValues();
        values.put("ItemName", item.getItemName());
        values.put("ItemUnit", item.getItemUnit());
        values.put("ItemQuantity" , item.getItemCount());
        values.put("eventId", id);
        db.insert("EVENT_DETAIL", null, values);
    }

}
