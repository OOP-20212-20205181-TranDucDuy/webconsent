package com.webconsent.demo.repository;

import com.webconsent.demo.entity.Consumer;
import com.webconsent.demo.entity.PublishRestApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConsumerRepository extends JpaRepository<Consumer, UUID> {
    Optional<Consumer> findByOauthClientId(String oauthClientId);
}
