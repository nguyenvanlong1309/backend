package com.tuthien.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuthien.backend.dao.ProjectDAO;
import com.tuthien.backend.dao.RegionDAO;
import com.tuthien.backend.model.RegionModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionService {

    private final RegionDAO regionDAO;
    private final ObjectMapper objectMapper;
    private final ProjectDAO projectDAO;

    public List<RegionModel> findByYear(Integer year) {
        return this.regionDAO.findAll().stream()
                .map(region -> this.objectMapper.convertValue(region, RegionModel.class))
                .peek((region -> {
                    region.setProjects(this.projectDAO.findByRegionIdAndYear(region.getId(), year));
                }))
                .collect(Collectors.toList());
    }
}
