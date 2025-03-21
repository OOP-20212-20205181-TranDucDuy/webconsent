package com.webconsent.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "published_rest_api")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishRestApi {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "host")
    private String host;

    @Column(name = "kong_service_id")
    private UUID kongServiceId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private PublishedProduct product;

    @Column(name = "product_id" , insertable = false, updatable = false)
    private UUID productId;

    @Column(name = "api_id")
    private UUID apiId;

    @Column(name = "plugin", columnDefinition = "TEXT")
    private String plugin;

    @Column(name = "route")
    private String route;

    @Column(name = "consent_url")
    private String consentUrl;

    @Column(name = "oauth_provision_key")
    private String oauthProvisionKey;
}
