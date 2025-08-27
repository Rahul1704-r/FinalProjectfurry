package com.furryhub.catalog_service.repository;


import com.furryhub.catalog_service.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {}
