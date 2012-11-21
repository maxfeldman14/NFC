package com.cs261.vulnerapp;

import android.app.*;
import android.nfc.*;
import android.os.Bundle;
import android.nfc.tech.Ndef;
import android.widget.TextView;
import android.util.Log;
import java.io.IOException;

import org.cs261.NdefSec.SignedNdef;

public class WriteTagActivity extends Activity
{
    private static final String TAG = "WriteTagActivity";
    public boolean writeUri(Tag tag, String uri) {
        SignedNdef classic = SignedNdef.get(tag);
        try {
            classic.connect();
            NdefRecord record = NdefRecord.createUri(uri);
            NdefMessage msg = new NdefMessage(record);
            classic.writeNdefMessage(msg);
        } catch (IOException e) {
            Log.e(TAG, "IOException while closing Ndef...", e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Exception while closing Ndef...", e);
            return false;
        } finally {
            try {
                classic.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing Ndef...", e);
                return false;
            }
        }
        return true;
    }

    public void readTag(Tag tag, TextView text) throws Exception {
        SignedNdef ndef = SignedNdef.get(tag);
        ndef.connect();
        NdefMessage message = ndef.getNdefMessage();
        text.setText("Contents: " + message.describeContents());
        ndef.close();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_tag);

        TextView text = (TextView) findViewById(R.id.TextView00);

        Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG); 

        //String uri = "http://www.google.com/Maecenas-quam-dolor,-sollicitudin-eu-tristique-vel,-vestibulum-nec-lacus.-Class-aptent-taciti-sociosqu-ad-litora-torquent-per-conubia-nostra,-per-inceptos-himenaeos.-Etiam-laoreet-leo-vitae-quam-venenatis-at-lacinia-elit-adipiscing.-Sed-bibendum-pharetra-quam,-sit-amet-mollis-sapien-hendrerit-vel";
        String uri = "http://www.google.com/";
        //if (writeUri(tag, uri)) {
        //    text.setText("Success");
        //} else {
        //    text.setText("Failure");
        //}

        try {
            readTag(tag, text);
        } catch (Exception e) {
            Log.e(TAG, "Exception readTag...", e);
            text.setText("Failed");
        }
    }
}
