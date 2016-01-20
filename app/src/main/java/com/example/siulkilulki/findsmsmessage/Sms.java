package com.example.siulkilulki.findsmsmessage;

import android.net.Uri;
import java.io.Serializable;

/**
 * Created by siulkilulki on 14.01.16.
 */
public class Sms implements Serializable {
    public int id;
    public Long contactId;
    public String name;
    public String phoneNr;
    public String date;
    public String date_sent;
    public String body;
    public Uri photoThumbnailUri;
}