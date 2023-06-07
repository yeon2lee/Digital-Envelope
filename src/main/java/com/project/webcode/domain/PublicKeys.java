package com.project.webcode.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PublicKeys {

    @Id @GeneratedValue
    @Column(name = "public_key_id")
    private Long id;

    private byte[] publicKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public PublicKeys(byte[] publicKey, Member member) {
        this.publicKey = publicKey;
        this.member = member;
    }
}
