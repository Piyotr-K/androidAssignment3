package ca.bcit.ass3.katz_kao;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);

        ListView list_events = (ListView) findViewById(R.id.list_events);

        String[] events = getEvents();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, events
        );

        list_events.setAdapter(arrayAdapter);

        list_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view;
                String event = tv.getText().toString();

                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra("event", event);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(cursor != null) {
            cursor.close();
        }

        if(db != null) {
            db.close();
        }
    }

    private String[] getEvents() {
        SQLiteOpenHelper helper = new PotluckDbHelper(this);
        String[] events = null;
        try {
            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select DISTINCT EVENT_NAME from EVENT_MASTER", null);

            int count = cursor.getCount();
            events = new String[count];

            if (cursor.moveToFirst()) {
                int ndx = 0;
                do {
                    events[ndx++] = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }

        return events;
    }
}
