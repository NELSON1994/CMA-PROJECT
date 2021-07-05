package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long> {

    List<Industry> findByIntrash(String intrashNO);

    Optional<Industry> findByIdAndIntrash(Long industryId, String intrashNO);
}
