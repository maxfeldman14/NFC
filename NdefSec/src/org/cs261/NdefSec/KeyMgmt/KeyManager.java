package org.cs261.NdefSec.KeyMgmt;

import java.security.*;
import java.security.spec.*;
import java.util.HashMap;
import java.math.*;

public class KeyManager
{
    protected KeyPair staticKeys;
    // TODO: Use Android's shared preferences:
    // http://developer.android.com/reference/android/content/SharedPreferences.html
    // in the meantime, a hack using a hashmap
    
    protected HashMap<String, PublicKey> keyFile;
     
    public KeyManager() throws GeneralSecurityException
    {
        /* Generate a static key pair for testing purposes */
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        byte[] privKey = {48, -126, 1, 75, 2, 1, 0, 48, -126, 1, 44, 6, 7, 42, -122, 72, -50, 56, 4, 1, 48, -126, 1, 31, 2, -127, -127, 0, -3, 127, 83, -127, 29, 117, 18, 41, 82, -33, 74, -100, 46, -20, -28, -25, -10, 17, -73, 82, 60, -17, 68, 0, -61, 30, 63, -128, -74, 81, 38, 105, 69, 93, 64, 34, 81, -5, 89, 61, -115, 88, -6, -65, -59, -11, -70, 48, -10, -53, -101, 85, 108, -41, -127, 59, -128, 29, 52, 111, -14, 102, 96, -73, 107, -103, 80, -91, -92, -97, -97, -24, 4, 123, 16, 34, -62, 79, -69, -87, -41, -2, -73, -58, 27, -8, 59, 87, -25, -58, -88, -90, 21, 15, 4, -5, -125, -10, -45, -59, 30, -61, 2, 53, 84, 19, 90, 22, -111, 50, -10, 117, -13, -82, 43, 97, -41, 42, -17, -14, 34, 3, 25, -99, -47, 72, 1, -57, 2, 21, 0, -105, 96, 80, -113, 21, 35, 11, -52, -78, -110, -71, -126, -94, -21, -124, 11, -16, 88, 28, -11, 2, -127, -127, 0, -9, -31, -96, -123, -42, -101, 61, -34, -53, -68, -85, 92, 54, -72, 87, -71, 121, -108, -81, -69, -6, 58, -22, -126, -7, 87, 76, 11, 61, 7, -126, 103, 81, 89, 87, -114, -70, -44, 89, 79, -26, 113, 7, 16, -127, -128, -76, 73, 22, 113, 35, -24, 76, 40, 22, 19, -73, -49, 9, 50, -116, -56, -90, -31, 60, 22, 122, -117, 84, 124, -115, 40, -32, -93, -82, 30, 43, -77, -90, 117, -111, 110, -93, 127, 11, -6, 33, 53, 98, -15, -5, 98, 122, 1, 36, 59, -52, -92, -15, -66, -88, 81, -112, -119, -88, -125, -33, -31, 90, -27, -97, 6, -110, -117, 102, 94, -128, 123, 85, 37, 100, 1, 76, 59, -2, -49, 73, 42, 4, 22, 2, 20, 123, -95, 123, 77, -110, -98, -30, 19, 123, 47, -1, -39, -127, 88, -41, -20, -58, -79, 112, -96};
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        byte[] pubKey = {48, -126, 1, -72, 48, -126, 1, 44, 6, 7, 42, -122, 72, -50, 56, 4, 1, 48, -126, 1, 31, 2, -127, -127, 0, -3, 127, 83, -127, 29, 117, 18, 41, 82, -33, 74, -100, 46, -20, -28, -25, -10, 17, -73, 82, 60, -17, 68, 0, -61, 30, 63, -128, -74, 81, 38, 105, 69, 93, 64, 34, 81, -5, 89, 61, -115, 88, -6, -65, -59, -11, -70, 48, -10, -53, -101, 85, 108, -41, -127, 59, -128, 29, 52, 111, -14, 102, 96, -73, 107, -103, 80, -91, -92, -97, -97, -24, 4, 123, 16, 34, -62, 79, -69, -87, -41, -2, -73, -58, 27, -8, 59, 87, -25, -58, -88, -90, 21, 15, 4, -5, -125, -10, -45, -59, 30, -61, 2, 53, 84, 19, 90, 22, -111, 50, -10, 117, -13, -82, 43, 97, -41, 42, -17, -14, 34, 3, 25, -99, -47, 72, 1, -57, 2, 21, 0, -105, 96, 80, -113, 21, 35, 11, -52, -78, -110, -71, -126, -94, -21, -124, 11, -16, 88, 28, -11, 2, -127, -127, 0, -9, -31, -96, -123, -42, -101, 61, -34, -53, -68, -85, 92, 54, -72, 87, -71, 121, -108, -81, -69, -6, 58, -22, -126, -7, 87, 76, 11, 61, 7, -126, 103, 81, 89, 87, -114, -70, -44, 89, 79, -26, 113, 7, 16, -127, -128, -76, 73, 22, 113, 35, -24, 76, 40, 22, 19, -73, -49, 9, 50, -116, -56, -90, -31, 60, 22, 122, -117, 84, 124, -115, 40, -32, -93, -82, 30, 43, -77, -90, 117, -111, 110, -93, 127, 11, -6, 33, 53, 98, -15, -5, 98, 122, 1, 36, 59, -52, -92, -15, -66, -88, 81, -112, -119, -88, -125, -33, -31, 90, -27, -97, 6, -110, -117, 102, 94, -128, 123, 85, 37, 100, 1, 76, 59, -2, -49, 73, 42, 3, -127, -123, 0, 2, -127, -127, 0, -55, -30, -75, -59, 50, 29, -83, 67, -127, 74, -18, 88, 91, -18, 52, 25, -74, -58, 27, -115, 39, -99, 114, 0, 77, -42, 70, 118, -125, -83, 42, -125, -120, 29, 102, -109, -21, -33, -33, 21, 22, -7, 122, -120, 109, 89, -38, -43, 11, 1, 77, 7, -53, 69, 44, -25, 85, -33, 57, -114, 60, -72, -14, 65, 96, -29, 68, -9, 126, 112, -75, -88, -27, 94, 30, 84, 41, 45, -8, -3, -5, -58, -74, 18, 47, -100, -61, -89, -118, -123, -77, -126, -23, -104, 41, 103, -22, -12, 4, -114, -3, -47, 28, -125, 121, 13, 68, -13, 105, -102, 77, 75, -88, 16, 25, -20, 48, 3, -84, 66, -16, -88, -87, -80, 39, 27, 60, -48};
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pubKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        staticKeys = keyPair;
        
        /* Fetch the keyfile */
        // TODO: refactor or use the actual android library
        keyFile = new HashMap<String, PublicKey>();
    }

    public KeyPair getStaticKeys()
    {
        // Return the static keypair (created in the constructor) for testing.
        return staticKeys;
    }

    public void addKey(PublicKey pub) 
    {
        // Add a key to the trusted keys.
        keyFile.put(fingerprint(pub), pub);
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
            return keyFile.get(fingerprint);
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


