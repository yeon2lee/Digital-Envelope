package com.project.webcode.util;

import com.project.webcode.VerificationFailedException;

import java.security.*;

public class DigitalSignatureManage {
    String signAlgorithm = "SHA256WithRSA";

    public byte[] create(byte[] data, PrivateKey privateKey) {
        return create(data, privateKey, "SHA256WithRSA");
    }

    public byte[] create(byte[] data, PrivateKey privateKey, String algorithm) {
        signAlgorithm = algorithm;
        Signature sig;
        byte[] signature = null;
        try {
            sig = Signature.getInstance(algorithm);
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


    public boolean verify(byte[] data, byte[] signature, PublicKey publicKey) throws VerificationFailedException {
        Signature sig;
        boolean rslt = false;
        try {
            sig = Signature.getInstance(signAlgorithm);
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

        if (rslt == false) {
            throw new VerificationFailedException();
        }

        return rslt;
    }

}
