package com.webconsent.demo.controller;

import com.webconsent.demo.dto.LdapRequest;
import com.webconsent.demo.service.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/ldap")
@RestController
public class LdapController {
    private final LdapService ldapService;
    @PostMapping
    ResponseEntity<String> createLdap(@RequestBody LdapRequest request) {
        ldapService.createLdapConfig(request);
        return ResponseEntity.ok("Ldap config created successfully");
    }
}
