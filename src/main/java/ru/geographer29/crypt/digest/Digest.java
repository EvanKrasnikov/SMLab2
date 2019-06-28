package ru.geographer29.crypt.digest;

import java.io.File;

public class Digest {

    private Digest() {}

    public static String generateDigest(ALGORITHM algName, File file) {

        return "";
    }

    public enum ALGORITHM {
        SHA1,
        RSA
    }

    public static boolean verifyChecksum(String s1, String s2) {
        return s1.equals(s2);
    }

}
