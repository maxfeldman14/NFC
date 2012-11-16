import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class VeriSign {

  public static void main(String[] args) {

    /* VERIfy a DSA SIGNature */

    if (args.length != 3) {
      System.out.println("Usage: VeriSign <publickey> <signature> <data>");
    } else
      try {

        /* Read in the encoded public key from file */

        FileInputStream pubKeyFIS = new FileInputStream(args[0]);
        byte[] encKey = new byte[pubKeyFIS.available()];
        pubKeyFIS.read(encKey);
        pubKeyFIS.close();

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);

        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        /* Read in the bytes from the signature */
        FileInputStream sigFIS = new FileInputStream(args[1]);
        byte[] sigToVerify = new byte[sigFIS.available()];
        sigFIS.read(sigToVerify);
        sigFIS.close();

        /*
         * Create a Signature object and initialize it with the public
         * key read from the file.
         */
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        sig.initVerify(pubKey);

        /* 
         * Update and verify the data (while the buffer still has data) 
         * from the given file 
         */

        FileInputStream dataFIS = new FileInputStream(args[2]);
        BufferedInputStream bufin = new BufferedInputStream(dataFIS);

        byte[] buffer = new byte[1024];
        int len;
        while (bufin.available() != 0) {
          len = bufin.read(buffer);
          sig.update(buffer, 0, len);
        }

        bufin.close();

        boolean verifies = sig.verify(sigToVerify);
        if (verifies) {
          System.out.println(args[1] + " verifies " + args[2]);
        } else {
          System.out.println(args[1] + " fails to verify " + args[2]);
        }


      } catch (Exception e) {
        System.err.println("Caught exception " + e.toString());
      }
    ;

  }

}

