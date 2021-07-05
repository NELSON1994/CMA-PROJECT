package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Impacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImpactsRepository extends JpaRepository<Impacts,Long> {
    List<Impacts> findByIntrash(String intrashNO);

    Optional<Impacts> findByIdAndIntrash(Long impactsId, String intrashNO);

    List<Impacts> findByIntrashAndActionStatus(String intrashNO, String actionApproved);

    Optional<Impacts> findByIdAndIntrashAndActionStatus(Long rId, String intrashNO, String actionApproved);

}
