package com.example.siulkilulki.findsmsmessage;

import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.HashMap;

/**
 * Created by siulkilulki on 17.01.16.
 */
public class SmsDataOrganizer {

    public String prettifyNumber(String phoneNumber) {
        if (phoneNumber.charAt(0) == '+') {
            phoneNumber = phoneNumber.substring(phoneNumber.length()-9);
        }
        return phoneNumber.replaceAll("[^0-9]+","");
    }

    /**
     * Creates hashmap to quickly populate messages with contact data.
     * @param contactsCursor
     * @return Returns HashMap with phones as key, and contact data as value.
     */
    public HashMap<String, Object[]> hashContacts(Cursor contactsCursor) {
        HashMap<String,Object[]> hashMap = new HashMap<>();
        contactsCursor.moveToFirst();
        int numberIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int nameIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int contactIdIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        int photoUriIndex = contactsCursor.getColumnIndex(ContactsContract.
                CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);;

        do {
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
