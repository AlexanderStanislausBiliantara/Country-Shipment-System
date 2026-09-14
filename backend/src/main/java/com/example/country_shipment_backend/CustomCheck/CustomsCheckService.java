package com.example.country_shipment_backend.CustomCheck;

import java.time.OffsetDateTime;
import java.util.List;

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

        if (origin == null && destination == null) {
            check.setOriginCountry("-");
            check.setDestCountry("-");
            check.setOriginRegion("-");
            check.setDestRegion("-");
            check.setTier(Tier.UNKNOWN);
            check.setCreatedAt(OffsetDateTime.now());
        } else if (origin == null) {
            check.setOriginCountry("-");
            check.setDestCountry(destination.getCountryCode());
            check.setOriginRegion("-");
            check.setDestRegion(destination.getRegion());
            check.setTier(Tier.UNKNOWN);
            check.setCreatedAt(OffsetDateTime.now());
        } else if (destination == null) {
            check.setOriginCountry(origin.getCountryCode());
            check.setDestCountry("-");
            check.setOriginRegion(origin.getRegion());
            check.setDestRegion("-");
            check.setTier(Tier.UNKNOWN);
            check.setCreatedAt(OffsetDateTime.now());
        } else {
            check.setOriginCountry(origin.getCountryCode());
            check.setDestCountry(destination.getCountryCode());
            check.setOriginRegion(origin.getRegion());
            check.setDestRegion(destination.getRegion());
            check.setTier(determineTier(origin, destination));
            check.setCreatedAt(OffsetDateTime.now());
        }

        return this.customsCheckRepository.save(check);
    }

    public List<CustomsCheck> listAllCustomsChecks() {
        List<CustomsCheck> result = this.customsCheckRepository.findAll();
        return result;
    }

    private Tier determineTier(Country origin, Country destination) {
        if (origin.getSubregion().equalsIgnoreCase(destination.getSubregion())) {
            return Tier.REGIONAL;
        } else if (origin.getRegion().equalsIgnoreCase(destination.getRegion())) {
            return Tier.CONTINENTAL;
        } else {
            return Tier.INTERNATIONAL;
        }
    }
}
