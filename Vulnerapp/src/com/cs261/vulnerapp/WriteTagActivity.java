package com.cs261.vulnerapp;

import android.app.*;
import android.nfc.*;
import android.os.Bundle;
import android.nfc.tech.Ndef;
import android.widget.TextView;
import android.util.Log;
import java.io.IOException;

public class WriteTagActivity extends Activity
{
    private static final String TAG = "WriteTagActivity";
    public void writeTag(Tag tag, String tagText) {
        Ndef classic = Ndef.get(tag);
        try {
            classic.connect();
            //classic.writeBlock(4, "abcd".getBytes(Charset.forName("US-ASCII")));
        } catch (IOException e) {
            Log.e(TAG, "IOException while closing Ndef...", e);
        } finally {
            try {
                classic.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing Ndef...", e);
            }
        }
    }

    public void readTag(Tag tag, TextView text) {
        Ndef ndef = Ndef.get(tag);
        text.setText("Tag Type: " + ndef.getType());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_tag);

        TextView text = (TextView) findViewById(R.id.TextView00);

        Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG); 

        readTag(tag, text);
    }
}
