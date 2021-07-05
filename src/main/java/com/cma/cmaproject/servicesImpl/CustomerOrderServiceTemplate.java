package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.dao.OrdersDao;

public interface CustomerOrderServiceTemplate {

  //  GenericResponseWrapper createOrder(CustomerOrders customerOrders);

    GenericResponseWrapper createOrder(OrdersDao ordersDao);

    GenericResponseWrapper viewOrdersAll();

    GenericResponseWrapper viewIndividualOrders(Long ordersId);

    GenericResponseWrapper deleteIndividualOrders(Long ordersId);

    GenericResponseWrapper verifyIndividualOrders(ApproveRequestIdsDao approveRequestIdsDao);

    GenericResponseWrapper errorSection(Exception ex);

    GenericResponseWrapper succesSection();

    GenericResponseWrapper notFound();
}
