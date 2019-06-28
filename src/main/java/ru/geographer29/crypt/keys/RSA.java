package ru.geographer29.crypt.keys;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA extends AbstractCryptographyAlgorithm {

    private final String DELIMETER = "#";

    @Override
    public void generateKeyPair() {
        int bitlen = 1024;
        SecureRandom rand = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitlen / 2, rand);
        BigInteger q = BigInteger.probablePrime(bitlen / 2, rand);
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));
        BigInteger e = BigInteger.probablePrime(bitlen / 2, rand);

        while (phi.gcd(e).compareTo(one) > 0 && e.compareTo(phi) < 0) {
            e = e.add(one);
        }

        BigInteger d = e.modInverse(phi);

        publicKey = n.toString(16) + DELIMETER + e.toString(16);
        privateKey = n.toString(16) + DELIMETER + d.toString(16);
    }

    public byte[] encrypt(byte[] message, String publicKey) {
        String[] arr = publicKey.split(DELIMETER);
        BigInteger e = new BigInteger(arr[1], 16);
        BigInteger n = new BigInteger(arr[0], 16);

        return (new BigInteger(message)).modPow(e, n).toByteArray();
    }

    public byte[] decrypt(byte[] encMessage, String privateKey) {
        String[] arr = privateKey.split(DELIMETER);
        BigInteger d = new BigInteger(arr[1], 16);
        BigInteger n = new BigInteger(arr[0], 16);

        return (new BigInteger(encMessage)).modPow(d, n).toByteArray();
    }

}
