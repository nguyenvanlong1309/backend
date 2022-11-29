package com.tuthien.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuthien.backend.constant.ProjectStatus;
import com.tuthien.backend.dao.DonateDAO;
import com.tuthien.backend.dao.ProjectDAO;
import com.tuthien.backend.entity.Project;
import com.tuthien.backend.entity.User;
import com.tuthien.backend.model.ProjectModel;
import com.tuthien.backend.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectDAO projectDAO;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final IOService ioService;
    private final DonateDAO donateDAO;

    public List<ProjectModel> findAll(Integer limit) {
        Pageable pageable = null;
        if (Objects.nonNull(limit)) {
            pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdDate"));
        }
        return this.projectDAO.findApprovedProject(pageable)
            .stream()
            .peek(project -> {
                BigDecimal total = this.donateDAO.sumTotalByProjectId(project.getId());
                project.setTotal(total);
            }).collect(Collectors.toList());
    }

    public ProjectModel findById(String projectId) {
        Project project = this.projectDAO.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dự án"));
        return this.objectMapper.convertValue(project, ProjectModel.class);
    }

    public List<ProjectModel> findMyProject() {
        User sessionUser = this.userService.getSessionUser();
        return this.findProjectByUsername(sessionUser.getUsername());
    }

    public List<ProjectModel> findProjectByUsername(String username) {
        return this.projectDAO.findByCreatedBy(username).stream()
                .map(pj -> this.objectMapper.convertValue(pj, ProjectModel.class))
                .peek(pjm -> {
                    pjm.setTotal(this.donateDAO.sumTotalByProjectId(pjm.getId()));
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseModel<ProjectModel> saveProject(ProjectModel projectModel) {
        User user = this.userService.getSessionUser();
        if (Objects.nonNull(projectModel.getAvatarFile())) {
            String imageName = this.ioService.saveImageToStore(projectModel.getAvatarFile())
                    .orElseThrow(() -> new IllegalArgumentException("Có lỗi xảy ra trong quá trình lưu ảnh"));
            projectModel.setAvatar(imageName);
        }
        projectModel.setStatus(ProjectStatus.PENDING.getStatus());
        projectModel.setCreatedDate(new Date());
        if (Objects.nonNull(projectModel.getId())) {
            Project project = this.projectDAO.findById(projectModel.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài viết"));
            if (Objects.nonNull(project.getEndDate()) && new Date().after(project.getEndDate())) {
                throw new IllegalArgumentException("Dự án đã hết hạn");
            }

            if (!"ADMIN".equalsIgnoreCase(user.getRole()) && !project.getCreatedBy().equals(user.getUsername())) {
                throw new IllegalArgumentException("Bạn không có quyền chỉnh sửa dự án");
            }

            projectModel.setCreatedDate(project.getCreatedDate());
            projectModel.setStatus(project.getStatus());
            projectModel.setCreatedBy(project.getCreatedBy());
            if (Objects.isNull(projectModel.getAvatar())) {
                projectModel.setAvatar(project.getAvatar());
            }
        } else {
            projectModel.setCreatedBy(user.getUsername());
        }


        Project project = this.objectMapper.convertValue(projectModel, Project.class);
        if (Objects.isNull(project.getId())) {
            project.setId(project.getCityId() +"_"+ UUID.randomUUID());
        }
        this.projectDAO.save(project);
        return new ResponseModel<>(HttpStatus.OK, null, "Thanh cong");
    }

    public List<ProjectModel> getPendingProject() {
        return this.projectDAO.findPendingProject();
    }

    public ResponseModel approveProject(String projectId) {
        Project project = this.projectDAO.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy project"));
        if (project.getStatus() != ProjectStatus.PENDING.getStatus()) {
            throw new IllegalArgumentException("Không thể duyệt project này.");
        }

        if (Objects.nonNull(project.getEndDate()) && new Date().after(project.getEndDate())) {
            throw new IllegalArgumentException("Dự án đã kết thúc");
        }

        project.setStatus(ProjectStatus.DOING.getStatus());
        this.projectDAO.save(project);
        return new ResponseModel(HttpStatus.OK, project, "Thành công");
    }
}
