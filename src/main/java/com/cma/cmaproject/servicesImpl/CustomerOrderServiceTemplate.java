package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;

public interface CustomerOrderServiceTemplate {

    GenericResponseWrapper createOrder(CustomerOrders customerOrders);

    GenericResponseWrapper viewOrdersAll();

    GenericResponseWrapper viewIndividualOrders(Long ordersId);

    GenericResponseWrapper deleteIndividualOrders(Long ordersId);

    GenericResponseWrapper verifyIndividualOrders(Long ordersId);

    GenericResponseWrapper errorSection(Exception ex);

    GenericResponseWrapper succesSection();

    GenericResponseWrapper notFound();
}
