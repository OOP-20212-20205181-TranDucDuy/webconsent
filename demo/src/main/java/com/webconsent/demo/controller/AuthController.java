package com.webconsent.demo.controller;

import com.webconsent.demo.dto.*;
import com.webconsent.demo.service.LdapService;
import com.webconsent.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
@Component
public class AuthController {
    private final LdapService ldapService;
    private final UserService userService;

    @PostMapping("/login")
    ResponseEntity<Oauth2Dto> login(
            @RequestBody LoginRequest request,
            @RequestParam("path") String path,
            @RequestParam("clientId") String clientId) {
        Oauth2Dto oauth2Dto = ldapService.authenticate(request, path , clientId);
        return ResponseEntity.ok(oauth2Dto);
    }
    @PostMapping("/register")
        ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }
    @GetMapping("/test")
    public List<OauthTokenLogDto> test() {
        return ldapService.getOauth2LogsByUserId("47a7bd3d-6015-4265-a39e-1b7bab541fb9");
    }
}
