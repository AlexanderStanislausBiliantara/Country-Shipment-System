package com.example.country_shipment_backend.Country;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data 
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIResponseDTO {
    private CountryDTO data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CountryDTO {
        private NameDTO names;
        private CodesDTO codes;
        private String region;
        private String subregion;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NameDTO {
        private String common;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CodesDTO {
        @JsonProperty("alpha_2")
        private String alpha2;
    }
}
