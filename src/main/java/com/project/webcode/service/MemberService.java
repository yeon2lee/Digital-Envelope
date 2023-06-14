package com.project.webcode.service;

import com.project.webcode.domain.KeyGenerateReq;
import com.project.webcode.domain.Member;
import com.project.webcode.domain.PublicKeys;
import com.project.webcode.repository.MemberRepository;
import com.project.webcode.repository.PublicKeysRepository;
import com.project.webcode.util.AsymmetricKeyManage;
import com.project.webcode.util.SymmetricKeyManage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService  {

    private final MemberRepository memberRepository;
    private final PublicKeysRepository publicKeysRepository;

    // 키 생성 및 저장
    public Member saveKeys(KeyGenerateReq keyGenerateReq) {
        // secret key 생성 및 로컬에 저장
        SymmetricKeyManage skm = new SymmetricKeyManage();
        skm.create();
        skm.save(keyGenerateReq.getSecretKeyPath());

        // private key와 public key 생성 및 로컬에 저장
        AsymmetricKeyManage akm = new AsymmetricKeyManage();
        akm.create();
        akm.savePrivateKey(keyGenerateReq.getPrivateKeyPath());
        PublicKey publicKey = akm.savePublicKey(keyGenerateReq.getPublicKeyPath());

        // Member 테이블에 사용자가 로컬에 키를 저장한 경로를 등록 (서버에 저장)
        Member member = memberRepository.save(keyGenerateReq.toEntity());

        // PublicKeys 테이블에 저장 (서버에 저장)
        byte[] bPublicKey = publicKey.getEncoded();
        publicKeysRepository.save(new PublicKeys(bPublicKey, member));

        return member;
    }


}
