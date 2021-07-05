package com.cma.cmaproject.wrappers;

import lombok.Data;

@Data
public class ProcedureExecutionAttributesCheckResponseWrapper {
    private String score;
    private String riskRating;
    private String riskMatrixName;
    private String isProcedurePrepared;
    private String isIssueNoted;
    private String isTheScoreSaved;
}
