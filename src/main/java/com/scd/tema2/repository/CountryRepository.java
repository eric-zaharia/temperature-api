package com.scd.tema2.repository;

import com.scd.tema2.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    boolean existsByName(String name);
    Country findByName(String nume);

    List<Country> findAllByName(String nume);
}
