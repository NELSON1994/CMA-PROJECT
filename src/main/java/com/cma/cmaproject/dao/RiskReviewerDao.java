package com.cma.cmaproject.dao;

import lombok.Data;

@Data
public class RiskReviewerDao {
    private Long userId;
    private Long riskDetailsId;
    private String reviewerComments;
    private String reviewStatus;
}
