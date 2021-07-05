package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Controls;
import com.cma.cmaproject.model.Procedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProceduresRepository extends JpaRepository<Procedures, Long> {
    Optional<Procedures> findByIdAndIntrash(Long id, String intrashNO);

    List<Procedures> findByIntrash(String intrashNO);

    List<Procedures> findByControls(Controls controls);

    List<Procedures> findByControlsAndIntrash(Controls controls,String intrash);

    List<Procedures> findByControlsAndIntrashAndActionStatus(Controls controls,String intrash,String status);
}
