package com.tuthien.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapModel implements Serializable {
    private Integer id;
    private String path;
    private Integer cityId;
    private String cityName;
    private List<ProjectModel> projects;
}