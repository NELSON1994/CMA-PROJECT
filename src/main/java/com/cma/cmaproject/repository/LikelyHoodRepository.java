package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Likelyhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikelyHoodRepository extends JpaRepository<Likelyhood,Long> {
    List<Likelyhood> findByIntrash(String intrashNO);

    Optional<Likelyhood> findByIdAndIntrash(Long lkId, String intrashNO);

    List<Likelyhood> findByIntrashAndActionStatus(String intrashNO, String actionApproved);

    Optional<Likelyhood> findByIdAndIntrashAndActionStatus(Long rId, String intrashNO, String actionApproved);

}
