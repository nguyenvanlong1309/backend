package com.tuthien.backend.controller;

import com.tuthien.backend.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maps")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(this.mapService.findProjectInMap());
    }
}
