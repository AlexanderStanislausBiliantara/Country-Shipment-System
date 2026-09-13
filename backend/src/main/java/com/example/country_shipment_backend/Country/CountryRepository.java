package com.example.country_shipment_backend.Country;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByCountryCode(String code);
}