package com.cma.cmaproject.wrappers;

import lombok.Data;

@Data
public class DomainScoreGraphDataWrapper {
    private String domainName;
    private int domainScoreInPercentage;
    private int domainBenchMarkScore;
    private int domainRatingScore;
    private int numberOfControls;
}
