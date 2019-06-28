package ru.geographer29.crypt.keys;

public class KeyPair {

    private String privateKey;
    private String publicKey;

    public void generateWith (CryptographyAlgorithm algorithm) {

        algorithm.generateKeyPair();
        privateKey = algorithm.getPrivateKey();
        publicKey = algorithm.getPublicKey();

    }

    public void generateWith (ALGORITHM algName) {

        CryptographyAlgorithm algorithm = null;

        switch (algName) {
            case RSA :
                algorithm = new RSA();
                break;
            case GOST_3410_2012:
                algorithm = new GOST_3410_2012();
                break;
        }

        algorithm.generateKeyPair();
        privateKey = algorithm.getPrivateKey();
        publicKey = algorithm.getPublicKey();

    }

    public enum ALGORITHM {
        RSA,
        GOST_3410_2012
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

}
