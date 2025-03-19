package com.webconsent.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends User {
    private final String clientId;
    private final String responseType;
    private final String scope;
    private final String redirectUri;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             String clientId, String responseType, String scope, String redirectUri) {
        super(username, password, authorities);
        this.clientId = clientId;
        this.responseType = responseType;
        this.scope = scope;
        this.redirectUri = redirectUri;
    }

}
