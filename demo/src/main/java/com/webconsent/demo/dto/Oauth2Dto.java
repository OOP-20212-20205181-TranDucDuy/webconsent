package com.webconsent.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oauth2Dto {
    private String accessToken;
    private String clientId;
    private String clientSecret;
    private String authorizationUserid;
    private String provisionKey;
    private String path;
}
