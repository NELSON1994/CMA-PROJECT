package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermisionRepository extends JpaRepository<Permissions, Long> {
    List<Permissions> findByIntrash(String intrash);
    Optional<Permissions> findByIdAndIntrash(Long aLong, String intrash);
}
