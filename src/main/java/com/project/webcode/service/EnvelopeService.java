package com.project.webcode.service;

import com.project.webcode.domain.EnvelopeReq;
import com.project.webcode.domain.Member;
import com.project.webcode.repository.EnvelopeRepository;
import com.project.webcode.repository.MemberRepository;
import com.project.webcode.repository.PublicKeysRepository;
import com.project.webcode.util.AsymmetricKeyManage;
import com.project.webcode.util.SymmetricKeyManage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EnvelopeService {

    private final PublicKeysRepository publicKeysRepository;

    public List<Member> findAll() {
        return publicKeysRepository.findMemberFetchJoin();
    }

}
