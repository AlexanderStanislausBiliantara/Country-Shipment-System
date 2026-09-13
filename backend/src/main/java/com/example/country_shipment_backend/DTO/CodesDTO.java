package com.example.country_shipment_backend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data 
public class CodesDTO {
    @JsonProperty("alpha_2")
    private String alpha2;
}
