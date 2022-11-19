package com.tuthien.backend.controller;

import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.model.DonateModel;
import com.tuthien.backend.model.ResponseModel;
import com.tuthien.backend.service.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/business")
    public ResponseEntity donateBusiness(@RequestPart DonateModel donate, @RequestPart(required = false) MultipartFile avatar) {
        ResponseModel<Donate> donateResponseModel = this.donateService.donateBusiness(donate, avatar);
        return ResponseEntity.ok(donateResponseModel);
    }

    @GetMapping("/mine")
    public ResponseEntity getMyDonate() {
        return ResponseEntity.ok(this.donateService.getMyDonate());
    }

    @GetMapping("/top-donate")
    public ResponseEntity getTopPersonalDonate(
            @RequestParam Integer type,
            @RequestParam(defaultValue = "10") Integer limit)
    {
        return ResponseEntity.ok(this.donateService.getTopDonate(type, limit));
    }
}
