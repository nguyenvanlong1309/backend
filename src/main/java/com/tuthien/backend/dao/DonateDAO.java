package com.tuthien.backend.dao;

import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.model.DonateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface DonateDAO extends JpaRepository<Donate, Long> {

    @Query("SELECT new com.tuthien.backend.model.DonateModel(d, p) FROM Donate d" +
            " JOIN Project p ON p.id = d.projectId" +
            " WHERE d.createdBy = ?1" +
            " ORDER BY d.createdDate DESC")
    List<DonateModel> findMyDonate(String username);

    @Query("SELECT SUM(d.money) FROM Donate d WHERE d.projectId = ?1")
    BigDecimal sumTotalByProjectId(String projectId);
}