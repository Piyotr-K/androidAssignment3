package ca.bcit.ass3.katz_kao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by Lel on 2017-10-31.
 */

public class PotluckDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "potluck.db";
    private static final int DB_VERSION = 1;
    private Context ctx;

    public PotluckDbHelper(Context text) {
        super(text, DB_NAME, null, 1);
        ctx = text;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getCreatePotluckTableSql());
        insertEvent(sqLiteDatabase, new Event("Halloween Party", new Date()));
        insertEvent(sqLiteDatabase, new Event("Halloween Party", new Date()));
        insertEvent(sqLiteDatabase, new Event("Halloween Party", new Date()));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private String getCreatePotluckTableSql() {
        String sql= "";
        sql += "CREATE TABLE EVENT_MASTER (";
        sql += "_id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += "EVENT_NAME TEXT, ";
        sql += "EVENT_DATE INTEGER);";
        return sql;
    }

    private void insertEvent(SQLiteDatabase db, Event evnt) {
        ContentValues values = new ContentValues();
        values.put("EVENT_NAME", evnt.getEventName());
        values.put("EVENT_DATE", evnt.getMilliDate());
        db.insert("EVENT_MASTER", null, values);
    }

}
