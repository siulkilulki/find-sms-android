package com.example.siulkilulki.findsmsmessage;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by siulkilulki on 17.01.16.
 */
public class SmsDataOrganizer {
   /* public HashMap<String, > contactsToHashMap (){

    }
    public boolean areNumbersEqual(String contactNumber, String smsNumber) {
    }*/
    public String prettifyNumber(String phoneNumber) {
        if (phoneNumber.charAt(0) == '+') {
            phoneNumber = phoneNumber.substring(phoneNumber.length()-9);
        }
        return phoneNumber.replaceAll("[^0-9]+","");
    }
    public HashMap<String, Object[]> hashContacts(Cursor contactsCursor) {
        HashMap<String,Object[]> hashMap = new HashMap<>();
        contactsCursor.moveToFirst();
        int numberIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int nameIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int contactIdIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        int photoUriIndex = contactsCursor.getColumnIndex(ContactsContract.
                CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);;

        do {
            /*Log.d("Sms", contactsCursor.getString(nameIndex));
            String[] col = contactsCursor.getColumnNames();
            for (String i : col
                 ) {
                Log.d("Sms", i);
            }*/
            hashMap.put(prettifyNumber(contactsCursor.getString(numberIndex)), new Object[]{
                            contactsCursor.getLong(contactIdIndex),
                            contactsCursor.getString(nameIndex),
                            contactsCursor.getString(photoUriIndex)}
            );
        } while (contactsCursor.moveToNext());
        contactsCursor.close();
        return hashMap;
    }
}
