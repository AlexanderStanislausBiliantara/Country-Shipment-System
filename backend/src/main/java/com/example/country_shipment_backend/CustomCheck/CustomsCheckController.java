package com.example.country_shipment_backend.CustomCheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class CustomsCheckController {
    
    @Autowired 
    private CustomsCheckService customsCheckService;

    // @GetMapping("/api/custom-checks")
    // public ResponseEntity<CustomCheck> listCustomChecks() {
        
    // }


    @PostMapping("/api/custom-checks")
    public ResponseEntity<String> createCustomCheck(@RequestBody String origCode, @RequestBody String destCode) {
        CustomsCheck savedCustoms = this.customsCheckService.createCustomsCheck(origCode, destCode);

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created customs check with tier: " + savedCustoms.getTier());
    }

}
