package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.ProcedureAssessment;
import com.cma.cmaproject.model.ProcedureExecution;
import com.cma.cmaproject.model.Procedures;
import org.apache.tomcat.jni.Proc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureExecutionRepository extends JpaRepository<ProcedureExecution,Long> {
    Optional<ProcedureExecution> findByIdAndIntrashAndActionStatusAndIsProcedurePrepared(Long id,String intrash,String status,String ispprepared);

    Optional<ProcedureExecution> findByIdAndIntrash(Long id, String intrashNO);

    List<ProcedureExecution> findByIntrash(String intrashNO);

    List<ProcedureExecution> findByIntrashAndProcedures(String intrash, Procedures pr);

    Optional<ProcedureExecution> findByProceduresAndIntrashAndActionStatus(Procedures p,String intrash,String status);

    Optional<ProcedureExecution> findByProceduresAndIntrashAndActionStatusAndCompany(Procedures p, String intrash, String status, Company c);

    List<ProcedureExecution> findByIntrashAndProcedureAssessment(String s, ProcedureAssessment procedureAssessment);



}
