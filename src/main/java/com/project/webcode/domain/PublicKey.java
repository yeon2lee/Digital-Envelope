package com.project.webcode.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class PublicKey {

    @Id @GeneratedValue
    @Column(name = "key_id")
    private Long id;

}
