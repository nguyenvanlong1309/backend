package com.tuthien.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuthien.backend.constant.ProjectStatus;
import com.tuthien.backend.entity.Project;
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

    @JsonIgnore
    private MultipartFile avatarFile;

    public String getStatusName() {
        if (Objects.isNull(this.status)) return "";
        return ProjectStatus.fromValue(this.status).getDesc();
    }
}