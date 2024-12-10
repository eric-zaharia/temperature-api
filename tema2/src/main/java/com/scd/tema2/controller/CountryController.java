package com.scd.tema2.controller;

import com.scd.tema2.dto.CountryRequest;
import com.scd.tema2.dto.CountryResponse;
import com.scd.tema2.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    @Autowired
    private CountryService countryService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable Long id) {
        int deleted = countryService.deleteCountry(id);
        if (deleted == 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCountry(@PathVariable Long id, @RequestBody CountryRequest countryRequest) {
        if (countryRequest == null || countryRequest.getNume() == null ||
                countryRequest.getNume().isEmpty() || countryRequest.getLon() == null ||
                countryRequest.getLat() == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input data");
        }

        CountryResponse country;

        try {
            country = countryService.modify(id, countryRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        if (country == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Country not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(country);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CountryRequest countryRequest) {
        if (countryRequest == null || countryRequest.getNume() == null ||
                countryRequest.getNume().isEmpty() || countryRequest.getLon() == null ||
                countryRequest.getLat() == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input data");
        }

        Long result = countryService.save(countryRequest);

        if (result == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Country already exists!");
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(result));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(countryService.findAll());
    }
}
