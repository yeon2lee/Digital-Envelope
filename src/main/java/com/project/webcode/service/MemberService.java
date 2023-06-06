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

import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService  {

    private final MemberRepository memberRepository;
    private final PublicKeysRepository publicKeysRepository;

    // 키 생성 및 저장
    public Member saveKeys(KeyGenerateReq keyGenerateReq) {
        // 키 생성 및 저장
        SymmetricKeyManage symmetricKeyManage = new SymmetricKeyManage();
        Key key = symmetricKeyManage.create();
        symmetricKeyManage.save(keyGenerateReq.getSecretKeyPath());

        AsymmetricKeyManage asymmetricKeyManage = new AsymmetricKeyManage();
        asymmetricKeyManage.create();
        PublicKey tmpKey = asymmetricKeyManage.savePublicKey(keyGenerateReq.getPublicKeyPath());
        byte[] publicKey = tmpKey.getEncoded();
        asymmetricKeyManage.savePrivateKey(keyGenerateReq.getPrivateKeyPath());

        // Member에 저장
        Member saved = memberRepository.save(keyGenerateReq.toEntity());

        // PublicKeys에 저장
        publicKeysRepository.save(new PublicKeys(publicKey, saved));

        return saved;
    }


}
