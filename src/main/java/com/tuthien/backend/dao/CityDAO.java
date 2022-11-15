package com.tuthien.backend.dao;

import com.tuthien.backend.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityDAO extends JpaRepository<City, Long> {
}
