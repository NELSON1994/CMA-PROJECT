package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.OveralRiskDescription;
import com.cma.cmaproject.model.Procedures;
import com.cma.cmaproject.model.RiskProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskProfileRepository extends JpaRepository<RiskProfile, Long> {
    Optional<RiskProfile> findByIdAndIntrash(Long id,String intrash);

    List<RiskProfile> findByIntrash(String intrash);

    List<RiskProfile> findByIntrashAndActionStatus(String intrash, String actionStatus);

    List<RiskProfile> findByCompanyAndIntrash(Company company,String intrash);

    RiskProfile findByOveralRiskDescriptionAndIntrash(OveralRiskDescription overalRiskDescription,String c);

    RiskProfile findByProceduresAndIntrash(Procedures procedures, String intrash);
}
