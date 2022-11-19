package com.tuthien.backend.controller;

import com.tuthien.backend.model.ProjectModel;
import com.tuthien.backend.model.ResponseModel;
import com.tuthien.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity findAll(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(this.projectService.findAll(limit));
    }

    @GetMapping("/pending-project")
    public ResponseEntity findPendingProject() {
        return ResponseEntity.ok(this.projectService.getPendingProject());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity findById(@PathVariable String projectId) {
        return ResponseEntity.ok(this.projectService.findById(projectId));
    }

    @GetMapping("/mine")
    public ResponseEntity findMyProject() {
        return ResponseEntity.ok(this.projectService.findMyProject());
    }

    @PostMapping
    public ResponseEntity saveProject(@RequestPart ProjectModel project, @RequestPart MultipartFile avatarFile) {
        project.setAvatarFile(avatarFile);
        ResponseModel<ProjectModel> postModelResponseModel = this.projectService.saveProject(project);
        return ResponseEntity.ok(postModelResponseModel);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity updatePost(
            @PathVariable String projectId,
            @RequestPart ProjectModel project,
            @RequestPart(required = false) MultipartFile avatarFile)
    {
        project.setId(projectId);
        project.setAvatarFile(avatarFile);
        ResponseModel<ProjectModel> postModelResponseModel = this.projectService.saveProject(project);
        return ResponseEntity.ok(postModelResponseModel);
    }
}