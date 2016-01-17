package com.example.siulkilulki.findsmsmessage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by siulkilulki on 17.01.16.
 */
public class CursorDataProviders {
    private Context mContext;
    private static final String TAG = "SmsCursorDataProviders";
    public CursorDataProviders(Context mContext) {
        this.mContext = mContext;
    }

    public Cursor smsQuery(Uri uri, String query) {
        String selection = "body LIKE ?";
        String[] selectionArgs = {"%"+query+"%"};
        String[] projection = {"_id","address","date","date_sent", "body"};
        Cursor smsCursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        return smsCursor;
    }

    public Cursor getContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = mContext.getContentResolver().query(uri, projection, null, null, null);
        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        people.moveToFirst();
        do {
            String name   = people.getString(indexName);
            String number = people.getString(indexNumber);
            Log.d(TAG, name +" "+number);
            // Do work...
        } while (people.moveToNext());
        return people;
    }
    public Cursor mmsQuery(Uri uri, String query) {

        Cursor mmsCursor = mContext.getContentResolver().query(uri, null, null, null, null);
        //Log.i(mmsTag, String.valueOf(mmsCursor.getCount()));
        String[] columnNames = mmsCursor.getColumnNames();

        for (String str : columnNames
                ) {
            Log.i(TAG,str);
        }
        mmsCursor.moveToFirst();
        do {
            //Log.i("mms",mmsCursor.);
        } while (mmsCursor.moveToNext());
        return mmsCursor;
    }
}
