package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.Controls;
import com.cma.cmaproject.model.Domain;
import lombok.Data;

import java.util.List;

@Data
public class DomainControlsWrapper {
    private Domain domain;
    private List<Controls> controlsList;
}
