package com.yamanyar.sforward;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * Created by IntelliJ IDEA.
 * User: Kaan
 * Date: 23.May.2010
 * Time: 16:41:41
 * To change this template use File | Settings | File Templates.
 */
public class SmsForwarderConfig extends Activity {
    public static final String KEY_IS_ENABLED = "SmsForwarderConfig_enabled";
    public static final String KEY_IS_AUTO = "SmsForwarderConfig_auto";
    public static final String KEY_SMS_NO = "SmsForwarderConfig_number";
    public static final String APP_SET_NAME = "SmsForwarderConfig_g_cfg";
    public static final String PASSWORD = "SmsForwarderConfig_pass";


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        final SharedPreferences mSettings = getSharedPreferences(APP_SET_NAME, MODE_PRIVATE);
        final Button ok = (Button) findViewById(R.id.ok);
        final Button cancel = (Button) findViewById(R.id.cancel);
        final RadioButton radio_auto_on = (RadioButton) findViewById(R.id.auto_on);
        final RadioButton radio_on = (RadioButton) findViewById(R.id.radio_on);
        final RadioButton radio_off = (RadioButton) findViewById(R.id.radio_off);
        final RadioButton radio__auto_off = (RadioButton) findViewById(R.id.auto_off);
        final TextView mTxtForwardTo = (TextView) findViewById(R.id.telnumber);
        final TextView password = (TextView) findViewById(R.id.psstext);


        //set pre entered values
        if (mSettings.getBoolean(KEY_IS_ENABLED, false)) {
            radio_on.setChecked(true);
            radio_off.setChecked(false);
        } else {
            radio_on.setChecked(false);
            radio_off.setChecked(true);
        }

        if (mSettings.getBoolean(KEY_IS_AUTO, false)) {
            radio_auto_on.setChecked(true);
            radio__auto_off.setChecked(false);
        } else {
            radio_auto_on.setChecked(false);
            radio__auto_off.setChecked(true);
        }
        mTxtForwardTo.setText(mSettings.getString(KEY_SMS_NO, ""));
        password.setText(mSettings.getString(PASSWORD, ""));

        //prepare listener
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String telNumber = mTxtForwardTo.getText().toString();
                final boolean isEnabled = radio_on.isChecked();
                final boolean remoteControl = radio_auto_on.isChecked();


                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(KEY_IS_ENABLED, isEnabled);
                editor.putString(KEY_SMS_NO, telNumber);
                editor.putBoolean(KEY_IS_AUTO, remoteControl);
                editor.putString(PASSWORD, password.getText().toString());
                editor.commit();
                finish();

            }
        });

        cancel.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View view) {
                        finish();
                    }
                }

        );


    }


}
