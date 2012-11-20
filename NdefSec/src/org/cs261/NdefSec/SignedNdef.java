package org.cs261.NdefSec;

import android.nfc.Tag;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.TagTechnology;
import android.nfc.tech.Ndef;

/* Probably refactor these */
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignedNdef implements TagTechnology
{
    private Ndef ndef;
    private NdefMessage ndefMessage;

    public static SignedNdef get(Tag tag)
    {
        return new SignedNdef(tag);
    }

    protected SignedNdef(Tag tag)
    {
        ndef = Ndef.get(tag);
    }

    /* TagTechnology methods */

    public void close() { ndef.close(); }

    public void connect() { ndef.connect(); }

    public Tag getTag() { return ndef.getTag(); }

    public boolean isConnected() { return ndef.isConnected(); }

    /* Ndef methods */

    public boolean canMakeReadOnly() { return ndef.canMakeReadOnly(); }

    public int getMaxSize() { return ndef.getMaxSize(); }

    public String getType() { return ndef.getType(); }

    public boolean isWritable() { return ndef.isWritable(); }

    public boolean makeReadOnly() { return ndef.makeReadOnly(); }

    /* Overriding Ndef methods */

    public NdefMessage getCachedNdefMessage()
    {
        return ndefMessage;
    }

    public NdefMessage getNdefMessage()
    {
        NdefMessage originalMessage = ndef.getNdefMessage();
        /* TODO: Verify originalMessage */
    }

    public void writeNdefMessage(NdefMessage msg)
    {
        NdefRecord[] oldNdefRecords = msg.getRecords();

        KeyPair keyPair = generateKey();
        NdefRecord signature = generateSignature(msg, keyPair);

        NdefRecord[] ndefRecords = new NdefRecord[oldNdefRecords.length+1];
        System.arraycopy(oldNdefRecords, 0, mRecords, 0, ndefRecords.length);
        ndefRecords[ndefRecords.length-1] = signature;

        NdefMessage newMsg = NdefMessage(ndefRecords);

        ndef.writeNdefMessage(newMsg);
    }

    /* TODO: Refactor into its own class */
    private KeyPair generateKey()
    {
        //KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        //byte[] seed = {0};
        //SecureRandom random = SecureRandom(seed);

        //keyGen.initialize(1024, random);

        //KeyPair keyPair = keyGen.generateKeyPair();
        
        //FileInputStream privKeyFIS = new FileInputStream(args[1]);
        //byte[] privKey = new byte[privKeyFIS.available()];
        byte[] privKey = {48, 130, 1, 75, 2, 1, 0, 48, 130, 1, 44, 6, 7, 42, 134, 72, 206, 56, 4, 1, 48, 130, 1, 31, 2, 129, 129, 0, 253, 127, 83, 129, 29, 117, 18, 41, 82, 223, 74, 156, 46, 236, 228, 231, 246, 17, 183, 82, 60, 239, 68, 0, 195, 30, 63, 128, 182, 81, 38, 105, 69, 93, 64, 34, 81, 251, 89, 61, 141, 88, 250, 191, 197, 245, 186, 48, 246, 203, 155, 85, 108, 215, 129, 59, 128, 29, 52, 111, 242, 102, 96, 183, 107, 153, 80, 165, 164, 159, 159, 232, 4, 123, 16, 34, 194, 79, 187, 169, 215, 254, 183, 198, 27, 248, 59, 87, 231, 198, 168, 166, 21, 15, 4, 251, 131, 246, 211, 197, 30, 195, 2, 53, 84, 19, 90, 22, 145, 50, 246, 117, 243, 174, 43, 97, 215, 42, 239, 242, 34, 3, 25, 157, 209, 72, 1, 199, 2, 21, 0, 151, 96, 80, 143, 21, 35, 11, 204, 178, 146, 185, 130, 162, 235, 132, 11, 240, 88, 28, 245, 2, 129, 129, 0, 247, 225, 160, 133, 214, 155, 61, 222, 203, 188, 171, 92, 54, 184, 87, 185, 121, 148, 175, 187, 250, 58, 234, 130, 249, 87, 76, 11, 61, 7, 130, 103, 81, 89, 87, 142, 186, 212, 89, 79, 230, 113, 7, 16, 129, 128, 180, 73, 22, 113, 35, 232, 76, 40, 22, 19, 183, 207, 9, 50, 140, 200, 166, 225, 60, 22, 122, 139, 84, 124, 141, 40, 224, 163, 174, 30, 43, 179, 166, 117, 145, 110, 163, 127, 11, 250, 33, 53, 98, 241, 251, 98, 122, 1, 36, 59, 204, 164, 241, 190, 168, 81, 144, 137, 168, 131, 223, 225, 90, 229, 159, 6, 146, 139, 102, 94, 128, 123, 85, 37, 100, 1, 76, 59, 254, 207, 73, 42, 4, 22, 2, 20, 123, 161, 123, 77, 146, 158, 226, 19, 123, 47, 255, 217, 129, 88, 215, 236, 198, 177, 112, 160};
        privKeyFIS.read(privKey);
        privKeyFIS.close();
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        byte[] pubKey = {48, 130, 1, 184, 48, 130, 1, 44, 6, 7, 42, 134, 72, 206, 56, 4, 1, 48, 130, 1, 31, 2, 129, 129, 0, 253, 127, 83, 129, 29, 117, 18, 41, 82, 223, 74, 156, 46, 236, 228, 231, 246, 17, 183, 82, 60, 239, 68, 0, 195, 30, 63, 128, 182, 81, 38, 105, 69, 93, 64, 34, 81, 251, 89, 61, 141, 88, 250, 191, 197, 245, 186, 48, 246, 203, 155, 85, 108, 215, 129, 59, 128, 29, 52, 111, 242, 102, 96, 183, 107, 153, 80, 165, 164, 159, 159, 232, 4, 123, 16, 34, 194, 79, 187, 169, 215, 254, 183, 198, 27, 248, 59, 87, 231, 198, 168, 166, 21, 15, 4, 251, 131, 246, 211, 197, 30, 195, 2, 53, 84, 19, 90, 22, 145, 50, 246, 117, 243, 174, 43, 97, 215, 42, 239, 242, 34, 3, 25, 157, 209, 72, 1, 199, 2, 21, 0, 151, 96, 80, 143, 21, 35, 11, 204, 178, 146, 185, 130, 162, 235, 132, 11, 240, 88, 28, 245, 2, 129, 129, 0, 247, 225, 160, 133, 214, 155, 61, 222, 203, 188, 171, 92, 54, 184, 87, 185, 121, 148, 175, 187, 250, 58, 234, 130, 249, 87, 76, 11, 61, 7, 130, 103, 81, 89, 87, 142, 186, 212, 89, 79, 230, 113, 7, 16, 129, 128, 180, 73, 22, 113, 35, 232, 76, 40, 22, 19, 183, 207, 9, 50, 140, 200, 166, 225, 60, 22, 122, 139, 84, 124, 141, 40, 224, 163, 174, 30, 43, 179, 166, 117, 145, 110, 163, 127, 11, 250, 33, 53, 98, 241, 251, 98, 122, 1, 36, 59, 204, 164, 241, 190, 168, 81, 144, 137, 168, 131, 223, 225, 90, 229, 159, 6, 146, 139, 102, 94, 128, 123, 85, 37, 100, 1, 76, 59, 254, 207, 73, 42, 3, 129, 133, 0, 2, 129, 129, 0, 201, 226, 181, 197, 50, 29, 173, 67, 129, 74, 238, 88, 91, 238, 52, 25, 182, 198, 27, 141, 39, 157, 114, 0, 77, 214, 70, 118, 131, 173, 42, 131, 136, 29, 102, 147, 235, 223, 223, 21, 22, 249, 122, 136, 109, 89, 218, 213, 11, 1, 77, 7, 203, 69, 44, 231, 85, 223, 57, 142, 60, 184, 242, 65, 96, 227, 68, 247, 126, 112, 181, 168, 229, 94, 30, 84, 41, 45, 248, 253, 251, 198, 182, 18, 47, 156, 195, 167, 138, 133, 179, 130, 233, 152, 41, 103, 234, 244, 4, 142, 253, 209, 28, 131, 121, 13, 68, 243, 105, 154, 77, 75, 168, 16, 25, 236, 48, 3, 172, 66, 240, 168, 169, 176, 39, 27, 60, 208};
        pubKeyFIS.read(pubKey);
        pubKeyFIS.close();
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(pubKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        return keyPair;
    }

    private NdefRecord generateSignature(NdefMessage message, KeyPair keyPair)
    {
        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(keyPair.getPrivate());
        byte[] data = message.toByteArray();
        dsa.update(data, 0, data.length);
        byte[] sig = dsa.sign();

        NdefRecord signature = NdefRecord.createMime("application/pkcs7-signature", sig);
        return signature;
    }
}
