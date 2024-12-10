package com.scd.tema2.repository;

import com.scd.tema2.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    boolean existsByNume(String name);
    Country findByNume(String nume);

    List<Country> findAllByNume(String nume);
}
