package com.tuthien.backend.dao;

import com.tuthien.backend.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapDAO extends JpaRepository<Map, Integer> {
}