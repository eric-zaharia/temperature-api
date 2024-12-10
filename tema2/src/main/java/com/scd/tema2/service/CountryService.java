package com.scd.tema2.service;

import com.scd.tema2.dto.CountryRequest;
import com.scd.tema2.dto.CountryResponse;
import com.scd.tema2.entity.Country;
import com.scd.tema2.repository.CountryRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public int deleteCountry(Long id) {
        if (countryRepository.existsById(id)) {
            countryRepository.deleteById(id);
            return 0;
        }

        return 1;
    }

    public CountryResponse modify(Long id, CountryRequest countryRequest) throws RuntimeException {
        return countryRepository.findById(id).map(country -> {
            country.setNume(countryRequest.getNume());
            country.setLat(countryRequest.getLat());
            country.setLon(countryRequest.getLon());

            List<Country> filter = countryRepository.findAllByNume(countryRequest.getNume()).stream()
                    .filter(c -> !Objects.equals(c.getId(), country.getId())).toList();

            if (!filter.isEmpty()) {
                throw new RuntimeException("country already exists");
            }

            Country newCountry = countryRepository.save(country);

            CountryResponse countryResponse = new CountryResponse();
            countryResponse.setId(newCountry.getId().toString());
            countryResponse.setNume(newCountry.getNume());
            countryResponse.setLat(newCountry.getLat());
            countryResponse.setLon(newCountry.getLon());
            return countryResponse;

        }).orElse(null);
    }

    public Long save(CountryRequest countryRequest) {
        Country country = new Country();
        country.setNume(countryRequest.getNume());
        country.setLat(countryRequest.getLat());
        country.setLon(countryRequest.getLon());

        if (countryRepository.existsByNume(country.getNume())) {
            return -1L;
        }
        try {
            countryRepository.save(country);
        } catch (ConstraintViolationException e) {
            return -1L;
        }

        return country.getId();
    }

    public List<CountryResponse> findAll() {
        return countryRepository.findAll().stream()
                .map(country -> {
                    CountryResponse countryResponse = new CountryResponse();
                    countryResponse.setId(country.getId().toString());
                    countryResponse.setNume(country.getNume());
                    countryResponse.setLat(country.getLat());
                    countryResponse.setLon(country.getLon());
                    return countryResponse;
                }).collect(Collectors.toList());
    }
}
