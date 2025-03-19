package com.webconsent.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishRestApiDto {

    private UUID id;

    private String name;

    private String path;

    private String host;

    private UUID kongServiceId;

    private UUID productId;

    private UUID apiId;

    private String plugin;

    private String route;

    private String consentUrl;

    private String oauthProvisionKey;
}
