package com.example.country_shipment_backend.CustomCheck;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface CustomsCheckRepository extends JpaRepository<CustomsCheck, Integer> {}
