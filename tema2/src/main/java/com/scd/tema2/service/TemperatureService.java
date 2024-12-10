package com.scd.tema2.service;

import com.scd.tema2.dto.TemperatureRequest;
import com.scd.tema2.dto.TemperatureResponse;
import com.scd.tema2.entity.City;
import com.scd.tema2.entity.Temperature;
import com.scd.tema2.repository.CityRepository;
import com.scd.tema2.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TemperatureService {
    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private CityRepository cityRepository;

    public int deleteTemperature(Long id) {
        if (temperatureRepository.existsById(id)) {
            temperatureRepository.deleteById(id);
            return 0;
        }

        return 1;
    }

    public TemperatureResponse modify(Long id, TemperatureRequest temperatureRequest) throws RuntimeException {
        return temperatureRepository.findById(id).map(temp -> {
            temp.setValue(temperatureRequest.getValoare());
            City city = cityRepository.findById(temperatureRequest.getIdOras()).orElse(null);

            if (city == null) {
                return null;
            }

            temp.setCity(city);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            temp.setTimestamp(timestamp);

            List<Temperature> filter = temperatureRepository.findAllByCityAndTimestamp(city, timestamp)
                    .stream().filter(c -> !Objects.equals(c.getId(), temp.getId())).toList();

            if (!filter.isEmpty()) {
                throw new RuntimeException("violated unique constraint");
            }

            Temperature newTemp = temperatureRepository.save(temp);

            return new TemperatureResponse(
                    newTemp.getId().toString(),
                    newTemp.getValue(),
                    newTemp.getTimestamp().toLocalDateTime().toLocalDate().toString()
            );

        }).orElse(null);
    }

    public Long saveTemperature(TemperatureRequest temperatureRequest) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Temperature temperature = new Temperature();
        temperature.setTimestamp(timestamp);
        City city = cityRepository.findById(temperatureRequest.getIdOras()).orElse(null);

        if (city == null) {
            return -1L;
        }

        temperature.setCity(city);
        temperature.setValue(temperatureRequest.getValoare());

        if (temperatureRepository.existsTemperatureByCityAndTimestamp(city, timestamp)) {
            return -2L;
        }

        return temperatureRepository.save(temperature).getId();
    }

    public List<TemperatureResponse> getTempsFilteredByPositionAndDate(Double lat, Double lon, LocalDate from, LocalDate until) {
        return temperatureRepository.findAll().stream()
                .filter(temp ->
                        (lat == null || Objects.equals(temp.getCity().getLatitude(), lat)) &&
                        (lon == null || Objects.equals(temp.getCity().getLongitude(), lon)) &&
                        (from == null || !temp.getTimestamp().toLocalDateTime().toLocalDate().isBefore(from)) && // Inclusive
                        (until == null || !temp.getTimestamp().toLocalDateTime().toLocalDate().isAfter(until))   // Inclusive
                )
                .map(temp -> new TemperatureResponse(
                        temp.getId().toString(),
                        temp.getValue(),
                        temp.getTimestamp().toLocalDateTime().toLocalDate().toString())
                )
                .toList();
    }

    public List<TemperatureResponse> getTempsFilteredByCityAndDate(Long cityId, LocalDate from, LocalDate until) {
        return temperatureRepository.findAll().stream()
                .filter(temp ->
                        (temp.getCity().getId().equals(cityId)) &&
                        (from == null || !temp.getTimestamp().toLocalDateTime().toLocalDate().isBefore(from)) &&
                        (until == null || !temp.getTimestamp().toLocalDateTime().toLocalDate().isAfter(until))
                )
                .map(temp -> new TemperatureResponse(
                        temp.getId().toString(),
                        temp.getValue(),
                        temp.getTimestamp().toLocalDateTime().toLocalDate().toString())
                )
                .toList();
    }

    public List<TemperatureResponse> getTempsFilteredByCountryAndDate(Long countryId, LocalDate from, LocalDate until) {
        return temperatureRepository.findAll().stream()
                .filter(temp ->
                        (temp.getCity().getCountry().getId().equals(countryId)) &&
                        (from == null || !temp.getTimestamp().toLocalDateTime().toLocalDate().isBefore(from)) &&
                        (until == null || !temp.getTimestamp().toLocalDateTime().toLocalDate().isAfter(until))
                )
                .map(temp -> new TemperatureResponse(
                        temp.getId().toString(),
                        temp.getValue(),
                        temp.getTimestamp().toLocalDateTime().toLocalDate().toString())
                )
                .toList();
    }
}
