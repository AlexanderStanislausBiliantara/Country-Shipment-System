package com.example.country_shipment_backend.Country;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.country_shipment_backend.DTO.APIResponseDTO;
import com.example.country_shipment_backend.DTO.CountryDTO;

@Service 
public class CountryService {

    @Value("${restcountries.api.key}")
    private String apiKey;

    @Autowired 
    private CountryRepository countryRepository;

    @Autowired
    private RestClient restClient;

    public Country getCountryByCode(String code) {
        Optional<Country> cached = this.countryRepository.findByCountryCode(code.toUpperCase());

        if (!cached.isEmpty()) {
            Country country = cached.get();
            return country;
        }

        String apiURI = "https://api.restcountries.com/countries/v5/codes.alpha_2/{countryCode}";
        
        try {
            APIResponseDTO response = restClient.get().uri(apiURI, code).header("Authorization", "Bearer " + apiKey).retrieve().body(APIResponseDTO.class);

            if (response == null || response.getData() == null) {
                return null;
            }

            CountryDTO fetchedCountry = response.getData().getObjects().get(0);

            System.out.println(fetchedCountry.getCodes().getAlpha2().length());

            Country newCountry = new Country(
                    0,
                    fetchedCountry.getCodes().getAlpha2(), 
                    fetchedCountry.getNames().getCommon(), 
                    fetchedCountry.getRegion(), 
                    fetchedCountry.getSubregion(), 
                    OffsetDateTime.now());
            
            this.countryRepository.save(newCountry);
            return newCountry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
