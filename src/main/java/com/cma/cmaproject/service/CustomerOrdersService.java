package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.repository.CustomerOrdersRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrdersService implements CustomerOrderServiceTemplate {
    @Autowired
    private CustomerOrdersRepository customerOrdersRepository;

    @Override
    public GenericResponseWrapper createOrder(CustomerOrders customerOrders) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            customerOrders.setActionStatus(Constants.orderStatus1);
            customerOrders.setIntrash(Constants.intrashNO);
            customerOrdersRepository.save(customerOrders);
            genericResponseWrapper=succesSection();
            genericResponseWrapper.setData(customerOrders);
        } catch (Exception ex) {
            genericResponseWrapper =errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper viewOrdersAll() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<CustomerOrders> list = customerOrdersRepository.findByIntrash(Constants.intrashNO);
            genericResponseWrapper=succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper =errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewIndividualOrders(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<CustomerOrders> c=customerOrdersRepository.findByIdAndIntrash(ordersId,Constants.intrashNO);
        try {
            if (c.isPresent()){
                genericResponseWrapper=succesSection();
                genericResponseWrapper.setData(c.get());
            }
            else{
                genericResponseWrapper=notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper =errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteIndividualOrders(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<CustomerOrders> c=customerOrdersRepository.findByIdAndIntrash(ordersId,Constants.intrashNO);
        try {
            if (c.isPresent()){
                CustomerOrders customerOrders=c.get();
                customerOrders.setIntrash(Constants.intrashYES);
                customerOrdersRepository.save(customerOrders);
                genericResponseWrapper=succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(customerOrders);
            }
            else{
                genericResponseWrapper=notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper =errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper verifyIndividualOrders(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<CustomerOrders> c=customerOrdersRepository.findByIdAndIntrash(ordersId,Constants.intrashNO);
        try {
            if (c.isPresent()){
                CustomerOrders customerOrders=c.get();
                customerOrders.setActionStatus(Constants.paymentStatus2);
                customerOrdersRepository.save(customerOrders);
                genericResponseWrapper=succesSection();
                genericResponseWrapper.setMessage("Verified Successfully");
                genericResponseWrapper.setData(customerOrders);
            }
            else{
                genericResponseWrapper=notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper =errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
    @Override
    public GenericResponseWrapper errorSection(Exception ex){
        GenericResponseWrapper genericResponseWrapper=new GenericResponseWrapper();
        genericResponseWrapper.setCode(Constants.errorCode);
        genericResponseWrapper.setStatus(Constants.errorResponse);
        genericResponseWrapper.setMessage(ex.getMessage());
        return genericResponseWrapper;
    }
    @Override
    public GenericResponseWrapper succesSection(){
        GenericResponseWrapper genericResponseWrapper=new GenericResponseWrapper();
        genericResponseWrapper.setCode(Constants.successCode);
        genericResponseWrapper.setStatus(Constants.successResponseMessage);
        genericResponseWrapper.setMessage(Constants.successMessage);
        return genericResponseWrapper;
    }
    @Override
    public GenericResponseWrapper notFound(){
        GenericResponseWrapper genericResponseWrapper=new GenericResponseWrapper();
        genericResponseWrapper.setCode(Constants.errorCodNotFound);
        genericResponseWrapper.setStatus(Constants.errorResponse);
        genericResponseWrapper.setMessage(Constants.notFoundResponseMessage);
        return genericResponseWrapper;
    }


}
