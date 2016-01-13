package com.example.siulkilulki.findsmsmessage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by siulkilulki on 12.01.16.
 */
public class SmsProvider {
    // Create Inbox box URI
    /*public Cursor InboxProvider(Context context){
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqCols = new String[] { "_id", "address", "body" };

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = context.getContentResolver();

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, null, null, null, null);

    }*/


}
