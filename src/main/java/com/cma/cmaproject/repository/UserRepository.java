package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByIntrash(String intrashNO);

    Optional<User> findByIdAndIntrash(Long userId, String intrashNO);

    List<User> findByIntrashAndActionStatus(String intrashNO, String userActive);

    Optional<User> findByUsernameAndIntrashAndActionStatus(String toUpperCase, String intrashNO, String userActive);

    Optional<User> findByUsernameAndIntrash(String username, String intrashNO);

    List<User> findUsersByCompanyAndIntrash(Company company,String intrash);

    Optional<User> findByFirstNameAndOtherNameAndEmail(String fname, String othername, String email);
}
