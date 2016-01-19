package com.example.siulkilulki.findsmsmessage;

/**
 * Created by siulkilulki on 13.01.16.
 */

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

/**
 *   Helper class to handle all the callbacks that occur when interacting with loaders.
 */
public class SmsLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Sms>> {
    Context mContext;
    SmsAdapter mSmsAdapter;
    ListView mListView;
    public static final String QUERY_KEY = "query";
    public static final String TAG = "SmsLoaderCallbacks";

    public SmsLoaderCallbacks(Context mContext, SmsAdapter mSmsAdapter, ListView mListView) {
        this.mContext = mContext;
        this.mSmsAdapter = mSmsAdapter;
        this.mListView = mListView;
    }

    @Override
    public Loader<List<Sms>> onCreateLoader(int loaderIndex, Bundle args) {
        String[] bundleData = args.getStringArray(QUERY_KEY);
        Log.i(TAG, "In onCreateLoader");
        return new SmsMmsLoader(mContext, bundleData);
        /*return new CursorLoader(
            mContext,   // Context
            uri,   // URI representing the table/resource to be queried
            projection,       // projection - the list of columns to return.  Null means "all"
            selection,       // selection - Which rows to return (condition rows must match)
            selectionArgs,       // selection args - can be provided separately and subbed into selection.
            null);      // string specifying sort order
        // END_INCLUDE(cursor_loader)*/

    }

    @Override
    public void onLoadFinished(Loader<List<Sms>> arg0, List<Sms> mSmsList) {
        Log.i(TAG, (String.valueOf(mSmsList.size())));
        if (mSmsList.size() == 0) {
            Log.i(TAG, "zero elements in mSmsBundle");
            // if no results (SmsBundle list empty) change layout to no_results_view.xml
            Activity a = (Activity) mContext;
            a.setContentView(R.layout.no_results_view);
            return;
        }
        mSmsAdapter.setData(mSmsList);//swapCursor(mSmsBundle);
        mListView.setAdapter(mSmsAdapter); // sets adapter when I'm sure the data is loaded


    }

    @Override
    public void onLoaderReset(Loader<List<Sms>> smmMmsLoader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        Log.d(TAG, "Loader reset");
        mSmsAdapter.setData(null);
    }


}
