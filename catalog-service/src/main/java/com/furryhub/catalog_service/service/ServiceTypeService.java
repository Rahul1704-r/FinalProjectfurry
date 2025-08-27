package com.furryhub.catalog_service.service;



import com.furryhub.catalog_service.dto.ServiceTypeResponse;
import com.furryhub.catalog_service.entity.ServiceType;
import com.furryhub.catalog_service.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class ServiceTypeService {
    private final ServiceTypeRepository repo;

    public ServiceType create(String name) {
        ServiceType st = new ServiceType();
        st.setName(name.trim());
        return repo.save(st);
    }

    public List<ServiceTypeResponse> list() {
        return repo.findAll().stream()
                .map(s -> new ServiceTypeResponse(s.getId(), s.getName()))
                .toList();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}