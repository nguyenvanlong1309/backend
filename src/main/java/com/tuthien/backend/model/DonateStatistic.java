package com.tuthien.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonateStatistic {

    private Long regionId;
    private String regionName;
    private List<StatisticModel> data;
}
