package com.webconsent.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "consumer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Consumer {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "kong_consumer_id")
    private String kongConsumerId;

    @Column(name = "oauth_client_id")
    private String oauthClientId;

    @Column(name = "oauth_client_secret")
    private String oauthClientSecret;

}
