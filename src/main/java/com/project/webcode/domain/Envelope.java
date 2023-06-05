package com.project.webcode.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Envelope {

    @Id @GeneratedValue
    @Column(name = "envelope_id")
    private Long id;

}
