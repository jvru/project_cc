import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
public class Sha {
    public static String sha256(byte[] input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            byte[] result = mDigest.digest(input);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                 sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
         
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String sha1(byte[] input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                 sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

}
