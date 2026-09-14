package com.example.country_shipment_backend.CustomCheck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class CustomsCheckController {
    
    @Autowired 
    private CustomsCheckService customsCheckService;

    @GetMapping("/api/custom-checks")
    public List<CustomsCheck> listCustomChecks() {
        return this.customsCheckService.listAllCustomsChecks();
    }


    @PostMapping("/api/custom-checks")
    public ResponseEntity<String> createCustomCheck(@RequestBody CreateCustomsCheckRequest request) {
        CustomsCheck savedCustoms = this.customsCheckService.createCustomsCheck(request.origCode, request.destCode);

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created customs check with tier: " + savedCustoms.getTier());
    }

}
