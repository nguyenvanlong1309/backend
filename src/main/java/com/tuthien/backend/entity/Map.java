package com.tuthien.backend.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MAP")
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "path")
    private String path;

    @Column(name = "city_id")
    private Integer cityId;
}
