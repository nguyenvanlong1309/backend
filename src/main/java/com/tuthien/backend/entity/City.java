package com.tuthien.backend.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CITY")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "region_id")
    private Long regionId;
}
