package com.example.country_shipment_backend.CustomCheck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CustomsCheckController {
    
    @Autowired 
    private CustomsCheckService customsCheckService;

    @GetMapping("/api/custom-checks")
    public ResponseEntity<List<CustomsCheck>> listCustomChecks() {
        List<CustomsCheck> checks = this.customsCheckService.listAllCustomsChecks();
        return ResponseEntity.ok(checks);
    }

    @PostMapping("/api/custom-checks")
    public ResponseEntity<String> createCustomCheck(@RequestBody CreateCustomsCheckRequest request) {
        if (!isValidCountryCode(request.origCode) || !isValidCountryCode(request.destCode)) {
            return ResponseEntity.badRequest().body("Country codes must consist of 2 letters.");
        }

        CustomsCheck savedCustoms = this.customsCheckService.createCustomsCheck(request.origCode, request.destCode);

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created customs check with tier: " + savedCustoms.getTier());
    }

    private boolean isValidCountryCode(String code) {
        if (code == null || code.length() != 2) {
            return false;
        }

        for (int i = 0;i < code.length();++i) {
            if (!Character.isLetter(code.charAt(i))) {
                return false;
            }
        }

        return true;
    }

}
