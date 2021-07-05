package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.AuditLogs;
import com.cma.cmaproject.model.User;
import com.cma.cmaproject.repository.AuditRailsRepository;
import com.cma.cmaproject.repository.UserRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuditRailsService {
    @Autowired
    private AuditRailsRepository auditRailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper viewAuditLogs(Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(loggedUserId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                String company = user1.getCompany().getCompanyName();
                List<AuditLogs> auditLogs = auditRailsRepository.findByCompanyName(company);
                userServiceTemplate.saveAuditRails(name, company8, "VIEW AUDIT RAILS", "SUCCESS: Viewed Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(auditLogs);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with Id : " + loggedUserId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW AUDIT RAILS", "FAILED: User with ID : " + loggedUserId + " NOT Found");
            }
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW AUDIT RAILS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewAllAuditLogs(Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(loggedUserId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                List<AuditLogs> auditLogs = auditRailsRepository.findAll();
                userServiceTemplate.saveAuditRails(name, company8, "VIEW AUDIT RAILS", "SUCCESS: Viewed Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(auditLogs);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with Id : " + loggedUserId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW AUDIT RAILS", "FAILED: User with ID : " + loggedUserId + " NOT Found");
            }
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW AUDIT RAILS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
