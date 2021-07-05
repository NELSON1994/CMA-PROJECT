package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ControlDao;
import com.cma.cmaproject.dao.ListOfControlsDao;
import com.cma.cmaproject.dao.ListOfProceduresDao;
import com.cma.cmaproject.model.Controls;
import com.cma.cmaproject.model.Domain;
import com.cma.cmaproject.model.Procedures;
import com.cma.cmaproject.repository.ControlRepository;
import com.cma.cmaproject.repository.DomainRepository;
import com.cma.cmaproject.repository.ProceduresRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.ControlProceduresWrapper;
import com.cma.cmaproject.wrappers.DomainControlsWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ControlService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ProceduresRepository proceduresRepository;

    public GenericResponseWrapper create(Long loggedInServiceProviderId, ControlDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Controls controls = new Controls();
            controls.setControlName(wrapper.getControlName());
            controls.setControlStandard(wrapper.getControlStandard());
            controls.setControlStandardClause(wrapper.getControlStandardClause());
            controls.setControlStatement(wrapper.getControlStatement());
            controls.setActionStatus(Constants.actionApproved);
            controls.setIntrash(Constants.intrashNO);
            controlRepository.save(controls);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE CONTROL", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(controls);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE CONTROL", "FAILED: System Malfunctioned");
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
            List<Controls> list = controlRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL CONTROL", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL CONTROL", "FAILED: System Malfunctioned");
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
        Optional<Controls> controls = controlRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (controls.isPresent()) {
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(controls.get());
                userServiceTemplate.saveAuditRails(name, company8, "VIEW CONTROL", "SUCCESS: Successfull");
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Control with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW CONTROL", "FAILED: Control with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW CONTROL", "FAILED: System Malfunctioned");
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
        Optional<Controls> c = controlRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Controls controls = c.get();
                controls.setIntrash(Constants.intrashYES);
                controlRepository.save(controls);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "DELETE CONTROL", "SUCCESS: Deleted Succesfully");
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(controls);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Control with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE CONTROL", "FAILED: Control with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE CONTROL", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long loggedInServiceProviderId, Long Id, ControlDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Controls> c = controlRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Controls controls = c.get();
                controls.setControlName(wrapper.getControlName());
                controls.setControlStandard(wrapper.getControlStandard());
                controls.setControlStandardClause(wrapper.getControlStandardClause());
                controls.setControlStatement(wrapper.getControlStatement());
                controls.setModifiedDate(new Date());
                controls.setActionStatus(Constants.actionApproved);
                controlRepository.save(controls);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE CONTROL", "SUCCESS: Updated Successfully");
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(controls);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Control with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE CONTROL", "FAILED: Control with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE CONTROL", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper linkControlsToDomain(Long loggedInServiceProviderId, Long domainId, ListOfControlsDao listOfControlsDao) {
        Long[] controlsId=listOfControlsDao.getControlIDS();
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            //FIND DOMAIN
            Optional<Domain> domain = domainRepository.findByIdAndIntrash(domainId, Constants.intrashNO);
            if (domain.isPresent()) {
                Domain domain1 = domain.get();
                List<Long> controlsNotFound = new ArrayList<>();
                List<Long> controlsFound = new ArrayList<>();
                List<Controls> controlslist = new ArrayList<>();
                for (Long controlID : controlsId) {
                    Optional<Controls> controls = controlRepository.findByIdAndIntrash(controlID, Constants.intrashNO);
                    if (controls.isPresent()) {
                        Controls controls1 = controls.get();
                        controls1.setDomain(domain1);
                        controlRepository.save(controls1);
                        controlslist.add(controls1);
                        controlsFound.add(controlID);
                    } else {
                        controlsNotFound.add(controlID);

                    }
                }
                int len = controlslist.size();
                DomainControlsWrapper domainControlsWrapper = new DomainControlsWrapper();
                domainControlsWrapper.setDomain(domain1);
                domainControlsWrapper.setControlsList(controlslist);
                userServiceTemplate.saveAuditRails(name, company8, "LINK CONTROL TO DOMAIN", "SUCCESS: Successfully Linked " + len + " Controls To the Domain ");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Successfully Linked " + len + " Controls To the Domain ");
                genericResponseWrapper.setData(domainControlsWrapper);
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "LINK CONTROL TO DOMAIN", "FAILED: Domain with Id : " + domainId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Domain with Id : " + domainId + " NOT Found");

            }
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "LINK CONTROL TO DOMAIN", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper linkProceduresToControl(Long loggedInServiceProviderId, Long controlId, ListOfProceduresDao listOfProceduresDao) {
        Long[] procedureIds=listOfProceduresDao.getProceduresIDS();
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<Controls> controld = controlRepository.findByIdAndIntrash(controlId, Constants.intrashNO);
            if (controld.isPresent()) {
                Controls controls2 = controld.get();
                List<Long> proceduresNotFound = new ArrayList<>();
                List<Procedures> proceduresFound = new ArrayList<>();
                for (Long procedureID : procedureIds) {
                    Optional<Procedures> procedures = proceduresRepository.findByIdAndIntrash(procedureID, Constants.intrashNO);
                    if (procedures.isPresent()) {
                        Procedures procedures1 = procedures.get();
                        procedures1.setControls(controls2);
                        proceduresRepository.save(procedures1);
                        proceduresFound.add(procedures1);

                    }
                }
                int len = proceduresFound.size();
                ControlProceduresWrapper controlProceduresWrapper = new ControlProceduresWrapper();
                controlProceduresWrapper.setControls(controls2);
                controlProceduresWrapper.setProceduresList(proceduresFound);
                userServiceTemplate.saveAuditRails(name, company8, "LINK PROCEDURE TO CONTROL", "SUCCESS: Successfully Linked " + len + " Procedures To the Control ");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Successfully Linked " + len + " Procedures To the Control ");
                genericResponseWrapper.setData(controlProceduresWrapper);
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "LINK PROCEDURE TO CONTROL", "FAILED: Control with Id : " + controlId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Control with Id : " + controlId + " NOT Found");

            }
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "LINK PROCEDURE TO CONTROL", "FAILED: System Malfunctioned");
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
        Optional<Controls> controls = controlRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (controls.isPresent()) {
                Controls controls1=controls.get();
                List<Procedures> procedures=proceduresRepository.findByControls(controls1);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(procedures);
                userServiceTemplate.saveAuditRails(name, company8, "VIEW LINKED PROCEDURES TO CONTROL", "SUCCESS: Successfull");
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Control with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW LINKED PROCEDURES TO CONTROL", "FAILED: Control with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW LINKED PROCEDURES TO CONTROL", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
