package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Controls;
import com.cma.cmaproject.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ControlRepository extends JpaRepository<Controls, Long> {
    Optional<Controls> findByIdAndIntrash(Long id, String intrashNO);

    List<Controls> findByIntrash(String intrashNO);

    List<Controls> findByIntrashAndActionStatus(String intrashNO,String status);

    List<Controls> findByDomain(Domain domain);

    List<Controls> findByDomainAndIntrash(Domain domain,String intrash);
}
