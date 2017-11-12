package ca.bcit.ass3.katz_kao;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by ChickenMan on 11/12/2017.
 */

public class ItemAdapter extends ResourceCursorAdapter {

    public ItemAdapter (Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.item_name);
        name.setText(cursor.getString(cursor.getColumnIndex("ItemName")));

        TextView unit = (TextView) view.findViewById(R.id.item_unit);
        unit.setText(cursor.getString(cursor.getColumnIndex("ItemUnit")));

        TextView amt = (TextView) view.findViewById(R.id.item_quantity);
        amt.setText(cursor.getString(cursor.getColumnIndex("ItemQuantity")));
    }

}