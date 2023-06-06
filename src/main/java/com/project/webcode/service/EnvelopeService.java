package com.project.webcode.service;

import com.project.webcode.domain.Envelope;
import com.project.webcode.domain.EnvelopeDto;
import com.project.webcode.domain.Member;
import com.project.webcode.repository.EnvelopeRepository;
import com.project.webcode.repository.MemberRepository;
import com.project.webcode.repository.PublicKeysRepository;
import com.project.webcode.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EnvelopeService {

    private final MemberRepository memberRepository;
    private final EnvelopeRepository envelopeRepository;
    private final PublicKeysRepository publicKeysRepository;

    public List<Member> findAll() {
        return publicKeysRepository.findMemberFetchJoin();
    }

    public Envelope createEnvelope(EnvelopeDto envelopeDto) {
        // sender의 key load
        Member sender = memberRepository.findByName(envelopeDto.getSender());

        SymmetricKeyManage symmetricKeyManage = new SymmetricKeyManage();
        Key secretKey = symmetricKeyManage.load(sender.getSecretKeyPath());

        AsymmetricKeyManage asymmetricKeyManage = new AsymmetricKeyManage();
        Key publicKey = asymmetricKeyManage.load(sender.getPublicKeyPath());
        Key privateKey = asymmetricKeyManage.load(sender.getPrivateKeyPath());

        // receiver의 public key load
        byte[] publicKeyB = publicKeysRepository.findPublicKey(envelopeDto.getReceiver());

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
        Key key = symmetricKeyManage.bytesToKey(publicKeyB);
        byte[] encrypted4 = dem.encrypt(secretKey.getEncoded(), key);

        // 전자봉투 보내기
        Member receiver = memberRepository.findByName(envelopeDto.getReceiver());
        Envelope envelope = new Envelope(sender, receiver, encrypted1, encrypted2, encrypted3, encrypted4);
        Envelope saved = envelopeRepository.save(envelope);
        return saved;
    }


    // 전자봉투 열어보기
    public String openEnvelope(String receiver) {

        Member member = memberRepository.findByName(receiver); // TODO Optional로 바꾸기
        Envelope envelope = envelopeRepository.findByReceiver(member.getName());

        // private key load
        AsymmetricKeyManage asymmetricKeyManage = new AsymmetricKeyManage();
        Key privateKey = asymmetricKeyManage.load(member.getPrivateKeyPath());

        DigitalEnvelopeManage dem = new DigitalEnvelopeManage();
        byte[] secretKey = dem.decrypt(envelope.getSecretKey(), privateKey);

        SymmetricKeyManage symmetricKeyManage = new SymmetricKeyManage();
        Key key = symmetricKeyManage.bytesToKey(secretKey);

        // 암호문 복호화
        byte[] data = dem.decrypt(envelope.getData(), key);
        byte[] signature = dem.decrypt(envelope.getSignature(), key);
        byte[] tmpKey = dem.decrypt(envelope.getPublicKey(), key);

        Key publicKey = asymmetricKeyManage.bytesToKey(tmpKey);

        DigitalSignatureManage dsm = new DigitalSignatureManage();
        boolean verify = dsm.verify(data, signature, (PublicKey) publicKey);

        if (verify) {
            return new String(data);
        } else {
            return "검증에 실패했습니다.";
        }
    }


}
