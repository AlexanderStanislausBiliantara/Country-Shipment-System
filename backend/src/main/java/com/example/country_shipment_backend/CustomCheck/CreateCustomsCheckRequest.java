package com.example.country_shipment_backend.CustomCheck;

import lombok.Data;

@Data 
public class CreateCustomsCheckRequest {
    String origCode;
    String destCode;
}
