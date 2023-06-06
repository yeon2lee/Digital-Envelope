package com.project.webcode.domain;

import lombok.Getter;

@Getter
public class EnvelopeReq {

    private String sender;

    private String receiver;

    private String message;
}
