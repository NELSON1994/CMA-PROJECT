package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepositort extends JpaRepository<Company,Long> {
    List<Company> findByIntrash(String intrashNO);
}
