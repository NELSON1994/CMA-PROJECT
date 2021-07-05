package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.RiskMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskMatrixRepository extends JpaRepository<RiskMatrix,Long> {
    Optional<RiskMatrix> findByIdAndIntrash(Long lhId, String intrashNO);

    List<RiskMatrix> findByIntrash(String intrashNO);

//    Optional<RiskMatrix> findByIntrashAndRiskLevelUpperLimitStartsWith(String c,String d);
//
//    List<RiskMatrix> findByIntrashAndRiskLevelLowLimitGreaterThanEqual(String intrash,String lower);
    List<RiskMatrix> findByIntrashAndRiskLevelUpperLimitIsGreaterThanEqual(String in,int upper);

    Optional<RiskMatrix>findByIntrashAndRiskLevelUpperLimit(String in,int upper);
}
