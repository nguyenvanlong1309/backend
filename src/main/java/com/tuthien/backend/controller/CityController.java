package com.tuthien.backend.controller;

import com.tuthien.backend.dao.CityDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityDAO cityDAO;

    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.ok(this.cityDAO.findAll());
    }
}
