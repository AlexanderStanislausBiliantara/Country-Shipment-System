package com.example.country_shipment_backend.CustomCheck;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.country_shipment_backend.Country.Country;
import com.example.country_shipment_backend.Country.CountryService;

@Service 
public class CustomsCheckService {

    @Autowired
    private CustomsCheckRepository customsCheckRepository;

    @Autowired 
    private CountryService countryService;

    public CustomsCheck createCustomsCheck(String origCode, String destCode) {
        Country origin = this.countryService.getCountryByCode(origCode);
        Country destination = this.countryService.getCountryByCode(destCode);
        CustomsCheck check = new CustomsCheck();

        check.setId(0);
        check.setOrigCountry(origin.getCommonName());
        check.setDestCountry(destination.getCommonName());
        
        if (origin == null || destination == null) {
            check.setTier(Tier.UNKNOWN);
        } else {
            check.setOrigRegion(origin.getRegion());
            check.setDestRegion(destination.getRegion());
            check.setTier(determineTier(origin, destination));
            check.setCreatedAt(OffsetDateTime.now());
        }

        return this.customsCheckRepository.save(check);
    }

    private Tier determineTier(Country origin, Country destination) {
        if (origin.getSubRegion().equalsIgnoreCase(destination.getSubRegion())) {
            return Tier.REGIONAL;
        } else if (origin.getRegion().equalsIgnoreCase(destination.getRegion())) {
            return Tier.CONTINENTAL;
        } else {
            return Tier.INTERNATIONAL;
        }
    }
}
