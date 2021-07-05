package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.Domain;
import com.cma.cmaproject.model.ProcedureAssessment;
import lombok.Data;

@Data
public class AssessmentViewWrapper {
    private ProcedureAssessment procedureAssessment;
    private String domainScore;
    private Domain domain;
    private AssessmentWrapper22 assessmentWrapper22;
}
