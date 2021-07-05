package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.dao.PaymentDao;

public interface PaymentServiceTemplate {

    GenericResponseWrapper makePayment(PaymentDao payment1);

    GenericResponseWrapper viewPaymentAll();

    GenericResponseWrapper viewIndividualPayment(Long ordersId);

    GenericResponseWrapper deleteIndividualPayment(Long ordersId);

    GenericResponseWrapper declinePayment(ApproveRequestIdsDao approveRequestIdsDao);

    GenericResponseWrapper verifyIndividualPayment(ApproveRequestIdsDao approveRequestIdsDao);

    GenericResponseWrapper updatePayment(Long userId, Long paymentId, PaymentDao paymentDao);
}
