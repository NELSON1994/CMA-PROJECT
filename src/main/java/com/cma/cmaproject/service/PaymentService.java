package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ApprovedResponseDao;
import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.model.Payment;
import com.cma.cmaproject.repository.CompanyRepository;
import com.cma.cmaproject.repository.CustomerOrdersRepository;
import com.cma.cmaproject.repository.PaymentsRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.PaymentServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.dao.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements PaymentServiceTemplate {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private CustomerOrdersRepository customerOrdersRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Override
    public GenericResponseWrapper makePayment(PaymentDao payment1) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Payment payment = new Payment();
        try {
            List<CustomerOrders> customerOrders = customerOrdersRepository.findByEmailAndIntrash(payment1.getEmailAddress(), Constants.intrashNO);
            if (customerOrders.size() < 1) {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("No Customer order made with this email : " + payment1.getEmailAddress());
                genericResponseWrapper.setData(null);
            } else {


                CustomerOrders customerOrders1 = customerOrders.get(0);// we get the first order
                customerOrders1.setActionStatus(Constants.paymentStatus3);
                //=======> create company ====================================>

                List<Payment> p = paymentsRepository.findByEmailAddressAndIntrash(payment1.getEmailAddress(), Constants.intrashNO);
                if (p.size() < 1) {
                    Company company = new Company();
                    company.setIndustry(customerOrders1.getIndustry());
                    company.setNumberOfEmployees(customerOrders1.getNumberOfEmployes());
                    company.setCompanyName(customerOrders1.getCompany());
                    company.setIntrash(Constants.intrashNO);
                    company.setActionStatus(Constants.actionUnApproved);
                    companyRepository.save(company);
                    payment.setCompany(company);
                } else {
                    Company company = p.get(0).getCompany();
                    payment.setCompany(company);
                }
                String genCode = paymentRef(payment1.getPaymentMode().trim().toUpperCase());
                payment.setPaymentReference(payment1.getPaymentReference());
                payment.setAmount(payment1.getAmount());
                payment.setEmailAddress(payment1.getEmailAddress());
                payment.setPaymentMode(payment1.getPaymentMode());
                payment.setPaymentReferenceCode(genCode);
                payment.setCustomerOrders(customerOrders1);
                payment.setActionStatus(Constants.paymentStatus1);
                payment.setIntrash(Constants.intrashNO);
                paymentsRepository.save(payment);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(payment);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper viewPaymentAll() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<Payment> list = paymentsRepository.findByIntrash(Constants.intrashNO);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewIndividualPayment(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Payment> payment = paymentsRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
        try {
            if (payment.isPresent()) {
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(payment.get());
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteIndividualPayment(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Payment> payment1 = paymentsRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
        try {
            if (payment1.isPresent()) {
                Payment payment = payment1.get();
                payment.setIntrash(Constants.intrashYES);
                paymentsRepository.save(payment);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(payment);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper declinePayment(ApproveRequestIdsDao approveRequestIdsDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        ApprovedResponseDao approvedResponseDao = new ApprovedResponseDao();
        List<Long> app = new ArrayList<>();
        List<Long> unapp = new ArrayList<>();
        Long[] lksIds = approveRequestIdsDao.getListOfIds();
        try {
            for (Long ordersId : lksIds) {
                Optional<Payment> payment = paymentsRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
                if (payment.isPresent()) {
                    Payment payment1 = payment.get();
                    Company company = payment1.getCompany();
                    company.setActionStatus(Constants.orderStatus3);
                    companyRepository.save(company);
                    payment1.setActionStatus(Constants.orderStatus3);
                    paymentsRepository.save(payment1);
                    app.add(ordersId);

                } else {
                    unapp.add(ordersId);
                }
            }
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            approvedResponseDao.setApprovedIds(app);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("Payment Declined");
            genericResponseWrapper.setData(approvedResponseDao);

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper verifyIndividualPayment(ApproveRequestIdsDao approveRequestIdsDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        ApprovedResponseDao approvedResponseDao = new ApprovedResponseDao();
        List<Long> app = new ArrayList<>();
        List<Long> unapp = new ArrayList<>();
        Long[] lksIds = approveRequestIdsDao.getListOfIds();
        try {
            for (Long ordersId : lksIds) {
                Optional<Payment> payment = paymentsRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
                if (payment.isPresent()) {
                    Payment payment1 = payment.get();
                    Company company = payment1.getCompany();
                    company.setActionStatus(Constants.actionApproved);
                    companyRepository.save(company);

                    payment1.setActionStatus(Constants.paymentStatus2);
                    paymentsRepository.save(payment1);
                    app.add(ordersId);
                } else {
                    unapp.add(ordersId);
                }
            }
            approvedResponseDao.setApprovedIds(app);
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("Approved Successfully");
            genericResponseWrapper.setData(approvedResponseDao);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }


    @Override
    public GenericResponseWrapper updatePayment(Long userId, Long paymentId, PaymentDao paymentDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(userId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Payment> payment = paymentsRepository.findByIdAndIntrash(paymentId, Constants.intrashNO);
        try {
            if (payment.isPresent()) {
                Payment payment1 = payment.get();
                payment1.setPaymentMode(paymentDao.getPaymentMode());
                payment1.setEmailAddress(paymentDao.getEmailAddress());
                payment1.setAmount(paymentDao.getAmount());
                payment1.setPaymentReference(paymentDao.getPaymentReference());
                payment1.setActionStatus(Constants.paymentStatus2);
                paymentsRepository.save(payment1);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PAYMENT", "SUCCESS: Updated Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(payment1);
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PAYMENT", "FAILED: Payment with Id : " + paymentId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Payment with Id : " + paymentId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE PAYMENT", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    // generate PaymentCode
    public String paymentRef(String paymentMode) {
        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMyyyyhhmmss");
        String strDate = formatter.format(d);
        String generatedCode = paymentMode + strDate;
        return generatedCode;
    }
}
