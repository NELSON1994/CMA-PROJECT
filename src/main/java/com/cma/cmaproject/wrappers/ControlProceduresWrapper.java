package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.Controls;
import com.cma.cmaproject.model.Procedures;
import lombok.Data;

import java.util.List;
@Data
public class ControlProceduresWrapper {
    private Controls controls;
    private List<Procedures> proceduresList;
}
