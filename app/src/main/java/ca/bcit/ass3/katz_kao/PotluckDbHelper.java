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
        try {
            if (oldVersion < 1) {
                db.execSQL(getCreatePotluckTableSql());
                insertEvent(db, new Event("Halloween Party", new Date()));
                insertEvent(db, new Event("Christmas Party", new Date()));
                insertEvent(db, new Event("New Years Party", new Date()));
            }
        } catch (SQLException sqle) {
            String msg = "[MyPlanetDbHelper / updateMyDatabase/insertCountry] DB unavailable";
            msg += "\n\n" + sqle.toString();
            Toast t = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
            t.show();
        }
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
