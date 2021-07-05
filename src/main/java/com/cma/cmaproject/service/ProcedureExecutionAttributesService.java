package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.model.ProcedureExecutionAttributes;
import com.cma.cmaproject.repository.OveralRiskDescripRepository;
import com.cma.cmaproject.repository.ProcedureExecutionAttributesRepo;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedureExecutionAttributesService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private ProcedureExecutionAttributesRepo procedureExecutionAttributesRepo;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper create(Long loggedInServiceProviderId, GeneralRequestDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            ProcedureExecutionAttributes attributes=new ProcedureExecutionAttributes();
            attributes.setName(wrapper.getName());
            attributes.setActionStatus(Constants.actionApproved);
            attributes.setIntrash(Constants.intrashNO);
            procedureExecutionAttributesRepo.save(attributes);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE PROCEDURE EXECUTION ATTRIBUTES", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(attributes);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE PROCEDURE EXECUTION ATTRIBUTES", "FAILED: System Malfunctioned");
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
            List<ProcedureExecutionAttributes> list = procedureExecutionAttributesRepo.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL PROCEDURE EXECUTION ATTRIBUTES", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL PROCEDURE EXECUTION ATTRIBUTES", "FAILED: System Malfunctioned");
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
        Optional<ProcedureExecutionAttributes> procedures = procedureExecutionAttributesRepo.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (procedures.isPresent()) {
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(procedures.get());
                userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE EXECUTION ATTRIBUTES", "SUCCESS: Successfull");
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE EXECUTION ATTRIBUTES", "FAILED: Attribute with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE EXECUTION ATTRIBUTES", "FAILED: System Malfunctioned");
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
        Optional<ProcedureExecutionAttributes> c = procedureExecutionAttributesRepo.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                ProcedureExecutionAttributes procedures = c.get();
                procedures.setIntrash(Constants.intrashYES);
                procedureExecutionAttributesRepo.save(procedures);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE EXECUTION ATTRIBUTES", "SUCCESS: Deleted Succesfully");
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(procedures);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE EXECUTION ATTRIBUTES", "FAILED: Attribute with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE EXECUTION ATTRIBUTES", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long loggedInServiceProviderId, Long Id, GeneralRequestDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<ProcedureExecutionAttributes> c = procedureExecutionAttributesRepo.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                ProcedureExecutionAttributes procedures = c.get();
                procedures.setName(wrapper.getName());
                procedures.setActionStatus(Constants.actionApproved);
                procedureExecutionAttributesRepo.save(procedures);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE EXECUTION ATTRIBUTES", "SUCCESS: Updated Successfully");
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(procedures);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE EXECUTION ATTRIBUTES", "FAILED: Attribute with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE EXECUTION ATTRIBUTES", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
