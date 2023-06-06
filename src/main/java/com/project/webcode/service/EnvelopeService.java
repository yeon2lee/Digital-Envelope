package com.project.webcode.service;

import com.project.webcode.domain.Envelope;
import com.project.webcode.domain.EnvelopeDto;
import com.project.webcode.domain.EnvelopeRes;
import com.project.webcode.domain.Member;
import com.project.webcode.repository.EnvelopeRepository;
import com.project.webcode.repository.MemberRepository;
import com.project.webcode.repository.PublicKeysRepository;
import com.project.webcode.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EnvelopeService {

    private final MemberRepository memberRepository;
    private final EnvelopeRepository envelopeRepository;
    private final PublicKeysRepository publicKeysRepository;

    public List<Member> findMembers() {
        return publicKeysRepository.findMemberFetchJoin();
    }

    public Envelope createEnvelope(EnvelopeDto envelopeDto) {
        // sender의 key load
        System.out.println("envelopeDto = " + envelopeDto.getSender());
        Member sender = memberRepository.findByName(envelopeDto.getSender());
        System.out.println("sender = " + sender.getSecretKeyPath());

        SymmetricKeyManage symmetricKeyManage = new SymmetricKeyManage();
        Key secretKey = symmetricKeyManage.load(sender.getSecretKeyPath());

        AsymmetricKeyManage asymmetricKeyManage = new AsymmetricKeyManage();
        Key publicKey = asymmetricKeyManage.load(sender.getPublicKeyPath());
        Key privateKey = asymmetricKeyManage.load(sender.getPrivateKeyPath());

        // receiver의 public key load
        Member receiver = memberRepository.findByName(envelopeDto.getReceiver());
        Key publicKeyB = asymmetricKeyManage.load(receiver.getPublicKeyPath());

        DigitalSignatureManage dsm = new DigitalSignatureManage();
        String message = envelopeDto.getMessage();
        byte[] data = message.getBytes();

        // 전자서명 생성
        byte[] signature = dsm.create(data, (PrivateKey) privateKey);

        // 암호문 생성 (secret key로 암호화)
        DigitalEnvelopeManage dem = new DigitalEnvelopeManage();

        byte[] encrypted1 = dem.encrypt(data, secretKey);
        byte[] encrypted2 = dem.encrypt(signature, secretKey);
        byte[] encrypted3 = dem.encrypt(publicKey.getEncoded(), secretKey);

        // secret key 암호화
        byte[] encrypted4 = dem.encrypt(secretKey.getEncoded(), publicKeyB);

        // 전자봉투 보내기
        Envelope envelope = new Envelope(sender, receiver, encrypted1, encrypted2, encrypted3, encrypted4);
        Envelope saved = envelopeRepository.save(envelope);
        return saved;
    }


    // 전자봉투 열어보기
    public EnvelopeRes openEnvelope(String name) throws NoSuchAlgorithmException, InvalidKeySpecException {

        Member member = memberRepository.findByName(name);
        System.out.println("member = " + member.getName());
        System.out.println("member.getSecretKeyPath() = " + member.getSecretKeyPath());
        Envelope envelope = envelopeRepository.findByReceiver(member.getId());
        System.out.println("envelope.getReceiver() = " + envelope.getReceiver().getName());
        for (byte b : envelope.getSecretKey()) {
            System.out.print(String.format("%02x", b) + " ");
        }

        // private key load
        AsymmetricKeyManage asymmetricKeyManage = new AsymmetricKeyManage();
        Key privateKey = asymmetricKeyManage.load(member.getPrivateKeyPath());

        DigitalEnvelopeManage dem = new DigitalEnvelopeManage();
        byte[] tmpSecretKey = dem.decrypt(envelope.getSecretKey(), privateKey);
        Key secretKey = new SecretKeySpec(tmpSecretKey, "AES");

        // 암호문 복호화
        byte[] data = dem.decrypt(envelope.getData(), secretKey);
        byte[] signature = dem.decrypt(envelope.getSignature(), secretKey);
        byte[] tmpKey = dem.decrypt(envelope.getPublicKey(), secretKey);

        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(tmpKey));

        DigitalSignatureManage dsm = new DigitalSignatureManage();
        boolean verify = dsm.verify(data, signature, publicKey);

//        if (verify) {
//        } else {
//        }

        EnvelopeRes envelopeRes = new EnvelopeRes(envelope.getSender().getName(), envelope.getReceiver().getName(), new String(data), verify);

        return envelopeRes;
    }


}
