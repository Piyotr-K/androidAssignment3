package ca.bcit.ass3.katz_kao;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private SQLiteOpenHelper helper = null;
    private Cursor cursor = null;
    private String mText = "";
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;

        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);

        ListView list_events = (ListView) findViewById(R.id.list_events);

        helper = new PotluckDbHelper(ctx);
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("select _eventId _id, * FROM EVENT_MASTER;", null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(ctx,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] {"EVENT_NAME", "EVENT_DATE"},
                new int[] { android.R.id.text1, android.R.id.text2 },
                0);

        list_events.setAdapter(adapter);

        list_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TwoLineListItem list = (TwoLineListItem) view;
                TextView tv = list.getText1();
                String event = tv.getText().toString();

                System.out.println(event);

                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra("event", event);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cursor != null) {
            cursor.close();
        }

        if (db != null) {
            db.close();
        }
    }

    @SuppressWarnings("deprecation")
    private void addEvent(String eventName) {
        try {
            ContentValues values = new ContentValues();
            values.put("EVENT_NAME", eventName);
            values.put("EVENT_DATE", "2017-12-12 12:12:12");
            db.insert("EVENT_MASTER", null, values);
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        cursor.requery();
    }

    private void deleteEvent(String eventName) {
        try {
            db.rawQuery("DELETE FROM EVENT_MASTER WHERE EVENT_NAME LIKE '%" + eventName + "%';", null);
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter an event name");
                final Context cur = this;

                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mText = input.getText().toString();
                        Toast.makeText(cur, "Entered: " + mText, Toast.LENGTH_SHORT).show();
                        addEvent(mText);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                Toast.makeText(this, "Add Item Clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
