package com.furryhub.catalog_service.service;


import com.furryhub.catalog_service.dto.ProviderRequest;
import com.furryhub.catalog_service.dto.ProviderResponse;
import com.furryhub.catalog_service.entity.Provider;
import com.furryhub.catalog_service.entity.ServiceType;
import com.furryhub.catalog_service.exception.NotFoundException;
import com.furryhub.catalog_service.repository.ProviderRepository;
import com.furryhub.catalog_service.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderSearchService {
    private final ProviderRepository providerRepo;
    private final ServiceTypeRepository serviceTypeRepo;
    private final GeometryFactory geometryFactory;

    @Transactional
    public ProviderResponse create(ProviderRequest req, String ownerEmail) {
        Point point = geometryFactory.createPoint(new Coordinate(req.getLongitude(), req.getLatitude()));
        point.setSRID(4326);

        Provider p = new Provider();
        p.setName(req.getName());
        p.setPhone(req.getPhone());
        p.setLocation(point);
        p.setOwnerEmail(ownerEmail);

        Set<ServiceType> types = serviceTypeRepo.findAllById(req.getServiceTypeIds())
                .stream().collect(Collectors.toSet());
        p.setServiceTypes(types);

        Provider saved = providerRepo.save(p);
        return toResponse(saved, req.getLatitude(), req.getLongitude());
    }

    @Transactional(readOnly = true)
    public List<ProviderResponse> nearby(double lat, double lon, double radiusMeters, Integer serviceTypeId) {
        List<Integer> ids = providerRepo.findNearbyIds(lat, lon, radiusMeters, serviceTypeId);
        return providerRepo.findAllById(ids).stream()
                .map(p -> {
                    var latLon = providerRepo.fetchLatLon(p.getId());
                    Double dbLat = null, dbLon = null;
                    if (!latLon.isEmpty()) {
                        Object[] row = latLon.get(0);
                        dbLat = row[0] != null ? ((Number) row[0]).doubleValue() : null;
                        dbLon = row[1] != null ? ((Number) row[1]).doubleValue() : null;
                    }
                    return toResponse(p, dbLat, dbLon);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProviderResponse> searchByName(String q) {
        return providerRepo.searchByName((q == null || q.isBlank()) ? null : q.trim())
                .stream()
                .map(p -> {
                    var latLon = providerRepo.fetchLatLon(p.getId());
                    Double dbLat = null, dbLon = null;
                    if (!latLon.isEmpty()) {
                        Object[] row = latLon.get(0);
                        dbLat = row[0] != null ? ((Number) row[0]).doubleValue() : null;
                        dbLon = row[1] != null ? ((Number) row[1]).doubleValue() : null;
                    }
                    return toResponse(p, dbLat, dbLon);
                })
                .toList();
    }

    private ProviderResponse toResponse(Provider p, Double lat, Double lon) {
        return new ProviderResponse(
                p.getId(),
                p.getName(),
                p.getPhone(),
                lat,
                lon,
                p.getServiceTypes().stream().map(ServiceType::getName).collect(Collectors.toSet()),
                p.getOwnerEmail()
        );
    }
    @Transactional(readOnly = true)
    public ProviderResponse get(Integer id) {
        Provider p = providerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider not found: " + id));

        var latLon = providerRepo.fetchLatLon(p.getId());
        Double dbLat = null, dbLon = null;
        if (!latLon.isEmpty()) {
            Object[] row = latLon.get(0);
            dbLat = row[0] != null ? ((Number) row[0]).doubleValue() : null;
            dbLon = row[1] != null ? ((Number) row[1]).doubleValue() : null;
        }
        return toResponse(p, dbLat, dbLon);
    }

    @Transactional
    public ProviderResponse update(Integer id, ProviderRequest req) {
        Provider p = providerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider not found: " + id));


        p.setName(req.getName());
        p.setPhone(req.getPhone());


        Point point = geometryFactory.createPoint(new Coordinate(req.getLongitude(), req.getLatitude()));
        point.setSRID(4326);
        p.setLocation(point);


        Set<ServiceType> types = serviceTypeRepo.findAllById(req.getServiceTypeIds())
                .stream().collect(Collectors.toSet());
        p.setServiceTypes(types);

        Provider saved = providerRepo.save(p);
        return toResponse(saved, req.getLatitude(), req.getLongitude());
    }

    @Transactional
    public void delete(Integer id) {
        if (!providerRepo.existsById(id)) {
            throw new NotFoundException("Provider not found: " + id);
        }
        providerRepo.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<ProviderResponse> listAll() {
        return providerRepo.findAll().stream()
                .map(p -> {
                    var latLon = providerRepo.fetchLatLon(p.getId());
                    Double dbLat = null, dbLon = null;
                    if (!latLon.isEmpty()) {
                        Object[] row = latLon.get(0);
                        dbLat = row[0] != null ? ((Number) row[0]).doubleValue() : null;
                        dbLon = row[1] != null ? ((Number) row[1]).doubleValue() : null;
                    }
                    return toResponse(p, dbLat, dbLon);
                })
                .toList();
    }
}
