package com.example.siulkilulki.findsmsmessage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by siulkilulki on 21.01.16.
 */
public class SmsDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener {
    private Sms mSms;
    private TextView body;
    private TextView date;
    private TextView nameNumber;
    private Button buttonBack;
    private float textSize;
    private final static int LARGER = 0;
    private final static int SMALLER = 1;
    private final static String KEY = "sms";

    public SmsDialogFragment() {
    }

    /**
     * Create a new instance of SmsDialogFragment
     */
    static SmsDialogFragment newInstance(Sms sms) {
        SmsDialogFragment f = new SmsDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(KEY, sms);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSms = (Sms) getArguments().getSerializable(KEY);
        int style = DialogFragment.STYLE_NO_TITLE,
            theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sms_dialog_fragment, container, false);
        body = (TextView) v.findViewById(R.id.dialog_body);
        date = (TextView) v.findViewById(R.id.dialog_date);
        nameNumber = (TextView) v.findViewById(R.id.dialog_name_number);
        (body).setText(mSms.bodyColored);
        SimpleDateFormat format = new SimpleDateFormat("d MMM y, HH:mm:ss");
        (date).setText(format.format(mSms.rawDate));
        (nameNumber).setText(mSms.name + " (" + mSms.phoneNr + ")");

        buttonBack = (Button)v.findViewById(R.id.dialog_back_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, go back to owning activity.
                dismiss();
            }
        });

        //sets key listner
        getDialog().setOnKeyListener(this);

        textSize = getSavedFont();
        applyFont(textSize);

        return v;
    }

    @Override
    public void onPause() {
        saveFont(textSize);
        super.onPause();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == event.KEYCODE_VOLUME_DOWN) {
                changeFont(SMALLER);
                return true;
            } else if (keyCode == event.KEYCODE_VOLUME_UP) {
                changeFont(LARGER);
                return  true;
            }
        }
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == event.KEYCODE_VOLUME_UP || keyCode == event.KEYCODE_VOLUME_DOWN) {
                return true;
            }
        }
        return false;
    }



    private void changeFont(int code) {
        if (code == LARGER) {
            if (textSize < 56) {
                textSize += 4;
            }
        } else if (code == SMALLER) {
            if (textSize > 20) {
                textSize -= 4;
            }
        }
        applyFont(textSize);
    }

    private void applyFont(Float fontSize) {
        body.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        nameNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        date.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        buttonBack.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
    }

    private void saveFont(Float fontSize) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(getString(R.string.pref_font_size_key), fontSize);
        editor.commit();
    }

    private float getSavedFont() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        float defaultFontSize = body.getTextSize();
        float fontSize = sharedPref.getFloat(getString(R.string.pref_font_size_key), defaultFontSize);
        return fontSize;
    }
}

