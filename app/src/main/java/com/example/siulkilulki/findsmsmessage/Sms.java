package com.example.siulkilulki.findsmsmessage;

import java.io.Serializable;

/**
 * Created by siulkilulki on 14.01.16.
 */
public class Sms implements Serializable {
    private int id;
    private String phoneNr;
    private String date;
    private String date_sent;
    private String body;

    public Sms(int id, String phoneNr, String date, String date_sent, String body) {
        this.id = id;
        this.phoneNr = phoneNr;
        this.date = date;
        this.date_sent = date_sent;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public String getDate() {
        return date;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public String getBody() {
        return body;
    }
}
