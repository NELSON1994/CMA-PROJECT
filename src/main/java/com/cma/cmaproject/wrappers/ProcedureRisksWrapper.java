package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.OveralRiskDescription;
import com.cma.cmaproject.model.Procedures;
import lombok.Data;

import java.util.List;
@Data
public class ProcedureRisksWrapper {
    private Procedures procedures;
    private List<OveralRiskDescription> overalRiskDescriptions;
}
