package com.furryhub.catalog_service.controller;


import com.furryhub.catalog_service.dto.ServiceTypeResponse;
import com.furryhub.catalog_service.entity.ServiceType;
import com.furryhub.catalog_service.service.ServiceTypeService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-types")
@RequiredArgsConstructor
public class ServiceTypeController {
    private final ServiceTypeService service;

    record CreateReq(@NotBlank String name) {}

    @PostMapping
    public ResponseEntity<ServiceType> create(@RequestBody CreateReq req) {
        return ResponseEntity.ok(service.create(req.name()));
    }

    @GetMapping
    public ResponseEntity<List<ServiceTypeResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
