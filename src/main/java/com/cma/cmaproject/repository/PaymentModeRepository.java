package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentMode,Long> {
    List<PaymentMode> findByIntrash(String intrashNO);

    Optional<PaymentMode> findByIdAndIntrash(Long industryId, String intrashNO);
}
