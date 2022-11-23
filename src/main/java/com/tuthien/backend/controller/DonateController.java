package com.tuthien.backend.controller;

import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.model.DonateModel;
import com.tuthien.backend.model.ResponseModel;
import com.tuthien.backend.service.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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

    @GetMapping("/list-donate")
    public ResponseEntity getTopPersonalDonate(
            @RequestParam(required = false) String projectId
    ) {
        return ResponseEntity.ok(this.donateService.getTopDonate(projectId));
    }

    @GetMapping("/top-donate2")
    public ResponseEntity getTopPersonalDonate2(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String projectId
    ) {
        return ResponseEntity.ok(this.donateService.getTopDonate2(type, limit, projectId));
    }

    @PostMapping("/export")
    public ResponseEntity exportExcelFile(@RequestBody DonateModel donateModel) throws IOException {
        ByteArrayInputStream inputStream = this.donateService.exportExcelFile(donateModel);
        return ResponseEntity.ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(new InputStreamResource(inputStream));
    }

    @GetMapping("/donate-and-project")
    public ResponseEntity getTotalProjectAndDonate() {
        return ResponseEntity.ok(this.donateService.getTotalProjectAndDonate());
    }
}
