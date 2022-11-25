package com.tuthien.backend.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "REGION")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    @Column(name = "name")
    private String name;
}
