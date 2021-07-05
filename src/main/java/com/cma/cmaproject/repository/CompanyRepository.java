package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.CustomerOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    List<Company> findByIntrash(String intrashNO);
    Optional<Company> findByIntrashAndId(String intrash,Long id);
    List<Company> findByIntrashAndActionStatus(String intrash,String status);
    Optional<Company> findByCompanyNameAndIntrash(String name,String intrash);
}
