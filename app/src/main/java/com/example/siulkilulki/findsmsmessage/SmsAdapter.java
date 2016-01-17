package com.example.siulkilulki.findsmsmessage;


import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by siulkilulki on 14.01.16.
 */

public class SmsAdapter extends BaseAdapter {
    private Context mContext;
    private SmsBundle mSmsBundle;
    private static final String TAG = "SmsAdapter";

    //R.layout.queried_message_row

    public SmsAdapter(Context mContext) {
        this.mContext = mContext;
        this.mSmsBundle = new SmsBundle();
        Log.d(TAG, "SmsAdapter contructor fired");
    }

    public void setData(SmsBundle smsBundle) {
        Log.d(TAG, "Data set");
        this.mSmsBundle = smsBundle;
    }
    @Override
    public int getCount() {
        Log.d(TAG, "Get Count fired");
        /*if (mSmsBundle == null)
            return 0;
        if (mSmsBundle.list == null)
            return 0;*/
        Log.d(TAG, "Get Count == " + String.valueOf(mSmsBundle.list.size()));
        return mSmsBundle.list.size();
    }

    @Override
    public Sms getItem(int position) {
        return mSmsBundle.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.d(TAG, "getView fired");
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.queried_message_row, parent, false);
            holder = new ViewHolder();
            holder.body = (TextView) convertView.findViewById(R.id.sms_text);
            holder.phoneNr = (TextView) convertView.findViewById(R.id.sms_label);
            convertView.setTag(holder);
        } else {
            // Reuse old views
            holder = (ViewHolder) convertView.getTag();
        }
        Log.d(TAG, String.valueOf(position));
        bindSmsToView(holder, getItem(position));
        return convertView;
    }
    private void bindSmsToView(ViewHolder holder, Sms sms){
        Log.d(TAG, "bindsmstoview");
        holder.body.setText(sms.body);
        holder.phoneNr.setText(sms.phoneNr);
    }
    static class ViewHolder {
        TextView body;
        TextView phoneNr;
    }

}
