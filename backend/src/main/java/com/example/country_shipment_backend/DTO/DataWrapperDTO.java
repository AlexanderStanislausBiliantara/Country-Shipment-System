package com.example.country_shipment_backend.DTO;

import java.util.List;

import lombok.Data;

@Data 
public class DataWrapperDTO {
    private List<CountryDTO> objects;
}
