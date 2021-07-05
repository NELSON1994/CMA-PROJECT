package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ApprovedResponseDao;
import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.repository.CustomerOrdersRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.dao.OrdersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrdersService implements CustomerOrderServiceTemplate {
    @Autowired
    private CustomerOrdersRepository customerOrdersRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Override
    public GenericResponseWrapper createOrder(OrdersDao ordersDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        CustomerOrders customerOrders = new CustomerOrders();
        try {
            customerOrders.setFirstName(ordersDao.getFirstName());
            customerOrders.setLastName(ordersDao.getLastName());
            customerOrders.setEmail(ordersDao.getEmail());
            customerOrders.setCompany(ordersDao.getCompany().trim().toUpperCase());
            customerOrders.setNumberOfEmployes(ordersDao.getNumberOfEmployes());
            customerOrders.setIndustry(ordersDao.getIndustry());
            customerOrders.setActionStatus(Constants.orderStatus1);
            customerOrders.setIntrash(Constants.intrashNO);
            customerOrdersRepository.save(customerOrders);
            userServiceTemplate.saveAuditRails(customerOrders.getFirstName() + " " + customerOrders.getLastName(), customerOrders.getCompany(), "CLIENT ORDERING", "SUCCESS: Order Made Successfully");
            genericResponseWrapper = succesSection();
            genericResponseWrapper.setData(customerOrders);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(null, null, "CLIENT ORDERING", "FAILED: System Error Occurred");
            genericResponseWrapper = errorSection(ex);
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
            userServiceTemplate.saveAuditRails(null, null, "VIEW CLIENT ORDERS", "SUCCESS: Successful");
            genericResponseWrapper = succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(null, null, "VIEW CLIENT ORDERS", "FAILED: System Error Occurred");
            genericResponseWrapper = errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewIndividualOrders(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<CustomerOrders> c = customerOrdersRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                genericResponseWrapper = succesSection();
                genericResponseWrapper.setData(c.get());
                userServiceTemplate.saveAuditRails(null, null, "VIEW ONE ORDER", "SUCCESS: Successful");
            } else {
                userServiceTemplate.saveAuditRails(null, null, "VIEW ONE ORDER", "FAILED: Order Not Found");
                genericResponseWrapper = notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = errorSection(ex);
            ex.printStackTrace();
            userServiceTemplate.saveAuditRails(null, null, "VIEW ONE ORDER", "FAILED: System Error (Malfunction)");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteIndividualOrders(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<CustomerOrders> c = customerOrdersRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                CustomerOrders customerOrders = c.get();
                customerOrders.setIntrash(Constants.intrashYES);
                customerOrdersRepository.save(customerOrders);
                genericResponseWrapper = succesSection();
                userServiceTemplate.saveAuditRails(null, null, "DELETE ORDER", "SUCCESS: Order deleted successfully");
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(customerOrders);
            } else {
                userServiceTemplate.saveAuditRails(null, null, "DELETE ORDER", "FAILED: Order Not Found");
                genericResponseWrapper = notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = errorSection(ex);
            userServiceTemplate.saveAuditRails(null, null, "DELETE ORDER", "FAILED: System Malfunctioned");
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper verifyIndividualOrders(ApproveRequestIdsDao approveRequestIdsDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        ApprovedResponseDao approvedResponseDao = new ApprovedResponseDao();
        List<Long> app = new ArrayList<>();
        List<Long> unapp = new ArrayList<>();
        Long[] ids = approveRequestIdsDao.getListOfIds();
        try {
            for (Long ordersId : ids) {
                Optional<CustomerOrders> c = customerOrdersRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
                if (c.isPresent()) {
                    CustomerOrders customerOrders = c.get();
                    customerOrders.setActionStatus(Constants.paymentStatus2);
                    customerOrdersRepository.save(customerOrders);
                    app.add(ordersId);
                } else {
                    unapp.add(ordersId);
                }
            }
            approvedResponseDao.setApprovedIds(app);
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            userServiceTemplate.saveAuditRails(null, null, "VERIFY ORDER", "SUCCESS: Order Verified Successfully");
            genericResponseWrapper = succesSection();
            genericResponseWrapper.setMessage("Verified Successfully");
            genericResponseWrapper.setData(approvedResponseDao);

        } catch (Exception ex) {
            genericResponseWrapper = errorSection(ex);
            userServiceTemplate.saveAuditRails(null, null, "VERIFY ORDER", "FAILED: System Malfunctioned");
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper errorSection(Exception ex) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        genericResponseWrapper.setCode(Constants.errorCode);
        genericResponseWrapper.setStatus(Constants.errorResponse);
        genericResponseWrapper.setMessage(ex.getMessage());
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper succesSection() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        genericResponseWrapper.setCode(Constants.successCode);
        genericResponseWrapper.setStatus(Constants.successResponseMessage);
        genericResponseWrapper.setMessage(Constants.successMessage);
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper notFound() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        genericResponseWrapper.setCode(Constants.errorCodNotFound);
        genericResponseWrapper.setStatus(Constants.errorResponse);
        genericResponseWrapper.setMessage(Constants.notFoundResponseMessage);
        return genericResponseWrapper;
    }


}
