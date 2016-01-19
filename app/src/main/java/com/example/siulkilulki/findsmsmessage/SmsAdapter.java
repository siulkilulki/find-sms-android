package com.example.siulkilulki.findsmsmessage;


import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by siulkilulki on 14.01.16.
 */

public class SmsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Sms> mSmsList;
    private static final String TAG = "SmsAdapter";

    //R.layout.queried_message_row

    public SmsAdapter(Context mContext) {
        this.mContext = mContext;
        this.mSmsList = new ArrayList<>();
        Log.d(TAG, "SmsAdapter contructor fired");
    }

    public void setData(List<Sms> smsList) {
        Log.d(TAG, "Data set");
        this.mSmsList = smsList;
    }
    @Override
    public int getCount() {
        return mSmsList.size();
    }

    @Override
    public Sms getItem(int position) {
        return mSmsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //Log.d(TAG, "getView fired");
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.queried_message_row, parent, false);
            holder = new ViewHolder();
            holder.body = (TextView) convertView.findViewById(R.id.sms_text);
            holder.name = (TextView) convertView.findViewById(R.id.sms_label);
            convertView.setTag(holder);
        } else {
            // Reuse old views
            holder = (ViewHolder) convertView.getTag();
        }
        //Log.d(TAG, String.valueOf(position));
        bindSmsToView(holder, getItem(position));
        return convertView;
    }
    private void bindSmsToView(ViewHolder holder, Sms sms){
        //Log.d(TAG, "bindsmstoview");
        holder.body.setText(sms.body);
        if (sms.name == null) {
            holder.name.setText(sms.phoneNr);
        } else {
            holder.name.setText(sms.name);
        }
    }
    static class ViewHolder {
        TextView body;
        TextView name;
    }
    public Bitmap openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = mContext.getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    ByteArrayInputStream byteArray = new ByteArrayInputStream(data);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    return bitmap;
                    //return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

}
