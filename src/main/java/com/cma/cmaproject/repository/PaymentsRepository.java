package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByIntrash(String intrashNO);

    Optional<Payment> findByIdAndIntrash(Long ordersId, String intrashNO);
}
