package com.cma.cmaproject.dao;

import lombok.Data;

@Data
public class MaturityLevelDao {
    private String overalReadinessLevel;
    private String name;
    private int lowerRange;
    private int upperRange;
    private String description;

}
