package com.webconsent.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LdapUser {

    private String cn;
    private String sn;
    private String password;
    private String username;


    public Attributes toAttributes() {
        Attributes attributes = new BasicAttributes();
        attributes.put("objectClass","inetOrgPerson");
        attributes.put("cn",cn);
        attributes.put("sn", sn);
        attributes.put("uid", username);
        attributes.put("userPassword", password);
        return attributes;
    }

}
