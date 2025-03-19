package com.webconsent.demo.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LdapRequest {
    private String url;
    private String baseDn;
    private String adminDn;
    private String adminPassword;
    private String userSearchFilter;
}
