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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private SQLiteOpenHelper helper;
    private Context ctx;
    private Cursor cursor;
    private long eventId;
    private String text1, text2, text3;
    private static String loadSql = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ctx = this;

        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);

        Intent i = getIntent();
        String eventName = i.getStringExtra("event");

        findEvent(eventName);

        ListView list_items = (ListView) findViewById(R.id.testItemList);

        ListAdapter adapter = getItems();

        list_items.setAdapter(adapter);
    }

    private void findEvent(String evntName) {
        helper = new PotluckDbHelper(ctx);
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("select _eventId FROM EVENT_MASTER WHERE EVENT_NAME LIKE '%" + evntName + "%' collate nocase;", null);
        cursor.moveToFirst();
        eventId = cursor.getLong(cursor.getColumnIndex("_eventId"));
    }

    private ListAdapter getItems() {
        cursor = db.rawQuery("select _detailId _id, * FROM EVENT_DETAIL WHERE eventId = " + eventId + " AND ItemName LIKE '%" + loadSql + "%';", null);
        cursor.moveToFirst();
        ListAdapter adapter = new ItemAdapter(ctx,
                R.layout.item_layout,
                cursor,
                0);
        return adapter;
    }

    private void addItem(String itemName, String itemUnit, String itemAmt) {
        try {
            ContentValues values = new ContentValues();
            values.put("ItemName", itemName);
            values.put("ItemUnit", itemUnit);
            values.put("ItemQuantity" , Integer.parseInt(itemAmt));
            values.put("eventId", eventId);
            db.insert("EVENT_DETAIL", null, values);
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        refresh();
    }

    private void deleteItem(String eventName) {
        try {
            db.execSQL("DELETE FROM EVENT_DETAIL WHERE ItemName LIKE '%" + eventName + "%';");
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        refresh();
    }

    private void findItem(String eventName) {
        try {
            loadSql = eventName;
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void refresh() {
        ItemActivity.this.recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder;
        final EditText input1;
        final EditText input2;
        final EditText input3;

        switch (item.getItemId()) {
            case R.id.menu_add:
                builder = new AlertDialog.Builder(this);

                builder.setTitle("Enter an item");

                View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);

                // Set up the input
                input1 = mView.findViewById(R.id.dialog_entry_1);
                input2 = mView.findViewById(R.id.dialog_entry_2);
                input3 = mView.findViewById(R.id.dialog_entry_3);

                builder.setView(mView);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text1 = input1.getText().toString();
                        text2 = input2.getText().toString();
                        text3 = input3.getText().toString();
                        addItem(text1, text2, text3);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            case R.id.menu_delete:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter an item name");
                // Set up the input
                input1 = new EditText(this);
                input1.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input1);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text1 = input1.getText().toString();
                        deleteItem(text1);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            case R.id.menu_search:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter an event name");

                // Set up the input
                input1 = new EditText(this);
                input1.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input1);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text1 = input1.getText().toString();
                        findItem(text1);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
