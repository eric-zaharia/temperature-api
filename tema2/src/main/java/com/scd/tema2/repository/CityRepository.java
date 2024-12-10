package com.scd.tema2.repository;

import com.scd.tema2.entity.City;
import com.scd.tema2.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    boolean existsCityByCountryIdAndName(Long countryId, String name);
    List<City> findAllByCountry(Country country);

    List<City> findAllByCountryAndName(Country country, String name);
}
