package com.cs261.vulnerapp;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;

public class VulnerappActivity extends Activity {
    /** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
      
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
  }

  
  
}
