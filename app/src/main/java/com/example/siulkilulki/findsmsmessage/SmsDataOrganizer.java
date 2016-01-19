package com.example.siulkilulki.findsmsmessage;

import android.database.Cursor;
import android.provider.ContactsContract;

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
    public HashMap<String, Tuple> hashContacts(Cursor contactsCursor) {
        HashMap<String,Tuple> hashMap = new HashMap<>();
        contactsCursor.moveToFirst();
        int numberIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int nameIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int contactIdIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        int photoUriIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);;

        do {

            hashMap.put(prettifyNumber(contactsCursor.getString(numberIndex)), new Tuple<Long,String>(
                    contactsCursor.getLong(contactIdIndex), contactsCursor.getString(nameIndex)));
        } while (contactsCursor.moveToNext());
        contactsCursor.close();
        return hashMap;
    }
}
