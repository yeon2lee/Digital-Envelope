package com.project.webcode.service;

import com.project.webcode.domain.Envelope;
import com.project.webcode.domain.EnvelopeReq;
import com.project.webcode.domain.EnvelopeRes;
import com.project.webcode.domain.Member;
import com.project.webcode.repository.EnvelopeRepository;
import com.project.webcode.repository.MemberRepository;
import com.project.webcode.repository.PublicKeysRepository;
import com.project.webcode.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

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

    public Envelope createEnvelope(EnvelopeReq envelopeReq) {
        // sender의 key load
        Member sender = memberRepository.findByName(envelopeReq.getSender());

        SymmetricKeyManage skm = new SymmetricKeyManage();
        Key secretKey = skm.load(sender.getSecretKeyPath());

        AsymmetricKeyManage akm = new AsymmetricKeyManage();
        Key publicKey = akm.load(sender.getPublicKeyPath());
        Key privateKey = akm.load(sender.getPrivateKeyPath());

        // receiver의 public key를 서버에서 load
        Member receiver = memberRepository.findByName(envelopeReq.getReceiver());
        byte[] bPublicKey = publicKeysRepository.findPublicKey(receiver.getName());
        PublicKey publicKeyB = (PublicKey) akm.bytesToKey(bPublicKey);

        DigitalSignatureManage dsm = new DigitalSignatureManage();
        String message = envelopeReq.getMessage();
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

        // 전자봉투 보내기 (서버에 저장)
        Envelope envelope = new Envelope(sender, receiver, encrypted1, encrypted2, encrypted3, encrypted4);
        Envelope saved = envelopeRepository.save(envelope);
        return saved;
    }


    // 전자봉투 열어보기
    public EnvelopeRes openEnvelope(String name) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 서버에서 receiver에게 온 전자봉투를 조회
        Member member = memberRepository.findByName(name);
        Envelope envelope = envelopeRepository.findByReceiver(member.getId());

        // receiver의 private key를 로컬에서 load
        AsymmetricKeyManage akm = new AsymmetricKeyManage();
        Key privateKey = akm.load(member.getPrivateKeyPath());

        // receiver의 private key로 secret key 복호화
        DigitalEnvelopeManage dem = new DigitalEnvelopeManage();
        byte[] bSecretKey = dem.decrypt(envelope.getSecretKey(), privateKey);
        // Bytes to Key
        SymmetricKeyManage skm = new SymmetricKeyManage();
        Key secretKey = skm.bytesToKey(bSecretKey);

        // 암호문 복호화
        byte[] data = dem.decrypt(envelope.getData(), secretKey);
        byte[] signature = dem.decrypt(envelope.getSignature(), secretKey);
        byte[] bPublicKey = dem.decrypt(envelope.getPublicKey(), secretKey);
        PublicKey publicKey = (PublicKey) akm.bytesToKey(bPublicKey);

        // 검증
        DigitalSignatureManage dsm = new DigitalSignatureManage();
        boolean verify = dsm.verify(data, signature, publicKey);

        EnvelopeRes envelopeRes = new EnvelopeRes(envelope.getSender().getName(), envelope.getReceiver().getName(), new String(data), verify);

        return envelopeRes;
    }


}
