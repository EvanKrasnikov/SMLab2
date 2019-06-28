package ru.geographer29.crypt.keys;

public interface CryptographyAlgorithm {

    void generateKeyPair();
    String getPrivateKey();
    String getPublicKey();
    byte[] encrypt(byte[] message, String publicKey);
    byte[] decrypt(byte[] encMessage, String privateKey);

}
