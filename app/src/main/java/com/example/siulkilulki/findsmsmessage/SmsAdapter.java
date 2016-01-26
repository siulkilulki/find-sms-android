package com.example.siulkilulki.findsmsmessage;

import android.content.Context;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by siulkilulki on 14.01.16.
 */

/**
 * Custom adapter for listView of queried messages.
 */
class SmsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Sms> mSmsList;
    private static final String TAG = "SmsAdapter";

    SmsAdapter(Context mContext) {
        this.mContext = mContext;
        this.mSmsList = new ArrayList<>();
        Log.d(TAG, "SmsAdapter contructor fired");
    }

    void setData(List<Sms> smsList) {
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

    private static class ViewHolder {
        TextView body;
        TextView name;
        TextView date;
        ImageView photo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.queried_message_row, parent, false);
            holder = new ViewHolder();
            holder.body = (TextView) convertView.findViewById(R.id.sms_text);
            holder.name = (TextView) convertView.findViewById(R.id.sms_label);
            holder.date = (TextView) convertView.findViewById(R.id.date_text);
            holder.photo = (ImageView) convertView.findViewById(R.id.face);
            convertView.setTag(holder);
        } else {
            // Reuse old views
            holder = (ViewHolder) convertView.getTag();
        }
        bindSmsToView(holder, getItem(position));
        return convertView;
    }

    private void bindSmsToView(ViewHolder holder, Sms sms){
        holder.body.setText(sms.bodyColored);
        holder.date.setText(sms.date);
        setName(holder, sms);

        if(sms.photoThumbnailUri != null) {
            Picasso.with(mContext).load(sms.photoThumbnailUri).into(holder.photo);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_face_black_36dp).into(holder.photo);
        }

        // TODO: delete after showing teachers that this is much slower then picasso
        /*if (sms.contactId != null) {
            Bitmap bitmap = getPhoto(sms.contactId);
            if (bitmap != null) {
                holder.photo.setImageBitmap(bitmap);
            } else {
                holder.photo.setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.ic_face_black_36dp));
            }
        } else {
            holder.photo.setImageDrawable(ContextCompat.getDrawable(mContext,
                    R.drawable.ic_face_black_36dp));
        }*/

    }

    private void setName(ViewHolder holder, Sms sms) {
        String smsDirection;
        switch (sms.type) {
            case Telephony.Sms.MESSAGE_TYPE_INBOX:
                smsDirection = "From: ";
                break;
            case Telephony.Sms.MESSAGE_TYPE_SENT:
                smsDirection = "To: ";
                break;
            default:
                smsDirection = "";
                break;
        }
        if (sms.name == null) {
            holder.name.setText(smsDirection + sms.phoneNr);
        } else {
            holder.name.setText(smsDirection + sms.name);
        }
    }



    // TODO: delete after showing teachers that this is much slower then picasso
    /*public Bitmap getPhoto(long contactId) {


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
*/
}
