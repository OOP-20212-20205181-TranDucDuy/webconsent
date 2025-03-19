package com.webconsent.demo.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webconsent.demo.dto.*;
import com.webconsent.demo.entity.Consumer;
import com.webconsent.demo.entity.LdapConfig;
import com.webconsent.demo.entity.PublishRestApi;
import com.webconsent.demo.entity.User;
import com.webconsent.demo.repository.ConsumerRepository;
import com.webconsent.demo.repository.PublishRestApiRepository;
import com.webconsent.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import com.webconsent.demo.repository.LdapConfigRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.naming.Name;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LdapService {
    private final LdapConfigRepository ldapConfigRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ConsumerRepository consumerRepository;
    private final PublishRestApiRepository publishRestApiRepository;
    @Transactional
    public void createLdapConfig(LdapRequest request) {
        LdapConfig ldapConfig = LdapConfig.builder()
                .url(request.getUrl())
                .baseDn(request.getBaseDn())
                .adminDn(request.getAdminDn())
                .adminPassword(request.getAdminPassword())
                .userSearchFilter(request.getUserSearchFilter())
                .build();

        ldapConfigRepository.save(ldapConfig);
    }

    private LdapTemplate createLdapTemplate(LdapConfig config) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(config.getUrl());
        contextSource.setBase(config.getBaseDn());
        contextSource.setUserDn(config.getAdminDn());
        contextSource.setPassword(config.getAdminPassword());

        try {
            contextSource.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException("LDAP Connection Failed", e);
        }

        return new LdapTemplate(contextSource);
    }


    public Oauth2Dto authenticate(LoginRequest request , String path , String clientId) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Consumer consumer = consumerRepository.findByOauthClientId(clientId).orElseThrow(
                () -> new RuntimeException("Consumer not found")
        );
        LdapConfig config = user.getLdapConfig();
        LdapTemplate ldapTemplate = createLdapTemplate(config);
        String searchFilter = config.getUserSearchFilter().replace("{0}", request.getUsername());
        if(ldapTemplate.authenticate("", searchFilter, request.getPassword())){
            String accessToken = jwtService.generate(String.valueOf(user.getId()), "ACCESS");
            PublishRestApiDto publishRestApi = getPublishRestApi(path);
            return Oauth2Dto.builder()
                    .accessToken(accessToken)
                    .clientId(clientId)
                    .clientSecret(consumer.getOauthClientSecret())
                    .provisionKey(publishRestApi.getOauthProvisionKey())
                    .authorizationUserid(consumer.getKongConsumerId())
                    .path(path)
                    .build();
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void syncUserToLdap(User user, LdapConfig ldapConfig) {
        LdapTemplate ldapTemplate = createLdapTemplate(ldapConfig);

        Name dn = LdapNameBuilder.newInstance("ou=users")
                .add("uid", user.getUsername())
                .build();

        DirContextAdapter context = new DirContextAdapter(dn);
        context.setAttributeValues("objectClass", new String[]{"inetOrgPerson", "top"});
        context.setAttributeValue("cn", user.getUsername());
        context.setAttributeValue("sn", user.getUsername());
        context.setAttributeValue("mail", user.getEmail());
        context.setAttributeValue("userPassword",user.getPassword());

        ldapTemplate.rebind(context);
    }

    public PublishRestApiDto getPublishRestApi(String path) {
        PublishRestApi publishRestApi = publishRestApiRepository.findByPath(path).orElseThrow(
                () -> new RuntimeException("API not found")
        );

        return PublishRestApiDto.builder()
                .id(publishRestApi.getId())
                .name(publishRestApi.getName())
                .path(publishRestApi.getPath())
                .host(publishRestApi.getHost())
                .kongServiceId(publishRestApi.getKongServiceId())
                .productId(publishRestApi.getProductId())
                .apiId(publishRestApi.getApiId())
                .plugin(publishRestApi.getPlugin())
                .route(publishRestApi.getRoute())
                .consentUrl(publishRestApi.getConsentUrl())
                .oauthProvisionKey(publishRestApi.getOauthProvisionKey())
                .build();
    }

    public OauthToken oauth2Autheticaion(Oauth2Dto request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Forwarded-Proto", "https");
        String body = "{\n" +
                "  \"client_id\": \"" + request.getClientId() + "\",\n" +
                "  \"response_type\": \"" + "code" + "\",\n" +
                "  \"scope\": \"" + "api" + "\",\n" +
                "  \"authenticated_userid\": \"" + request.getAuthorizationUserid() + "\",\n" +
                "  \"provision_key\": \"" + request.getProvisionKey() + "\"\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(request.getPath() + "/oauth2/authorize", HttpMethod.POST, entity, String.class);
            JsonObject convertedObject = new Gson().fromJson(response.getBody(), JsonObject.class);
            String callBackUri = convertedObject.get("redirect_uri").getAsString();
            OauthToken token = exchangeCodeToToken(callBackUri, request.getClientId(), request.getClientSecret(), request.getPath());
            return token;

        }
        catch (Exception e) {
            throw new RuntimeException("Error when sending request to " + request.getPath() + "/oauth2/authorize");
        }
    }
    public OauthToken exchangeCodeToToken(String callbackUri, String clientId , String clientSecret , String path) {
        String redirectUri = callbackUri.split("\\?code=")[0].replace("{\"redirect_uri\":\"", "").replace("\"}", "");
        String code = callbackUri.split("\\?code=")[1].replace("\"}", "");
        RestTemplate restTemplate = new RestTemplate();

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Forwarded-Proto", "https");

        // Body
       String body = "{\n" +
               "  \"grant_type\": \"" + "authorization_code" + "\",\n" +
               "  \"code\": \"" + code + "\",\n" +
               "  \"redirect_uri\": \"" + redirectUri + "\",\n" +
               "  \"client_id\": \"" + clientId + "\",\n" +
               "  \"client_secret\": \"" + clientSecret + "\"\n" +
               "}";

        // Tạo HttpEntity với body và headers
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(path + "/oauth2/token", HttpMethod.POST, entity, String.class);
            JsonObject convertedObject = new Gson().fromJson(response.getBody(), JsonObject.class);
            String accessToken = convertedObject.get("access_token").getAsString();
            String refreshToken = convertedObject.get("refresh_token").getAsString();
            String expiresIn = convertedObject.get("expires_in").getAsString();
            return OauthToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(Long.parseLong(expiresIn))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error when sending request to " + path + "/oauth2/token");
        }

    }
}
