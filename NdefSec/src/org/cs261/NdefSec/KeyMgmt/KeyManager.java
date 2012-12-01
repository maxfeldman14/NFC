package org.cs261.NdefSec.KeyMgmt;

import java.security.*;
import java.security.spec.*;
import java.util.HashMap;
import java.math.*;
import org.cs261.NdefSec.KeyMgmt.StaticKeyPair;

public class KeyManager
{
    // http://developer.android.com/reference/android/content/SharedPreferences.html
    // in the meantime, a hack using a hashmap
    
    protected KeyFile keyFile;
     
    public KeyManager(KeyFile inKeyFile) throws GeneralSecurityException
    {
        /* Generate a static key pair for testing purposes */
        keyFile = inKeyFile;

    }

    public KeyPair getStaticKeys()
    {
        // Return the static keypair (created in the constructor) for testing.
        return StaticKeyPair.getStaticKeyPair();
    }

    public void addKey(PublicKey pub) 
    {
        // Add a key to the trusted keys.
        keyFile.put(fingerprint(pub), pubKeyToString(pub));
    }

    public void revokeKey(PublicKey pub)
    {
        // Remove a key from the trusted keys. Do nothing if it was not already
        // trusted. First get the fingerprint, for removal.
        keyFile.remove(fingerprint(pub));       

    }

    public PublicKey lookup(String fingerprint) throws KeyException
    {
        // Find a public key in the trusted keys based on the fingerprint.
        // Throw exception if key not found.
        if (keyFile.containsKey(fingerprint)) {
            return stringToPubKey(keyFile.get(fingerprint));
        } else 
        {
            throw new KeyException("Key not in keyfile");
        }

    }

    public String fingerprint(PublicKey pub)
    {
        // Hash a public key to generate a fingerprint, using MD5
        // Get encoded bytes of key, hash them, return string of hex digest
        try {
        byte[] bytes = MessageDigest.getInstance("MD5").digest(pub.getEncoded());
        String fingerprint = new BigInteger(1, bytes).toString(16);
        return fingerprint;
        } catch (NoSuchAlgorithmException e) {
            // TODO: what should standard behavior be?
            e.printStackTrace();
            return null;
        }
    }
    
    public String pubKeyToString(PublicKey pub)
    {
        // Return the string representation of a public key
        byte[] pubBytes = pub.getEncoded();
        BigInteger pubBigInt = new BigInteger(pubBytes);
        String pubString = pubBigInt.toString(16);
        return pubString;
    }
    
    public PublicKey stringToPubKey (String pub)
    {
        // Construct a public key from the string representation of the key
        // The string representation is the string of the BigInteger
        // representing the encoded bytes of the key
        BigInteger pubBigInt = new BigInteger(pub, 16);
        byte[] pubBytes = pubBigInt.toByteArray();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubBytes);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
            return pubKey;

        } catch (GeneralSecurityException e) {
            // (either invalid spec, or nosuchalgorithm
            e.printStackTrace();
        }
        return null;

    }

}



