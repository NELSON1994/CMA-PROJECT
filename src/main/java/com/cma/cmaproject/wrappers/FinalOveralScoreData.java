package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.MaturityLevel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FinalOveralScoreData {
        private int finalScore;
        private String maturityLevel;
        private String maturityLevelName;
        private String maturityLevelDescription;
        private int maturityLowerRange;
        private int maturityUpperRange;

        private List<DomainScoreGraphDataWrapper> domainScoreGraphDataWrapperList=new ArrayList<>();
}
