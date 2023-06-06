package com.project.webcode.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Envelope {

    @Id @GeneratedValue
    @Column(name = "envelope_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    private byte[] data;
    private byte[] signature;
    private byte[] publicKey;
    private byte[] secretKey;

    public Envelope(Member sender, Member receiver, byte[] data, byte[] signature, byte[] publicKey, byte[] secretKey) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
        this.signature = signature;
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }
}
