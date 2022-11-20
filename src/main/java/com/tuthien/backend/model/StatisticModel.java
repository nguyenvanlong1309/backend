package com.tuthien.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticModel {
    private Integer year;
    private String label;
    private BigDecimal value;

    public StatisticModel(Integer year, Integer label, BigDecimal value) {
        this(year, label.toString(), value);
    }
}
