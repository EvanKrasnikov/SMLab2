package ru.geographer29.crypt.digest;

import java.io.File;
import java.math.BigInteger;

public class RSA {

    private static final String DELIMETER = "#";


    public static String calculateDigest(String privateKey, String data) {
        String[] arr = privateKey.split(DELIMETER);
        BigInteger d = new BigInteger(arr[1], 16);
        BigInteger n = new BigInteger(arr[0], 16);

        BigInteger m = new BigInteger(data, 16);
        BigInteger s = m.modPow(d, n);

        return s.toString(16);
    }

}
