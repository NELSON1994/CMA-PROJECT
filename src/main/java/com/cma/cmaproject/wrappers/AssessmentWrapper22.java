package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.Controls;
import lombok.Data;

import java.util.List;

@Data
public class AssessmentWrapper22 {
    private String controlScore;
    private Controls controls;
    List<AssessmentWrapper3> assessmentWrapper3;

}
