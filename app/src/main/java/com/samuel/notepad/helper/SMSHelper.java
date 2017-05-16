package com.samuel.notepad.helper;

import android.telephony.SmsManager;

/**
 * Static methods to encapsulate the logic behind
 * sending an SMS with the Notepad app
 *
 * Created by samuel on 5/15/17.
 */

public class SMSHelper {

    public static void sendSMS(String body, String... destinations) {
        SmsManager manager = SmsManager.getDefault();
        for(String destination : destinations) {
            manager.sendTextMessage(destination, null, body, null, null);
        }
    }
}
