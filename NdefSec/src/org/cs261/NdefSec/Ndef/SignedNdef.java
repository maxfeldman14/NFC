package org.cs261.NdefSec.Ndef;

import java.io.IOException;

import android.nfc.Tag;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.TagTechnology;
import android.nfc.tech.Ndef;
import android.app.Activity;
import android.content.*;

import org.cs261.NdefSec.KeyMgmt.*;

/* Probably refactor these */
import java.security.*;
import java.security.spec.*;

public class SignedNdef implements TagTechnology
{
    private Ndef ndef;
    private NdefMessage ndefMessage;
    private SharedPreferences prefs;
    private KeyManager mgr;

    public static SignedNdef get(Tag tag, SharedPreferences prefs) throws GeneralSecurityException
    {
        return new SignedNdef(tag, prefs);
    }

    protected SignedNdef(Tag tag, SharedPreferences preferences) throws GeneralSecurityException
    {
        ndef = Ndef.get(tag);
        this.prefs = preferences;
        KeyFile keyFile = new KeyFile(prefs);
        mgr = new KeyManager(keyFile);
        
    }

    /* TagTechnology methods */

    public void close() throws IOException { ndef.close(); }

    public void connect() throws IOException { ndef.connect(); }

    public Tag getTag() { return ndef.getTag(); }

    public boolean isConnected() { return ndef.isConnected(); }

    /* Ndef methods */

    public boolean canMakeReadOnly() throws Exception { return ndef.canMakeReadOnly(); }

    public int getMaxSize() { return ndef.getMaxSize(); }

    public String getType() { return ndef.getType(); }

    public boolean isWritable() { return ndef.isWritable(); }

    public boolean makeReadOnly() throws Exception { return ndef.makeReadOnly(); }

    /* Overriding Ndef methods */

    public NdefMessage getCachedNdefMessage()
    {
        return ndefMessage;
    }

    public void writeNdefMessage(NdefMessage msg) throws Exception
    {
        NdefRecord[] oldNdefRecords = msg.getRecords();

        KeyPair keyPair = staticKey();
        NdefRecord signature = generateSignature(msg, keyPair);

        NdefRecord[] ndefRecords = new NdefRecord[oldNdefRecords.length+1];
        System.arraycopy(oldNdefRecords, 0, ndefRecords, 0, oldNdefRecords.length);
        ndefRecords[ndefRecords.length-1] = signature;

        NdefMessage newMsg = new NdefMessage(ndefRecords);

        ndef.writeNdefMessage(newMsg);
    }

    public NdefMessage getNdefMessage() throws Exception
    {
        NdefMessage originalMessage = ndef.getNdefMessage();
        NdefRecord[] records = originalMessage.getRecords();
        NdefRecord signatureRecord = records[records.length-1];

        KeyPair keyPair = staticKey();

        NdefRecord[] newRecords = new NdefRecord[records.length-1];
        System.arraycopy(records, 0, newRecords, 0, newRecords.length);
        NdefMessage newMessage = new NdefMessage(newRecords);

        if (verifySignature(newMessage, keyPair, signatureRecord)) {
            return newMessage;
        } else {
            throw new Exception("Could not verify");
        }
    }

    private KeyPair staticKey() throws Exception
    {
        return mgr.getStaticKeys();
    }

    private NdefRecord generateSignature(NdefMessage message, PrivateKey key) throws Exception
    {
        Signature dsa = Signature.getInstance("SHA1withDSA");
        dsa.initSign(key);
        byte[] data = message.toByteArray();
        dsa.update(data, 0, data.length);
        byte[] sig = dsa.sign();

        NdefRecord signature = NdefRecord.createMime("application/pkcs7-signature", sig);
        return signature;
    }

    private NdefRecord generateSignature(NdefMessage message, KeyPair keyPair) throws Exception
    {
        return generateSignature(message, keyPair.getPrivate());
    }

    private boolean verifySignature(NdefMessage message, PublicKey key, NdefRecord signatureRecord) throws Exception
    {
        Signature dsa = Signature.getInstance("SHA1withDSA");
        dsa.initVerify(key);
        byte[] data = message.toByteArray();
        dsa.update(data, 0, data.length);
        byte[] signature = signatureRecord.getPayload();
        return dsa.verify(signature);
    }
        
    private boolean verifySignature(NdefMessage message, KeyPair keyPair, NdefRecord signatureRecord) throws Exception
    {
        return verifySignature(message, keyPair.getPublic(), signatureRecord);
    }
}
