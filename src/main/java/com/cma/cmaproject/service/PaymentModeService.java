package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.PaymentDao;
import com.cma.cmaproject.model.Payment;
import com.cma.cmaproject.model.PaymentMode;
import com.cma.cmaproject.repository.PaymentModeRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentModeService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private PaymentModeRepository paymentModeRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper createPMode(GeneralRequestDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            PaymentMode paymentMode=new PaymentMode();
            paymentMode.setModeName(wrapper.getName());
            paymentMode.setActionStatus(Constants.actionApproved);
            paymentMode.setIntrash(Constants.intrashNO);
            paymentModeRepository.save(paymentMode);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(paymentMode);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper viewAllPMode() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<PaymentMode> list = paymentModeRepository.findByIntrash(Constants.intrashNO);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewPMode(Long industryId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<PaymentMode> p = paymentModeRepository.findByIdAndIntrash(industryId, Constants.intrashNO);
        try {
            if (p.isPresent()) {
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(p.get());
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

    public GenericResponseWrapper deletePMode(Long industryId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<PaymentMode> c = paymentModeRepository.findByIdAndIntrash(industryId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                PaymentMode paymentMode = c.get();
                paymentMode.setIntrash(Constants.intrashYES);
                paymentModeRepository.save(paymentMode);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(paymentMode);
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


}
