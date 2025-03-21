package com.webconsent.demo.repository;

import com.webconsent.demo.entity.LdapConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LdapConfigRepository extends JpaRepository<LdapConfig, Long> {
    Optional<LdapConfig> findByUrl(String url);
}
