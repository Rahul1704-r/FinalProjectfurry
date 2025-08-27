package com.furryhub.catalog_service.controller;


import com.furryhub.catalog_service.dto.ProviderRequest;
import com.furryhub.catalog_service.dto.ProviderResponse;
import com.furryhub.catalog_service.service.ProviderSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
public class ProviderController {
    private final ProviderSearchService providerService;

    @PostMapping
    public ResponseEntity<ProviderResponse> create(
            @RequestHeader("X-User-Email") String ownerEmail,
            @Valid @RequestBody ProviderRequest req) {
        return ResponseEntity.ok(providerService.create(req, ownerEmail));
    }
    @GetMapping("/nearby")
    public ResponseEntity<List<ProviderResponse>> nearby(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "3000") double radiusMeters,
            @RequestParam(required = false) Integer serviceTypeId
    ) {
        return ResponseEntity.ok(providerService.nearby(lat, lon, radiusMeters, serviceTypeId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProviderResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(providerService.searchByName(q));
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponse>> list() {
        return ResponseEntity.ok(providerService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(providerService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> update(@PathVariable Integer id,
                                                   @Valid @RequestBody ProviderRequest req) {
        return ResponseEntity.ok(providerService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        providerService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
