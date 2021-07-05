package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.ProcedureAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureAssessmentRepository extends JpaRepository<ProcedureAssessment,Long> {
        List<ProcedureAssessment> findByIntrashAndCompany(String intrash, Company c);

    Optional<ProcedureAssessment> findByIdAndIntrash(Long id, String intrashNO);
}
