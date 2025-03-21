package com.webconsent.demo.service;

import com.webconsent.demo.dto.RegisterRequest;
import com.webconsent.demo.entity.Consumer;
import com.webconsent.demo.entity.LdapConfig;
import com.webconsent.demo.entity.Site;
import com.webconsent.demo.entity.User;
import com.webconsent.demo.repository.ConsumerRepository;
import com.webconsent.demo.repository.LdapConfigRepository;
import com.webconsent.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService {
    private final LdapConfigRepository ldapConfigRepository;
    private final UserRepository userRepository;
    private final LdapService ldapService;
    private final ConsumerRepository consumerRepository;

    public void registerUser(RegisterRequest request){
        Consumer consumer = consumerRepository.findByOauthClientId(request.getConsumerId()).orElseThrow(
                () -> new RuntimeException("Consumer not found")
        );
        LdapConfig config = ldapConfigRepository.findByUrl(consumer.getSite().getLdap_url())
                .orElseGet(() -> {
                    Site site = consumer.getSite();
                    return ldapService.parseLdapConfigFromSite(site);
                });

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .ldapConfig(config)
                .consumer(consumer)
                .build();
        userRepository.save(user);
        ldapService.syncUserToLdap(user, config);
    }

}
