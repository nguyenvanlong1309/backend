package com.tuthien.backend.controller.admin;

import com.tuthien.backend.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping(params = {"year"})
    public ResponseEntity findByYear(@RequestParam Integer year) {
        return ResponseEntity.ok(this.regionService.findByYear(year));
    }
}
