package com.cma.cmaproject.wrappers;

import lombok.Data;

@Data
public class ScoringControlsWrapper {
    private String controlName;
    private String controlScore;
    private String  numberOfProcedures;
    private String totalPossibleScoreForAllProcedures;
    private String totalAttainedScoreForPreparedProcedures;
}
