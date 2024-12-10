package com.scd.tema2.service;

import com.scd.tema2.dto.CityRequest;
import com.scd.tema2.dto.CityResponse;
import com.scd.tema2.entity.City;
import com.scd.tema2.entity.Country;
import com.scd.tema2.repository.CityRepository;
import com.scd.tema2.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CityService {
    @Autowired
    public CityRepository cityRepository;

    @Autowired
    public CountryRepository countryRepository;

    public int deleteCity(Long id) {
        if (cityRepository.existsById(id)) {
            cityRepository.deleteById(id);
            return 0;
        }

        return 1;
    }

    public CityResponse modify(Long id, CityRequest cityRequest) throws RuntimeException {
        return cityRepository.findById(id).map(city -> {
            city.setName(cityRequest.getNume());
            Country country = countryRepository.findById(cityRequest.getIdTara()).orElse(null);
            if (country == null) {
                return null;
            }

            city.setCountry(country);
            city.setLatitude(cityRequest.getLat());
            city.setLongitude(cityRequest.getLon());

            List<City> filter = cityRepository.findAllByCountryAndName(country, cityRequest.getNume())
                    .stream().filter(c -> !Objects.equals(c.getId(), city.getId())).toList();

            if (!filter.isEmpty()) {
                throw new RuntimeException("violated unique constraint");
            }

            City newCity = cityRepository.save(city);

            CityResponse cityResponse = new CityResponse();
            cityResponse.setId(newCity.getId().toString());
            cityResponse.setNume(newCity.getName());
            cityResponse.setLat(newCity.getLatitude());
            cityResponse.setLon(newCity.getLongitude());
            cityResponse.setIdTara(cityRequest.getIdTara().toString());

            return cityResponse;

        }).orElse(null);
    }

    public List<CityResponse> findByCountryId(Long countryId) {
        Country country = countryRepository.findById(countryId).orElse(null);
        if (country == null) {
            return null;
        }

        return cityRepository.findAllByCountry(country).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<CityResponse> findAll() {
        return cityRepository.findAll().stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    public Long save(CityRequest cityRequest) {
        City city = new City();
        city.setName(cityRequest.getNume());
        city.setLatitude(cityRequest.getLat());
        city.setLongitude(cityRequest.getLon());
        city.setCountry(countryRepository.findById(cityRequest.getIdTara()).orElse(null));

        if (cityRepository.existsCityByCountryIdAndName(cityRequest.getIdTara(), cityRequest.getNume())) {
            return -1L;
        } else if (city.getCountry() == null) {
            return -2L;
        }

        cityRepository.save(city);

        return city.getId();
    }

    private CityResponse mapToResponse(City city) {
        CityResponse cityResponse = new CityResponse();
        cityResponse.setId(city.getId().toString());
        cityResponse.setNume(city.getName());
        cityResponse.setLat(city.getLatitude());
        cityResponse.setLon(city.getLongitude());
        cityResponse.setIdTara(countryRepository.
                findByNume(city.getCountry().getNume()).getId().toString());

        return cityResponse;
    }
}
