package com.furryhub.booking_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "catalog-service")
public interface CatalogClient {

    @GetMapping("/providers/{id}")
    ProviderDto getProvider(@PathVariable("id") Integer id);

    record ProviderDto(
            Integer id, String name, String phone,
            Double latitude, Double longitude,
            java.util.Set<String> services,
            String ownerEmail
    ) {}
}
