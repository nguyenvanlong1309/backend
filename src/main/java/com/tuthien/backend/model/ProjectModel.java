package com.tuthien.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuthien.backend.constant.ProjectStatus;
import com.tuthien.backend.entity.Project;
import com.tuthien.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ProjectModel implements Serializable {
    private String id;
    private String title;
    private String content;
    private String createdBy;
    private Date createdDate;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private String avatar;
    private Integer cityId;
    private String description;
    private BigDecimal total;
    private Integer type;
    private BigDecimal money;
    private Date modifiedDate;
    private String modifier;

    @JsonIgnore
    private MultipartFile avatarFile;
    private String createdByName;

    public ProjectModel(Project project, User user) {
        this(project);
        this.createdByName = user.getFullName();
    }

    public ProjectModel(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.content = project.getContent();
        this.createdBy = project.getCreatedBy();
        this.createdDate = project.getCreatedDate();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.status = project.getStatus();
        this.avatar = project.getAvatar();
        this.cityId = project.getCityId();
        this.description = project.getDescription();
        this.money = project.getMoney();
        this.type = project.getType();
        this.modifiedDate = project.getModifiedDate();
        this.modifier = project.getModifier();
    }

    public String getStatusName() {
        if (Objects.isNull(this.status)) return "";
        return ProjectStatus.fromValue(this.status).getDesc();
    }
}