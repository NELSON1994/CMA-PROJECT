package com.cma.cmaproject.wrappers;

import lombok.Data;

@Data
public class DomainScoringWrapper {
    private String domainName;
    private String domainScore;
    private String numberOfControls;
    private String benchMarkScore;
    private String ratingMark;
}
