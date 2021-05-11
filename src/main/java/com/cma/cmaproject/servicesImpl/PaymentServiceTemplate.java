package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.model.Payment;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;

public interface PaymentServiceTemplate {

    GenericResponseWrapper makePayment(Payment payment);

    GenericResponseWrapper viewPaymentAll();

    GenericResponseWrapper viewIndividualPayment(Long ordersId);

    GenericResponseWrapper deleteIndividualPayment(Long ordersId);

    GenericResponseWrapper verifyIndividualPayment(Long ordersId);
}
