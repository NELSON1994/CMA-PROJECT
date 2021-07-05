package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.Impacts;
import com.cma.cmaproject.model.Likelyhood;
import com.cma.cmaproject.model.OveralRiskDescription;
import lombok.Data;

@Data
public class AssessmentWrapper {
private OveralRiskDescription overalRiskDescription;
private Likelyhood likelyhood;
private Impacts impacts;
private Company company;
}
