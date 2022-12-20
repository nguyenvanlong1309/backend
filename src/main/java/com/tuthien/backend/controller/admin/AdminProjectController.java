package com.tuthien.backend.controller.admin;

import com.tuthien.backend.model.ResponseModel;
import com.tuthien.backend.model.UserModel;
import com.tuthien.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class AdminProjectController {

    private final ProjectService projectService;

    @GetMapping("/approve/{projectId}")
    public ResponseEntity approveProject(@PathVariable String projectId) {
        ResponseModel responseModel = this.projectService.approveProject(projectId);
        return ResponseEntity.ok(responseModel);
    }

    @GetMapping("/lock/{projectId}")
    public ResponseEntity lockProject(@PathVariable String projectId) {
        ResponseModel responseModel = this.projectService.lockProject(projectId);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping("/username")
    public ResponseEntity findProjectByUsername(@RequestBody UserModel userModel) {
        return ResponseEntity.ok(this.projectService.findProjectByUsername(userModel.getUsername()));
    }
}
