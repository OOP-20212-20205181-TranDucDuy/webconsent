package com.webconsent.demo.repository;

import com.webconsent.demo.entity.PublishRestApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PublishRestApiRepository extends JpaRepository<PublishRestApi, UUID> {
    Optional<PublishRestApi> findByPath(String path);

    List<PublishRestApi> findByProductId(UUID productId);
}
