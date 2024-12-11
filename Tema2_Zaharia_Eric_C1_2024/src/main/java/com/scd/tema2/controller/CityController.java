package com.scd.tema2.controller;

import com.scd.tema2.dto.CityRequest;
import com.scd.tema2.dto.CityResponse;
import com.scd.tema2.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    @Autowired
    private CityService cityService;

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        int deleted = cityService.deleteCity(id);

        if (deleted == 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCity(@PathVariable Long id, @RequestBody CityRequest cityRequest) {
        if (cityRequest == null || cityRequest.getNume() == null ||
                cityRequest.getNume().isEmpty() || cityRequest.getLon() == null ||
                cityRequest.getLat() == null || cityRequest.getIdTara() == null ||
                cityRequest.getId() == null || (cityRequest.getId() != null &&
                !cityRequest.getId().equals(id)))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input data");
        }
        CityResponse cityResponse;
        try {
            cityResponse = cityService.modify(id, cityRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Country + city already exists!");
        }

        if (cityResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City/Country not found!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(cityResponse);
    }

    @GetMapping("/country/{id}")
    public ResponseEntity<?> findByCountryId(@PathVariable Long id) {
        List<CityResponse> response = cityService.findByCountryId(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CityRequest cityRequest) {
        if (cityRequest == null || cityRequest.getNume() == null ||
                cityRequest.getNume().isEmpty() || cityRequest.getLon() == null ||
                cityRequest.getLat() == null || cityRequest.getIdTara() == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input data");
        }

        Long result = cityService.save(cityRequest);

        if (result == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Country + city already exists!");
        } else if (result == -2) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Country does not exist!");
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(result));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(cityService.findAll());
    }
}
