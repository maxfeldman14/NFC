import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public class Sign {

  public static void main(String[] args) {

    /* Generate a DSA signature of input using an existing public/private key */

    if (args.length != 2) {
      System.out.println("Usage: Sign <name of file to sign> <private key>");
    } else
      try {

        /* Extract the private key from the file. */

        FileInputStream privKeyFIS = new FileInputStream(args[1]);
        byte[] privKey = new byte[privKeyFIS.available()];
        privKeyFIS.read(privKey);
        privKeyFIS.close();
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        
        /*
         * Create a Signature object and initialize it with the private
         * key specified, SHA1 with DSA for the algorithm, and Sun as the
         * provider.
         */

        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(privateKey);

        /* 
         * Update and sign the data (while the buffer still has data) 
         * from the given file 
         */

        FileInputStream fis = new FileInputStream(args[0]);
        BufferedInputStream bufin = new BufferedInputStream(fis);
        byte[] buffer = new byte[1024];
        int len;
        while (bufin.available() != 0) {
          len = bufin.read(buffer);
          dsa.update(buffer, 0, len);
        }
        

        bufin.close();

        /*
         * Now that all the data to be signed has been read in, generate
         * a signature for it
         */

        byte[] sig = dsa.sign();
        
        /* Save the signature in a file, for debugging
        FileOutputStream sigfos = new FileOutputStream(args[0]+".sig");                 
        sigfos.write(sig);                                                 
        
        sigfos.close();
        */

      } catch (Exception e) {
        System.err.println("Caught exception " + e.toString());
      }

  };

}
