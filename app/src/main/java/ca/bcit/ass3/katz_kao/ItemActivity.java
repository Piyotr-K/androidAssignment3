package ca.bcit.ass3.katz_kao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private SQLiteOpenHelper helper;
    private Context ctx;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ctx = this;

        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);

        Intent i = getIntent();
        String eventName = i.getStringExtra("event");
        TableLayout table = (TableLayout) findViewById(R.id.itemTable);

        if (eventName.equalsIgnoreCase("christmas party")) {
            ListView list_items = (ListView) findViewById(R.id.testItemList);

            ListAdapter adapter = getItems("christmas");

            list_items.setAdapter(adapter);
        }
    }

    private ListAdapter getItems(String evntName) {
        helper = new PotluckDbHelper(ctx);
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("select _detailId _id, * FROM EVENT_DETAIL WHERE eventId = 1;", null);
        ListAdapter adapter = new SimpleCursorAdapter(ctx,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{"ItemName", "ItemUnit"},
                new int[]{android.R.id.text1, android.R.id.text2},
                0);
        return adapter;
    }
}
