package com.webconsent.demo.controller;

import com.webconsent.demo.dto.Oauth2Dto;
import com.webconsent.demo.dto.OauthToken;
import com.webconsent.demo.service.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/oauth2")
@RestController
@Component
public class Oauth2Controller {
    private final LdapService ldapService;
    @PostMapping("/authorize")
    public ResponseEntity<OauthToken> publish(@RequestBody Oauth2Dto request) {
        return ResponseEntity.ok(ldapService.oauth2Autheticaion(request));
    }
}
