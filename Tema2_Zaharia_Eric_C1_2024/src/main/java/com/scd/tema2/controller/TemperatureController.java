package com.scd.tema2.controller;

import com.scd.tema2.dto.TemperatureRequest;
import com.scd.tema2.dto.TemperatureResponse;
import com.scd.tema2.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/temperatures")
public class TemperatureController {
    @Autowired
    private TemperatureService temperatureService;

    @DeleteMapping("/{id}")
    public ResponseEntity<TemperatureResponse> deleteTemperature(@PathVariable Long id) {
        int deleted = temperatureService.deleteTemperature(id);

        if (deleted == 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemperature(@PathVariable Long id, @RequestBody TemperatureRequest temperatureRequest) {
        if (temperatureRequest == null || temperatureRequest.getIdOras() == null ||
                temperatureRequest.getValoare() == null || temperatureRequest.getId() == null ||
                (temperatureRequest.getId() != null && !temperatureRequest.getId().equals(id))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("incomplete request");
        }

        TemperatureResponse temperatureResponse;

        try {
            temperatureResponse = temperatureService.modify(id, temperatureRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        if (temperatureResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Temp not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(temperatureResponse);
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<?> getTempsFilteredByCountryAndDate(
            @PathVariable Long id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(value = "until", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate until
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(temperatureService
                .getTempsFilteredByCountryAndDate(id, from, until));
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<?> getTempsFilteredByCityAndDate(
            @PathVariable Long id,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(value = "until", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate until
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(temperatureService
                .getTempsFilteredByCityAndDate(id, from, until));
    }

    @GetMapping
    public ResponseEntity<?> getTempsFilteredByPositionAndDate(
            @RequestParam(value = "lat", required = false) Double lat,
            @RequestParam(value = "lon", required = false) Double lon,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(value = "until", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate until
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(temperatureService
                .getTempsFilteredByPositionAndDate(lat, lon, from, until));
    }

    @PostMapping
    public ResponseEntity<?> postTemperature(@RequestBody TemperatureRequest temperatureRequest) {
        if (temperatureRequest == null || temperatureRequest.getIdOras() == null ||
            temperatureRequest.getValoare() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("incomplete request");
        }

        Long saved = temperatureService.saveTemperature(temperatureRequest);

        if (saved == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("city not found");
        } else if (saved == -2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("exists timestamp + id city");
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(saved));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }
}
