package com.tuthien.backend.dao;

import com.tuthien.backend.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionDAO extends JpaRepository<Region, Long> {
}