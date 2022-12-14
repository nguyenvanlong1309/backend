package com.tuthien.backend.dao;

import com.tuthien.backend.entity.Project;
import com.tuthien.backend.model.ProjectModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ProjectDAO extends JpaRepository<Project, String> {

    List<Project> findByCreatedBy(String username);

    @Query("SELECT p FROM Project p WHERE p.status <> 0 AND p.endDate = ?1")
    List<Project> findByEndDate(Date endDate);

    @Query("SELECT new com.tuthien.backend.model.ProjectModel(p)" +
            " FROM Project p" +
            " WHERE p.cityId = ?1 AND p.status NOT IN (0,4,5)")
    List<ProjectModel> findByCityId(Integer cityId);

    @Query("SELECT new com.tuthien.backend.model.ProjectModel(p, u)" +
            " FROM Project p" +
            " JOIN User u ON u.username = p.createdBy" +
            " WHERE p.status NOT IN (0,4,5)")
    List<ProjectModel> findApprovedProject(Pageable pageable);

    @Query("SELECT new com.tuthien.backend.model.ProjectModel(p)" +
            " FROM Project p" +
            " JOIN City c ON c.id = p.cityId" +
            " WHERE c.regionId = ?1 AND YEAR(p.createdDate) = ?2 and p.status <> 4" )
    List<ProjectModel> findByRegionIdAndYear(Long regionId, Integer year);

    @Query("SELECT new com.tuthien.backend.model.ProjectModel(p, u)" +
            " FROM Project p" +
            " JOIN User u ON u.username = p.createdBy" +
            " WHERE p.status = 0 AND (p.endDate > current_date OR p.type = 1) ")
    List<ProjectModel> findPendingProject();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status <> 0 and p.status <> 4")
    Long countApprovedProject();

    @Query("SELECT p FROM Project p WHERE p.status NOT IN (0,4) GROUP BY p.cityId")
    List<Project> findByGroupByCityId();
}
