package com.project.webcode.util;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class AsymmetricKeyManage implements KeyManage {
    KeyPair keypair;
    PublicKey publicKey;
    PrivateKey privateKey;

    public KeyPair create() {
        return create("RSA", 1028);
    }

    public KeyPair create(String algorithm, int bytes) {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(bytes);

        keypair = keyPairGen.generateKeyPair();
        publicKey = keypair.getPublic();
        privateKey = keypair.getPrivate();

        return keypair;
    }

    @Override
    public Key save(String fileName, Key key) {
        try (FileOutputStream fstream = new FileOutputStream(fileName)) {
            try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
                ostream.writeObject(key);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return key;
    }

    public PublicKey savePublicKey(String publicFileName) {
        if (publicKey == null) {
            return null;
        }
        save(publicFileName, publicKey);
        return publicKey;
    }

    public PrivateKey savePrivateKey(String privateFileName) {
        if (privateKey == null) {
            return null;
        }
        save(privateFileName, privateKey);
        return privateKey;
    }

    @Override
    public Key load(String fileName) {
        Key key = null;
        try (FileInputStream fis = new FileInputStream(fileName)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                Object obj = ois.readObject();
                key = (Key) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return key;
    }

    @Override
    public Key bytesToKey(byte[] byteKey) {
        Key key = null;
        try {
            key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(byteKey));
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return key;
    }

}
