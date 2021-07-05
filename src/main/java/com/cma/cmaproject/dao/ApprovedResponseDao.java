package com.cma.cmaproject.dao;

import lombok.Data;

import java.util.List;

@Data
public class ApprovedResponseDao {
    private List<Long> approvedIds;
    private List<Long> failedToBeApprovedIds;
}
