package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.AuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRailsRepository extends JpaRepository<AuditLogs, Long> {
}
