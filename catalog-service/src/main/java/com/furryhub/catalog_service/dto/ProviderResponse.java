package com.furryhub.catalog_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data @AllArgsConstructor
public class ProviderResponse {
    private Integer id;
    private String name;
    private String phone;
    private Double latitude;
    private Double longitude;
    private Set<String> services;
    private String ownerEmail;
}
