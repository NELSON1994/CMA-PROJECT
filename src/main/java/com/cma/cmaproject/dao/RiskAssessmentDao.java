package com.cma.cmaproject.dao;

import lombok.Data;

@Data
public class RiskAssessmentDao {
    private Long userId;
    private Long riskId;
    private Long likelyhoodId;
    private Long impactId;
    private String inputterComments;
}
