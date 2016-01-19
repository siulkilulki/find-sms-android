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
    public HashMap<String, String[]> hashContacts(Cursor contactsCursor) {
        HashMap<String,String[]> hashMap = new HashMap<>();
        contactsCursor.moveToFirst();
        int numberIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int nameIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int contactIdIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

        do {

            hashMap.put(prettifyNumber(contactsCursor.getString(numberIndex)), new String[]
                    {contactsCursor.getString(contactIdIndex), contactsCursor.getString(nameIndex)});
        } while (contactsCursor.moveToNext());
        return hashMap;
    }
}
