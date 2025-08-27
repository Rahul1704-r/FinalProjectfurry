package com.furryhub.catalog_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class ProviderRequest {
    @NotBlank
    private String name;
    private String phone;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Set<Integer> serviceTypeIds;
}
