package com.furryhub.catalog_service.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "service_type")
@Data @NoArgsConstructor @AllArgsConstructor
public class ServiceType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
}
