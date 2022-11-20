package com.tuthien.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuthien.backend.dao.CityDAO;
import com.tuthien.backend.dao.DonateDAO;
import com.tuthien.backend.dao.MapDAO;
import com.tuthien.backend.dao.ProjectDAO;
import com.tuthien.backend.entity.City;
import com.tuthien.backend.model.MapModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapDAO mapDAO;
    private final ObjectMapper objectMapper;
    private final ProjectDAO projectDAO;
    private final CityDAO cityDAO;
    private final DonateDAO donateDAO;

    public List<MapModel> findProjectInMap() {
        return this.mapDAO.findAll().stream()
                .map(map -> this.objectMapper.convertValue(map, MapModel.class))
                .peek(map -> {
                    map.setProjects(this.projectDAO.findByCityId(map.getCityId()).stream().peek(project -> {
                        project.setTotal(this.donateDAO.sumTotalByProjectId(project.getId()));
                    }).collect(Collectors.toList()));
                    map.setCityName(this.cityDAO.findById(map.getCityId()).map(City::getName).orElse(null));
                })
                .collect(Collectors.toList());
    }
}
