package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.ProcedureExecutionAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureExecutionAttributesRepo extends JpaRepository<ProcedureExecutionAttributes,Long> {
    Optional<ProcedureExecutionAttributes> findByIdAndIntrash(Long id, String intrashNO);

    List<ProcedureExecutionAttributes> findByIntrash(String intrashNO);
}
