package com.yamanyar.sforward;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        final SharedPreferences settings = context.getSharedPreferences(SmsForwarderConfig.APP_SET_NAME, Context.MODE_PRIVATE);
        boolean isActive = settings.getBoolean(SmsForwarderConfig.KEY_IS_ENABLED, false);
        final boolean isRemoteEnabled = settings.getBoolean(SmsForwarderConfig.KEY_IS_AUTO, false);
        final String telNumber = settings.getString(SmsForwarderConfig.KEY_SMS_NO, "");

        searchRemoteReq:
        if (isRemoteEnabled && !isActive) {
            String password = settings.getString(SmsForwarderConfig.PASSWORD, "");
            if (password.trim().length() > 0) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String message = msgs[i].getMessageBody().toString();
                        if (message.contains(password)) {
                            isActive = true;
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean(SmsForwarderConfig.KEY_IS_ENABLED, true);
                            editor.commit();
                            break searchRemoteReq;
                        }
                    }
                }

            }
        }

        if (isActive && telNumber != null && telNumber.length() > 0) {
            Log.v("SmsReceiver", "Is active: " + isActive);
            Log.v("SmsReceiver", "Tel Number: " + telNumber);
            sendSMS(context, intent, telNumber);
        }
    }

    private void sendSMS(Context context, Intent intent, String phoneNumber) {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                SmsManager sms = SmsManager.getDefault();
                String from = msgs[i].getDisplayOriginatingAddress();
                String message = msgs[i].getMessageBody().toString();
                String all = from + ":" + message;
                Log.v("SmsReceiver", "SMS Message: " + all);
                sms.sendTextMessage(phoneNumber, null, all, null, null);
            }

        }
    }


}