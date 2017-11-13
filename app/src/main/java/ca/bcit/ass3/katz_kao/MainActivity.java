package ca.bcit.ass3.katz_kao;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private SQLiteOpenHelper helper = null;
    private Cursor cursor = null;
    private static String loadSql = "select _eventId _id, * FROM EVENT_MASTER;";
    private String textIn1, textIn2;
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
        cursor = db.rawQuery(loadSql, null);
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
    private void addEvent(String eventName, String dateTime) {
        try {
            ContentValues values = new ContentValues();
            values.put("EVENT_NAME", eventName);
            values.put("EVENT_DATE", dateTime);
            db.insert("EVENT_MASTER", null, values);
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        cursor.requery();
    }

    @SuppressWarnings("deprecation")
    private void deleteEvent(String eventName) {
        try {
            db.execSQL("DELETE FROM EVENT_MASTER WHERE EVENT_NAME LIKE '" + eventName + "%';");
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        cursor.requery();
    }

    @SuppressWarnings("deprecation")
    private void findEvent(String eventName) {
        try {
            loadSql = "SELECT _eventId _id, * FROM EVENT_MASTER WHERE EVENT_NAME LIKE '%" + eventName + "%';";
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getEvents] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
        cursor.requery();
        MainActivity.this.recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder;
        final Context cur = this;
        final EditText input;
        final EditText input2;
        final EditText input3;

        final Calendar calendar = Calendar.getInstance();

        switch (item.getItemId()) {
            case R.id.menu_add:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Add an event");
                View mView = getLayoutInflater().inflate(R.layout.dialog_event_layout, null);
                // Set up the input
                input = mView.findViewById(R.id.dialog_entry_name);
                input2 = mView.findViewById(R.id.dialog_entry_date);
                input3 = mView.findViewById(R.id.dialog_entry_time);
                builder.setView(mView);

                input2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String formatDate = "MMMM dd, yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.CANADA);
                                input2.setText(sdf.format(calendar.getTime()));
                            }
                        };
                        new DatePickerDialog(ctx, date, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                input3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePicker = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String AM_PM = "AM";
                                if (selectedHour >= 12) {
                                    AM_PM = "PM";
                                }
                                input3.setText(selectedHour + ":" + selectedMinute + " " + AM_PM);
                            }
                        }, hour, minute, true);
                        timePicker.setTitle("Select Time");
                        timePicker.show();
                    }
                });

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textIn1 = input.getText().toString();
                        textIn2 = input2.getText().toString() + " " + input3.getText().toString();
                        Toast.makeText(cur, "Entered: " + textIn1, Toast.LENGTH_SHORT).show();
                        addEvent(textIn1, textIn2);
                        MainActivity.this.recreate();
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
                builder.setTitle("Enter an event name");

                // Set up the input
                input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textIn1 = input.getText().toString();
                        Toast.makeText(cur, "Entered: " + textIn1, Toast.LENGTH_SHORT).show();
                        deleteEvent(textIn1);
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
                input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textIn1 = input.getText().toString();
                        Toast.makeText(cur, "Entered: " + textIn1, Toast.LENGTH_SHORT).show();
                        findEvent(textIn1);
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
