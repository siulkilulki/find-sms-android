package com.example.siulkilulki.findsmsmessage;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by siulkilulki on 14.01.16.
 */
public class SmsAdapter extends ResourceCursorAdapter {

    public SmsAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Gets elements to populate
        TextView authorText = (TextView) view.findViewById(R.id.sms_label);
        TextView bodyText = (TextView) view.findViewById(R.id.sms_text);

        //Gets data stored in cursor
        String author = cursor.getString(cursor.getColumnIndex("address"));
        String body = cursor.getString(cursor.getColumnIndex("body"));

        //Sets element we wanted to populate
        authorText.setText(author);
        bodyText.setText(body);


    }


}
