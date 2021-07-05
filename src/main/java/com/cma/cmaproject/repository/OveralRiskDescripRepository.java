package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.OveralRiskDescription;
import com.cma.cmaproject.model.Procedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OveralRiskDescripRepository extends JpaRepository<OveralRiskDescription,Long> {
    List<OveralRiskDescription> findByIntrash(String intrashNO);

    Optional<OveralRiskDescription> findByIdAndIntrash(Long lkId, String intrashNO);

    Optional<OveralRiskDescription> findByIdAndIntrashAndActionStatus(Long rId, String intrashNO, String actionApproved);

    List<OveralRiskDescription> findByIntrashAndActionStatus(String intrashNO, String actionApproved);
    List<OveralRiskDescription> findByProcedures(Procedures procedures);
    OveralRiskDescription findByProceduresAndIntrash(Procedures procedures,String c);
}
