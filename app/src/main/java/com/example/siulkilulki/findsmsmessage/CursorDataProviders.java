package com.example.siulkilulki.findsmsmessage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by siulkilulki on 17.01.16.
 */
class CursorDataProviders {
    private Context mContext;
    private static final String TAG = "SmsCursorDataProviders";

    CursorDataProviders(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Queries sms data that matches the given query.
     * @param uri
     * @param query
     * @param regex
     * @return Cursor with sms data.
     */
    Cursor smsQuery(Uri uri, String query, String regex) {
        String selection = null;
        String[] selectionArgs = null;
        switch (regex) {
            case "true":
                selection = "body GLOB ?";
                selectionArgs = new String[]{query};
                break;
            case "false":
                selection = "body LIKE ? ESCAPE '†'";
                selectionArgs = new String[]{"%"+query+"%"};
                break;
        }
        String[] projection = {"_id","address","date","date_sent", "body", "type"};
        Cursor smsCursor = mContext.getContentResolver().query(uri, projection, selection,
                selectionArgs, null);
        return smsCursor;
    }

    /**
     * Gets contacts data
     * @return Cursor with contacts data.
     */
    Cursor getContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI};
        Cursor people = mContext.getContentResolver().query(uri, projection, null, null, null);
        people.moveToFirst();
        return people;
    }

    // TODO: probably wouldn't use that method, delete soon
     Cursor getContact(Uri contactUri) {
        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME,
        ContactsContract.PhoneLookup._ID};
        return mContext.getContentResolver().query(contactUri, projection, null, null, null);
    }

    // TODO: mmsQuery
     Cursor mmsQuery(Uri uri, String query) {
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
