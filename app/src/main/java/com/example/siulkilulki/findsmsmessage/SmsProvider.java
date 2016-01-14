package com.example.siulkilulki.findsmsmessage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siulkilulki on 12.01.16.
 */
public class SmsProvider {
    private static List<Sms> smses = new ArrayList<>();
    public SmsProvider(Context context, List<Sms> smsList) {
        this.smses = smsList;
    }

    public List<Sms> getSmses() {
        return smses;
    }

    public void setSmses(List<Sms> smses) {
        this.smses = smses;
    }
}
