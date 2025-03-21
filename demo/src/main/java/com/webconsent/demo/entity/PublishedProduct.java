package com.webconsent.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Table(name = "published_product")
@NoArgsConstructor
@Builder
public class PublishedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
}