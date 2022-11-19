package com.tuthien.backend.controller.admin;

import com.tuthien.backend.model.UserModel;
import com.tuthien.backend.service.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/donates")
@RequiredArgsConstructor
public class AdminDonateController {

    private final DonateService donateService;

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(this.donateService.findAll());
    }

    @PostMapping("/username")
    public ResponseEntity findDonateByUsername(@RequestBody UserModel userModel) {
        return ResponseEntity.ok(this.donateService.findDonateByUsername(userModel.getUsername()));
    }
}
