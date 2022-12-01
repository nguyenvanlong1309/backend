package com.tuthien.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "PROJECT")
public class Project {

    @Id
    @Column(name = "project_id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "content")
    private String content;

    @Column(name = "username")
    private String createdBy;

    @Column(name = "city_id")
    private Integer cityId;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "type")
    private Integer type;

    @Column(name = "money")
    private BigDecimal money;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "modifier")
    private String modifier;
}
