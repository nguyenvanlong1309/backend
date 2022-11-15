package com.tuthien.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "DONATE")
public class Donate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "public_name")
    private String publicName;

    @Column(name = "mode")
    public Integer mode;

    @Column(name = "email")
    public String email;

    @Column(name = "phone")
    public String phone;

    @Column(name = "method_donate")
    public Integer methodDonate;

    @Column(name = "comment")
    private String comment;
}
