package com.webconsent.demo.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "ldap_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LdapConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ldap_url", nullable = false)
    private String url;

    @Column(name = "base_dn", nullable = false)
    private String baseDn;

    @Column(name = "admin_dn", nullable = false)
    private String adminDn;

    @Column(name = "admin_password", nullable = false)
    private String adminPassword;

    @Column(name = "user_search_filter", nullable = false)
    private String userSearchFilter;


}
