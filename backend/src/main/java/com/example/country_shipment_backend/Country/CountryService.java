package com.example.country_shipment_backend.Country;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service 
public class CountryService {

    @Autowired 
    private CountryRepository countryRepository;

    @Autowired
    private RestClient restClient;

    public Country getCountryByCode(String code) {
        Optional<Country> cached = this.countryRepository.findByCountryCode(code);

        if (cached.isPresent()) {
            Country country = cached.get();
            return country;
        }

        String apiURI = "https://api.restcountries.com/countries/v5/codes.alpha_2/{countryCode}";
        
        try {
            APIResponseDTO response = restClient.get().uri(apiURI, code).retrieve().body(APIResponseDTO.class);

            if (response == null || response.getData() == null) {
                return null;
            }

            APIResponseDTO.CountryDTO fetchedCountry = response.getData();

            Country newCountry = new Country(
                    0,
                    fetchedCountry.getNames().getCommon(), 
                    fetchedCountry.getCodes().getAlpha2(), 
                    fetchedCountry.getRegion(), 
                    fetchedCountry.getSubregion(), 
                    OffsetDateTime.now());
            
            this.countryRepository.save(newCountry);
            return newCountry;
        } catch (Exception e) {
            return null;
        }
    }
}
