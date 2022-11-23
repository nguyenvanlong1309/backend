package com.tuthien.backend.dao;

import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.model.DonateModel;
import com.tuthien.backend.model.StatisticModel;
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

    @Query("SELECT new com.tuthien.backend.model.DonateModel(d, p) FROM Donate d" +
            " JOIN Project p ON p.id = d.projectId" +
            " WHERE d.projectId = ?1" +
            " ORDER BY d.createdDate DESC")
    List<DonateModel> findDonateByProject(String projectId);

    @Query(nativeQuery = true,
            value = "SELECT IF(mode = 1, '*********', public_name) publicName, sum(money) total, count(1) count, DATE_FORMAT(created_date, '%d/%m/%Y') createdDate" +
            " FROM DONATE" +
            " WHERE (?1 IS NULL OR project_id = ?1)" +
            " GROUP BY publicName, phone, email, createdDate" +
            " ORDER BY total DESC")
    List<Map<String, Object>> findListDonate(String projectId);

    @Query(nativeQuery = true,
            value = "SELECT IF(mode = 1, '*********', public_name) publicName, sum(money) total, count(1) count" +
            " FROM DONATE" +
            " WHERE (?1 IS NULL OR type = ?1)" +
            " AND (?2 IS NULL OR project_id = ?2)" +
            " GROUP BY publicName, phone, email" +
            " ORDER BY total DESC")
    List<Map<String, Object>> findTopPersonalDonate2(Integer type, String projectId, Pageable pageable);

    @Query("SELECT new com.tuthien.backend.model.StatisticModel( YEAR(d.createdDate), MONTH(d.createdDate), SUM(d.money) )" +
            " FROM Donate d" +
            " JOIN Project p ON p.id = d.projectId" +
            " JOIN City c ON c.id = p.cityId" +
            " WHERE YEAR(d.createdDate) = ?1 AND c.regionId = ?2" +
            " GROUP BY MONTH(d.createdDate), YEAR(d.createdDate)" +
            " ORDER BY MONTH(d.createdDate) ASC")
    List<StatisticModel> statisticDonate(Integer year, Long regionId);

    @Query("SELECT SUM(d.money) FROM Donate d WHERE d.projectId = ?1")
    BigDecimal sumTotalByProjectId(String projectId);

    @Query("SELECT SUM(d.money) FROM Donate d")
    BigDecimal sumAllDonate();
}