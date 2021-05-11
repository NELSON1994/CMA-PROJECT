package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.model.Payment;
import com.cma.cmaproject.repository.CustomerOrdersRepository;
import com.cma.cmaproject.repository.PaymentsRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.PaymentServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    @Override
    public GenericResponseWrapper makePayment(Payment payment) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<CustomerOrders> customerOrders=customerOrdersRepository.findByEmailAndIntrash(payment.getEmailAddress(),Constants.intrashNO);
            if (customerOrders.size()<1){
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("No Customer order made with that email : "+payment.getEmailAddress());
                genericResponseWrapper.setData(null);
            }
            else{
                CustomerOrders customerOrders1=customerOrders.get(0);// we get the first order
                customerOrders1.setActionStatus(Constants.paymentStatus3);
                String genCode=paymentRef(payment.getPaymentMode().trim().toUpperCase());
                payment.setPaymentReferenceCode(genCode);
                payment.setCustomerOrders(customerOrders1);
                payment.setActionStatus(Constants.paymentStatus1);
                payment.setIntrash(Constants.intrashNO);
                paymentsRepository.save(payment);
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
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
            genericResponseWrapper=customerOrderServiceTemplate.succesSection();
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
        Optional<Payment> payment=paymentsRepository.findByIdAndIntrash(ordersId,Constants.intrashNO);
        try {
            if (payment.isPresent()){
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(payment.get());
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
    @Override
    public GenericResponseWrapper deleteIndividualPayment(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Payment> payment1=paymentsRepository.findByIdAndIntrash(ordersId,Constants.intrashNO);
        try {
            if (payment1.isPresent()){
                Payment payment=payment1.get();
                payment.setIntrash(Constants.intrashYES);
                paymentsRepository.save(payment);
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(payment);
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper verifyIndividualPayment(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Payment> payment=paymentsRepository.findByIdAndIntrash(ordersId,Constants.intrashNO);
        try {
            if (payment.isPresent()){
                Payment payment1=payment.get();
                payment1.setActionStatus(Constants.paymentStatus2);
                paymentsRepository.save(payment1);
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Verified Successfully");
                genericResponseWrapper.setData(payment1);
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
    // generate PaymentCode
    public String paymentRef(String paymentMode){
        Date d=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMyyyyhhmmss");
        String strDate = formatter.format(d);
        String generatedCode=paymentMode+strDate;
        return generatedCode;
    }
}
