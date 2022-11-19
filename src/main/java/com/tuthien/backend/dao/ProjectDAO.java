package com.tuthien.backend.dao;

import com.tuthien.backend.entity.Project;
import com.tuthien.backend.model.ProjectModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectDAO extends JpaRepository<Project, String> {

    @Query("SELECT new com.tuthien.backend.model.ProjectModel(p, u)" +
            " FROM Project p" +
            " JOIN User u ON u.username = p.createdBy" +
            " WHERE p.status <> 0")
    List<ProjectModel> findApprovedProject(Pageable pageable);

    List<Project> findByCreatedBy(String username);

    @Query("SELECT new com.tuthien.backend.model.ProjectModel(p, u)" +
            " FROM Project p" +
            " JOIN User u ON u.username = p.createdBy" +
            " WHERE p.status = 0 AND (p.endDate > current_date OR p.type = 1) ")
    List<ProjectModel> findPendingProject();
}
