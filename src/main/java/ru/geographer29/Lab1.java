package ru.geographer29;

import ru.geographer29.crypt.digest.Digest;
import ru.geographer29.crypt.digest.SHA1;
import ru.geographer29.crypt.keys.CryptographyAlgorithm;
import ru.geographer29.crypt.keys.KeyPair;
import ru.geographer29.crypt.keys.RSA;
import ru.geographer29.file.Util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Lab1 {
    final static File original = new File("Lab1.original");
    final static File encrypted = new File("Lab1.encrypted");
    final static File decrypted = new File("Lab1.decrypted");

    public static void main(String[] args) throws Exception {
        Lab1 lab = new Lab1();
        lab.core();
    }

    private void core() throws Exception {
        KeyPair rsaKeyPair = new KeyPair();
        rsaKeyPair.generateWith(KeyPair.ALGORITHM.RSA);
        String rsaPrivateKey = rsaKeyPair.getPrivateKey();
        String rsaPublicKey = rsaKeyPair.getPublicKey();
        SecretKey tempDesKey = KeyGenerator.getInstance("DES").generateKey();
        String desSecretKey = Base64.getEncoder().encodeToString(tempDesKey.getEncoded());

        System.out.println("RSA private key = " + rsaPrivateKey.toUpperCase());
        System.out.println("RSA public key = " + rsaPublicKey.toUpperCase());
        System.out.println("DES secret key = " + desSecretKey.toUpperCase());
        System.out.println();

        CryptographyAlgorithm rsa = new RSA();
        byte[] encMsg = rsa.encrypt(desSecretKey.getBytes(), rsaPublicKey);
        System.out.println("Encrypted DES key = " + Util.bytesToHex(encMsg).toUpperCase());
        byte[] decMsg = rsa.decrypt(encMsg, rsaPrivateKey);
        System.out.println("Decrypted DES key = " + new String(decMsg).toUpperCase());
        System.out.println();

        encryptFile(tempDesKey);
        File in = new File("Lab1.encrypted");
        String encFileContent = Util.readFileAsHex(in);
        System.out.println("File .encrypted content = " + encFileContent);
        decryptFile(tempDesKey);
        System.out.println();

        String digestOriginal = SHA1.calculateDigest(original);
        String digestDecrypted = SHA1.calculateDigest(decrypted);
        digestOriginal = ru.geographer29.crypt.digest.RSA.calculateDigest(rsaPrivateKey, digestOriginal);
        digestDecrypted = ru.geographer29.crypt.digest.RSA.calculateDigest(rsaPrivateKey, digestDecrypted);
        System.out.println("Digest of original file = " + digestOriginal);
        System.out.println("Digest of decrypted file = " + digestDecrypted);
        System.out.println("Are the digests identical? " + Digest.verifyChecksum(digestOriginal, digestDecrypted));
    }

    private void encryptFile(SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();

            try (
                    FileInputStream fis = new FileInputStream(original);
                    FileOutputStream fos = new FileOutputStream(encrypted);
                    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            ) {
                byte[] bytes = new byte[1024];
                fis.read(bytes);

                System.out.println("File .original content = " + new String(bytes));

                fos.write(iv);
                cos.write(bytes);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void decryptFile(SecretKey secretKey) {
        try (FileInputStream fis = new FileInputStream(encrypted)){
            byte[] fileIv = new byte[8];
            fis.read(fileIv);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            try (
                    CipherInputStream cis = new CipherInputStream(fis, cipher);
                    InputStreamReader ir = new InputStreamReader(cis);
                    BufferedReader reader = new BufferedReader(ir);
                    FileOutputStream fos = new FileOutputStream(decrypted);
            ) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String content = sb.toString().trim();
                fos.write(content.getBytes());
                System.out.println("File .decrypted content = " + content);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
