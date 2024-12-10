package com.scd.tema2.repository;

import com.scd.tema2.entity.City;
import com.scd.tema2.entity.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
    boolean existsTemperatureByCityAndTimestamp(City city, Timestamp timestamp);

    List<Temperature> findAllByCityAndTimestamp(City city, Timestamp timestamp);
}
