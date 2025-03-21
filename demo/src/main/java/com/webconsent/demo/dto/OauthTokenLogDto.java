package com.webconsent.demo.dto;

import lombok.Data;

@Data
public class OauthTokenLogDto {
    private String id;
    private String accessToken;
    private String refreshToken;
    private String scope;
    private Long createdAt;
    private String authenticatedUserid;
    private Long expiresIn;
    private Long ttl;
}
