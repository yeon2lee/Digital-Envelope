package com.project.webcode.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.security.PublicKey;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Member  {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String secretKeyPath;
    private String publicKeyPath;
    private String privateKeyPath;
}
