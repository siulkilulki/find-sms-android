package com.example.siulkilulki.findsmsmessage;

/**
 * Created by siulkilulki on 13.01.16.
 */

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

/**
 *   Helper class to handle all the callbacks that occur when interacting with loaders.
 */
public class SmsLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    Context mContext;
    public static final String QUERY_KEY = "query";
    public static final String TAG = "SmsLoaderCallbacks";

    public SmsLoaderCallbacks(Context context) { mContext = context; }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderIndex, Bundle args) {
        // BEGIN_INCLUDE(uri_with_query)
        String query = args.getString(QUERY_KEY);
        Uri inboxUri = Uri.parse("content://sms");
        //Uri uri = Uri.withAppendedPath(inboxUri, query); content://sms/"inbox lub sent"
        // END_INCLUDE(uri_with_query)
        Log.i(TAG, ("i am in onCreateLoader"));
        return new CursorLoader(
            mContext,   // Context
            inboxUri,   // URI representing the table/resource to be queried
            null,       // projection - the list of columns to return.  Null means "all"
            null,       // selection - Which rows to return (condition rows must match)
            null,       // selection args - can be provided separately and subbed into selection.
            null);      // string specifying sort order
        // END_INCLUDE(cursor_loader)

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        Log.i(TAG, (String.valueOf(cursor.getCount())));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }


}
