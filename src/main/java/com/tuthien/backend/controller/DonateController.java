package com.tuthien.backend.controller;

import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.model.DonateModel;
import com.tuthien.backend.model.ResponseModel;
import com.tuthien.backend.service.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donates")
public class DonateController {

    private final DonateService donateService;

    @PostMapping("/personal")
    public ResponseEntity donatePersonal(@RequestBody DonateModel donateModel) {
        ResponseModel<Donate> donateResponseModel = this.donateService.donatePersonal(donateModel);
        return ResponseEntity.ok(donateResponseModel);
    }

    @GetMapping("/mine")
    public ResponseEntity getMyDonate() {
        return ResponseEntity.ok(this.donateService.getMyDonate());
    }
}
