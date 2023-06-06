package com.project.webcode.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class KeyGenerateReq {
    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @NotBlank
    private String secretKeyPath;

    @NotBlank
    private String publicKeyPath;

    @NotBlank
    private String privateKeyPath;

    @Builder
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .secretKeyPath(secretKeyPath)
                .publicKeyPath(publicKeyPath)
                .privateKeyPath(privateKeyPath)
                .build();
    }
}
