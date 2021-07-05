package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.Domain;
import com.cma.cmaproject.model.DomainScores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomainScoresRepository extends JpaRepository<DomainScores,Long> {
    Optional<DomainScores> findByDomain(Domain domain);

    Optional<DomainScores> findByDomainAndCompany(Domain domain, Company company);

    List<DomainScores> findByCompany(Company company);
}
