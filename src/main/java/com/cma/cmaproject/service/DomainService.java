package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.DomainDao;
import com.cma.cmaproject.model.Controls;
import com.cma.cmaproject.model.Domain;
import com.cma.cmaproject.repository.ControlRepository;
import com.cma.cmaproject.repository.DomainRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DomainService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper create(Long loggedInServiceProviderId, DomainDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Domain domain = new Domain();
            domain.setDomainName(wrapper.getDomainName());
            domain.setDomainDescription(wrapper.getDomainDescription());
            domain.setActionStatus(Constants.actionApproved);
            domain.setIntrash(Constants.intrashNO);
            domainRepository.save(domain);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE DOMAIN", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(domain);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE DOMAIN", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper viewAll(Long loggedInServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<Domain> list = domainRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL DOMAINS", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL DOMAINS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper view(Long loggedInServiceProviderId, Long Id) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Domain> domain = domainRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (domain.isPresent()) {
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(domain.get());
                userServiceTemplate.saveAuditRails(name, company8, "VIEW DOMAIN", "SUCCESS: Successfull");
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Domain with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW DOMAIN", "FAILED: Domain with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW DOMAIN", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper delete(Long loggedInServiceProviderId, Long Id) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Domain> c = domainRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Domain domain = c.get();
                domain.setIntrash(Constants.intrashYES);
                domainRepository.save(domain);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "DELETE DOMAIN", "SUCCESS: Deleted Succesfully");
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(domain);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Domain with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE DOMAIN", "FAILED: Domain with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE DOMAIN", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long loggedInServiceProviderId, Long Id, DomainDao domainDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Domain> c = domainRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Domain domain = c.get();
                domain.setDomainDescription(domainDao.getDomainDescription());
                domain.setDomainName(domainDao.getDomainName());
                domain.setActionStatus(Constants.actionApproved);
                domainRepository.save(domain);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE DOMAIN", "SUCCESS: Updated Successfully");
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(domain);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Domain with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE DOMAIN", "FAILED: Domain with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE DOMAIN", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewLinked(Long loggedInServiceProviderId, Long Id) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Domain> domain = domainRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (domain.isPresent()) {
                Domain d=domain.get();
                List<Controls> controls=controlRepository.findByDomain(d);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(controls);
                userServiceTemplate.saveAuditRails(name, company8, "VIEW LINKED CONTROLS TO DOMAIN", "SUCCESS: Successfull");
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Domain with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW LINKED CONTROLS TO DOMAIN", "FAILED: Domain with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW LINKED CONTROLS TO DOMAIN", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
