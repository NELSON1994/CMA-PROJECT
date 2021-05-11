package com.cma.cmaproject.repository;

import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.model.Licence;
import com.cma.cmaproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LincenceRepository extends JpaRepository<Licence, Long> {
    Optional<Licence> findByIdAndIntrash(Long licenceId, String intrashNO);

    Optional<Licence> findByIdAndIntrashAndActionStatus(Long licenceId, String intrashNO, String licenceStatus4);
    Optional<Licence> findByUser(User user);
    List<Licence> findByUserAndActionStatus(User user,String actionStatus);

    List<CustomerOrders> findByIntrash(String intrashNO);
}
