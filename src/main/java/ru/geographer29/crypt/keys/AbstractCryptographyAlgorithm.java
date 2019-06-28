package ru.geographer29.crypt.keys;

import java.math.BigInteger;

public abstract class AbstractCryptographyAlgorithm implements CryptographyAlgorithm {

    protected String privateKey;
    protected String publicKey;
    final BigInteger one = BigInteger.ONE;

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

}
