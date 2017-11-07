package ca.bcit.ass3.katz_kao;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent i = getIntent();
        String eventName = i.getStringExtra("event");
        TableLayout table = (TableLayout) findViewById(R.id.itemTable);

        if (eventName.equalsIgnoreCase("christmas party")) {
            TextView tv = new TextView(this);
            tv.setText("Christmas");
            ListView list_items = (ListView) findViewById(R.id.testItemList);

            String[] itemNames = getItemNames("Christmas");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, itemNames
            );

            list_items.setAdapter(arrayAdapter);
            table.addView(tv);
        }
    }

    private String[] getItemNames(String evntName) {
        SQLiteOpenHelper helper = new PotluckDbHelper(this);
        String[] itemNames = null;
        try {
            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select DISTINCT ItemName from EVENT_DETAIL WHERE eventId = 1;", null);

            int count = cursor.getCount();
            itemNames = new String[count];

            if (cursor.moveToFirst()) {
                int ndx = 0;
                do {
                    itemNames[ndx++] = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getItemNames] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }

        return itemNames;
    }
}
