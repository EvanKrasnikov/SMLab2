package ru.geographer29.crypt.digest;

import ru.geographer29.file.Util;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1  {


    public static String calculateDigest(File file) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String content = Util.readFileAsHex(file);
        byte[] result = md.digest(content.getBytes());

        return Util.bytesToHex(result);
    }

}
