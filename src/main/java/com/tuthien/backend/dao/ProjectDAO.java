package com.tuthien.backend.dao;

import com.tuthien.backend.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectDAO extends JpaRepository<Project, String> {

    @Query("SELECT p FROM Project p WHERE p.status <> -1")
    List<Project> findApprovedProject(Pageable pageable);

    List<Project> findByCreatedBy(String username);
}
