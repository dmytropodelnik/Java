package step.java.web1.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public static String hash(String str) {
        if (str == null) str = "";
        try {
            MessageDigest messageDigest =
                    MessageDigest.getInstance("SHA-1");
            // MD5 - 128b, SHA-1 - 160b, SHA-2 (SHA256) - 256b
            byte[] src = str.getBytes();
            byte[] res = messageDigest.digest(src);
            // return DatatypeConverter.printHexBinary( res ) ;
            char[] sym = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            for (byte b : res) {
                sb.append(sym[(b & 0xF0) >> 4]);
                sb.append(sym[b & 0xF]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
