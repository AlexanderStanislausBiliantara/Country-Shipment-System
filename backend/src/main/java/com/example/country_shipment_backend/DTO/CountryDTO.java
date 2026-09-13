package com.example.country_shipment_backend.DTO;

import lombok.Data;

@Data 
public class CountryDTO {
    private NamesDTO names;
    private CodesDTO codes;
    private String region;
    private String subregion;
}
