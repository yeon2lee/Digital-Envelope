package com.project.webcode.util;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class SymmetricKeyManage implements KeyManage {
    private Key secretKey;

    public Key create() {
        return create("AES", 128);
    }

    public Key create(String algorithm, int bytes) {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(bytes);
        secretKey = keyGen.generateKey();
        return secretKey;
    }
    public Key save(String fileName) {
        return save(fileName, secretKey);
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

    @Override
    public Key load(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                Object obj = ois.readObject();
                secretKey = (Key) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return secretKey;
    }

    @Override
    public Key bytesToKey(byte[] byteKey) {
        return new SecretKeySpec(byteKey, "AES");
    }
}
