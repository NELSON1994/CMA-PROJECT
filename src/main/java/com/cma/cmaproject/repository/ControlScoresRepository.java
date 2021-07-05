package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.ControlScores;
import com.cma.cmaproject.model.Controls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ControlScoresRepository extends JpaRepository<ControlScores,Long> {

    Optional<ControlScores> findByControls(Controls controls);

    Optional<ControlScores> findByControlsAndCompany(Controls controls, Company company);
}
