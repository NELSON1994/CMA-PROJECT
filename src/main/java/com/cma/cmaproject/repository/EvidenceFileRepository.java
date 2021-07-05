package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.EvidenceFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvidenceFileRepository extends JpaRepository<EvidenceFileUpload,Long> {
}
