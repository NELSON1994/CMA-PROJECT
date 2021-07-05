package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.CustomerOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrdersRepository extends JpaRepository<CustomerOrders, Long> {
    List<CustomerOrders> findByIntrash(String intrash);
    Optional<CustomerOrders> findByIdAndIntrash(Long aLong,String intrash);
    List<CustomerOrders> findByEmailAndIntrash(String email,String intrash);
    List<CustomerOrders> findByIdAndActionStatusAndIntrash(Long id, String actionStatus, String intrash);
}
