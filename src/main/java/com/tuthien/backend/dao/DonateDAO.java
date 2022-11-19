package com.tuthien.backend.dao;

import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.model.DonateModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DonateDAO extends JpaRepository<Donate, Long> {

    @Query("SELECT new com.tuthien.backend.model.DonateModel(d, p) " +
            " FROM Donate d" +
            " JOIN Project p ON d.projectId = p.id" +
            " ORDER BY d.createdDate DESC")
    List<DonateModel> findAllDonate();

    @Query("SELECT new com.tuthien.backend.model.DonateModel(d, p) FROM Donate d" +
            " JOIN Project p ON p.id = d.projectId" +
            " WHERE d.createdBy = ?1" +
            " ORDER BY d.createdDate DESC")
    List<DonateModel> finDonateByUsername(String username);

    @Query(nativeQuery = true,
            value = "SELECT public_name publicName, sum(money) total, count(1) count" +
            " FROM DONATE" +
            " WHERE type = ?1 AND ((?1 = 0 AND mode = 0) OR ?1 = 1)" +
            " GROUP BY publicName, phone, email" +
            " ORDER BY total DESC")
    List<Map<String, Object>> findTopPersonalDonate(Integer type, Pageable pageable);

    @Query("SELECT SUM(d.money) FROM Donate d WHERE d.projectId = ?1")
    BigDecimal sumTotalByProjectId(String projectId);
}