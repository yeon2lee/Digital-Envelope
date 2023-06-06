package com.project.webcode.util;

import java.io.*;
import java.security.*;

public class AsymmetricKeyManage {
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

    public PublicKey savePublicKey(String publicFileName) {
        if (publicKey == null) {
            return null;
        }

        try (FileOutputStream fstream = new FileOutputStream(publicFileName)) {
            try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
                ostream.writeObject(publicKey);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public PrivateKey savePrivateKey(String privateFileName) {
        if (privateKey == null) {
            return null;
        }
        try (FileOutputStream fstream = new FileOutputStream(privateFileName)) {
            try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
                ostream.writeObject(privateKey);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

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
}
