package com.cma.cmaproject.dao;

import lombok.Data;

@Data
public class RiskMatrixDao {
    private String riskMatrixName;
    private int riskLevelLowLimit;
    private int riskLevelUpperLimit;
}
