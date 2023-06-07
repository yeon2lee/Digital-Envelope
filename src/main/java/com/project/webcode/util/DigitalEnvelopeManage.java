package com.project.webcode.util;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class DigitalEnvelopeManage {
    private Key secretKey;


    public byte[] encrypt(byte[] data, Key key) {
        byte[] encrypted = null;
        Cipher c1 = null;
        try {
            c1 = Cipher.getInstance(key.getAlgorithm());
            c1.init(Cipher.ENCRYPT_MODE, key);
            encrypted = c1.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
            e1.printStackTrace();
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return encrypted;
    }

    public byte[] decrypt(byte[] data, Key key) {
        byte[] decrypted = null;
        Cipher c1 = null;
        try {
            c1 = Cipher.getInstance(key.getAlgorithm());
            c1.init(Cipher.DECRYPT_MODE, key);
            decrypted = c1.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
            e1.printStackTrace();
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return decrypted;
    }
}
