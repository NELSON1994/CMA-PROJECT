package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.Industry;
import com.cma.cmaproject.repository.IndustryRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IndustryService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper createIndustry(GeneralRequestDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            Industry industry=new Industry();
            industry.setIndustryName(wrapper.getName());
            industry.setActionStatus(Constants.actionApproved);
            industry.setIntrash(Constants.intrashNO);
            industryRepository.save(industry);
            userServiceTemplate.saveAuditRails(null,null,"CREATE INDUSTRY","SUCCESS: Industry created Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(industry);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            userServiceTemplate.saveAuditRails(null,null,"CREATE INDUSTRY","FAILED: System Malfunctioned");
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper viewAllIndustry() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<Industry> list = industryRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(null,null,"VIEW ALL INDUSTRY","SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            userServiceTemplate.saveAuditRails(null,null,"VIEW ALL INDUSTRY","FAILED: System Malfunctioned");
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewIndustry(Long industryId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Industry> industry = industryRepository.findByIdAndIntrash(industryId, Constants.intrashNO);
        try {
            if (industry.isPresent()) {
                userServiceTemplate.saveAuditRails(null,null,"VIEW ALL INDUSTRY","SUCCESS: Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(industry.get());
            } else {
                userServiceTemplate.saveAuditRails(null,null,"VIEW ONE INDUSTRY","FAILED: Industry Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(null,null,"VIEW ONE INDUSTRY","FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper deleteIndustry(Long industryId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Industry> c = industryRepository.findByIdAndIntrash(industryId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Industry industry = c.get();
                industry.setIntrash(Constants.intrashYES);
                industryRepository.save(industry);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(null,null,"DELETE INDUSTRY","FAILED: Deleted Succesfully");
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(industry);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                userServiceTemplate.saveAuditRails(null,null,"DELETE INDUSTRY","FAILED: Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            userServiceTemplate.saveAuditRails(null,null,"DELETE INDUSTRY","FAILED: System Malfunctioned");
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
