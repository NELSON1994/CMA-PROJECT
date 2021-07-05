package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.MaturityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaturityLevelRepository extends JpaRepository<MaturityLevel,Long> {
    List<MaturityLevel> findByIntrash(String intrashNO);

    Optional<MaturityLevel> findByIdAndIntrash(Long lkId, String intrashNO);

    List<MaturityLevel> findByIntrashAndUpperRangeGreaterThanEqual(String intrash,int upper);
}
