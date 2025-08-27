package com.furryhub.catalog_service.repository;



import com.furryhub.catalog_service.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    // Nearby using geometry (meters via geography cast)
    @Query(value = """
      SELECT p.id
      FROM provider p
      WHERE ST_DWithin(
        p.location::geography,
        ST_SetSRID(ST_MakePoint(?2, ?1), 4326)::geography,
        ?3
      )
      AND (
        ?4 IS NULL OR EXISTS (
          SELECT 1 FROM provider_service_map m
          WHERE m.provider_id = p.id AND m.service_type_id = ?4
        )
      )
      """, nativeQuery = true)
    List<Integer> findNearbyIds(double lat, double lon, double radiusMeters, Integer serviceTypeId);

    // Return stored lat/lon for UI mapping (optional helper)
    @Query(value = """
      SELECT
        ST_Y(p.location::geometry) as lat,
        ST_X(p.location::geometry) as lon
      FROM provider p
      WHERE p.id = ?1
      """, nativeQuery = true)
    List<Object[]> fetchLatLon(Integer id);

    @Query("""
        SELECT p FROM Provider p
        WHERE (:q IS NULL OR p.name ILIKE CONCAT('%', :q, '%'))
    """)
    List<Provider> searchByName(@Param("q") String q);

}
