package com.webconsent.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "site")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Site {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "LDAP_URL")
    private String ldap_url;

    @Column(name = "LDAP_PRINCIPAL")
    private String ldap_principal;

    @Column(name = "LDAP_CREDENTIAL")
    private String ldap_credential;
}
