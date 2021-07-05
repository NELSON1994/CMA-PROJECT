package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository  extends JpaRepository<Roles, Long> {
    List<Roles> findByIntrash(String intrash);
    Optional<Roles> findByIdAndIntrash(Long aLong, String intrash);
    Roles findByUser(User user);
    Optional<Roles> findByRoleNameAndIntrash(String name,String intrash);
}
