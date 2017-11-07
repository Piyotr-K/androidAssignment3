package ca.bcit.ass3.katz_kao;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        ListView list_continents = (ListView) findViewById(R.id.list_events);

        String[] continents = getEvents();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, continents
        );

        list_continents.setAdapter(arrayAdapter);

        /*
        list_continents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view;
                String continent = tv.getText().toString();

                Intent intent = new Intent(MainActivity.this, CountryActivity.class);
                intent.putExtra("continent", continent);

                startActivity(intent);
            }
        });
        */
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
            Cursor cursor = db.rawQuery("select EVENT_NAME CONTINENT from EVENT_MASTER", null);

            int count = cursor.getCount();
            events = new String[count];

            if (cursor.moveToFirst()) {
                int ndx = 0;
                do {
                    events[ndx++] = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getContinents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }

        return events;
    }
}
