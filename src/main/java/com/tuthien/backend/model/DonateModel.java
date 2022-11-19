package com.tuthien.backend.model;

import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonateModel implements Serializable {
    private Long id;
    private String createdBy;
    private Date createdDate;
    private String projectId;
    private BigDecimal money;
    private String fullName;
    private String publicName;
    private Integer mode;
    private String email;
    private String phone;
    private Integer methodDonate;
    private String comment;
    private String title;
    private String image;
    private Integer type;

    public DonateModel(Donate donate, Project project) {
        this.id = donate.getId();
        this.createdBy = donate.getCreatedBy();
        this.createdDate = donate.getCreatedDate();
        this.projectId = donate.getProjectId();
        this.mode = donate.getMode();
        this.money = donate.getMoney();
        this.fullName = donate.getFullName();
        this.publicName = donate.getPublicName();
        this.phone = donate.getPhone();
        this.email = donate.getEmail();
        this.methodDonate = donate.getMethodDonate();
        this.comment = donate.getComment();
        this.image = donate.getImage();
        this.type = donate.getType();
        this.title = project.getTitle();
    }
}