package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.Licence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LincenceRepository extends JpaRepository<Licence, Long> {
    Optional<Licence> findByIdAndIntrash(Long licenceId, String intrashNO);
    Optional<Licence> findLicenceByCompany(Company company);
    List<Licence> findByIntrash(String intrashNO);
    List<Licence> findByIntrashAndActionStatus(String intrash,String status);

    Optional<Licence> findLicenceByCompanyAndIntrash(Company company1, String intrashNO);
}
