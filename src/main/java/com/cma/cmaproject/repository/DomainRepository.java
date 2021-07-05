package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomainRepository  extends JpaRepository<Domain, Long> {
    List<Domain> findByIntrash(String intrashNO);

    Optional<Domain> findByIdAndIntrash(Long id, String intrashNO);

    List<Domain> findByIntrashAndActionStatus(String intrashNO, String actionApproved);
}
