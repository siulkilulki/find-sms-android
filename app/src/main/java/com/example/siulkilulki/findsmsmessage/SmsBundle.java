package com.example.siulkilulki.findsmsmessage;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by siulkilulki on 17.01.16.
 */
public class SmsBundle implements Serializable {
    public HashMap<Integer, Bitmap> photos;
    public List<Sms> list;

    public SmsBundle() {
        this.list = new ArrayList<>();
        this.photos = new HashMap<>();
    }
}
