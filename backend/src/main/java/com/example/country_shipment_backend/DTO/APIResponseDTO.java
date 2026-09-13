package com.example.country_shipment_backend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data 
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIResponseDTO {
    private DataWrapperDTO data;   
}