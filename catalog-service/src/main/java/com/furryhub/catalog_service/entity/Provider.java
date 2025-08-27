package com.furryhub.catalog_service.entity;


import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "provider")
@Data @NoArgsConstructor @AllArgsConstructor
public class Provider {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String name;

    private String phone;

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point location;

    @ManyToMany
    @JoinTable(
            name = "provider_service_map",
            joinColumns = @JoinColumn(name = "provider_id"),
            inverseJoinColumns = @JoinColumn(name = "service_type_id")
    )

    private Set<ServiceType> serviceTypes = new HashSet<>();

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;
}
