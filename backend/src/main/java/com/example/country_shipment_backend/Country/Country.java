package com.example.country_shipment_backend.Country;

import java.time.OffsetDateTime;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "country_cache")
@Data
@NoArgsConstructor 
@AllArgsConstructor  
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "country_code", nullable = false)
    private String countryCode;
    
    @Column(name = "common_name")
    private String commonName;

    private String region;
    private String subregion;

    @Column(name = "fetched_at", insertable = false, updatable = false)
    @Generated(event = EventType.INSERT)
    private OffsetDateTime fetchedAt;
}
