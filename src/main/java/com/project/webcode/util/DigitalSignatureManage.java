package com.project.webcode.util;

import java.security.*;

public class DigitalSignature {
    byte[] data;
    PublicKey publicKey;
    PrivateKey privateKey;

    public byte[] create(byte[] data, PrivateKey privateKey) {
        Signature sig;
        byte[] signature = null;
        try {
            sig = Signature.getInstance("SHA-1");
            sig.initSign((PrivateKey) privateKey);
            sig.update(data);
            signature = sig.sign();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return signature;
    }

    public boolean verify(byte[] data, byte[] signature, PublicKey publicKey) {
        Signature sig;
        boolean rslt = false;
        try {
            sig = Signature.getInstance("SHA256WithRSA");
            sig.initVerify(publicKey);
            sig.update(data);
            rslt = sig.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return rslt;
    }

}
