package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ProcedureDao;
import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.OveralRiskDescription;
import com.cma.cmaproject.model.Procedures;
import com.cma.cmaproject.model.RiskProfile;
import com.cma.cmaproject.repository.CompanyRepository;
import com.cma.cmaproject.repository.OveralRiskDescripRepository;
import com.cma.cmaproject.repository.ProceduresRepository;
import com.cma.cmaproject.repository.RiskProfileRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.ProcedureRisksWrapper;
import com.cma.cmaproject.wrappers.RiskIDsWrapper;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedureService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private ProceduresRepository proceduresRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RiskProfileRepository riskProfileRepository;
    @Autowired
    private OveralRiskDescripRepository overalRiskDescripRepository;

    public GenericResponseWrapper create(Long loggedInServiceProviderId, ProcedureDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Procedures procedures = new Procedures();
            procedures.setProcedureName(wrapper.getProcedureName());
            procedures.setProcedureDescription(wrapper.getProcedureDescription());
            procedures.setActionStatus(Constants.actionApproved);
            procedures.setIntrash(Constants.intrashNO);
            proceduresRepository.save(procedures);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE PROCEDURE", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(procedures);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE PROCEDURE", "FAILED: System Malfunctioned");
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
            List<Procedures> list = proceduresRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL PROCEDURE", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL PROCEDURE", "FAILED: System Malfunctioned");
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
        Optional<Procedures> procedures = proceduresRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (procedures.isPresent()) {
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(procedures.get());
                userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE", "SUCCESS: Successfull");
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE", "FAILED: Procedure with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE", "FAILED: System Malfunctioned");
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
        Optional<Procedures> c = proceduresRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Procedures procedures = c.get();
                procedures.setIntrash(Constants.intrashYES);
                proceduresRepository.save(procedures);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE", "SUCCESS: Deleted Succesfully");
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(procedures);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE", "FAILED: Procedure with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long loggedInServiceProviderId, Long Id, ProcedureDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Procedures> c = proceduresRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Procedures procedures = c.get();
                procedures.setProcedureName(wrapper.getProcedureName());
                procedures.setProcedureDescription(wrapper.getProcedureDescription());
                procedures.setActionStatus(Constants.actionApproved);
                procedures.setModifiedDate(new Date());
                proceduresRepository.save(procedures);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE", "SUCCESS: Updated Successfully");
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(procedures);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE", "FAILED: Procedure with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper linkProcedureToRisks(Long loggedInServiceProviderId, Long Id, Long riskProfileId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Procedures> c = proceduresRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        ProcedureRisksWrapper procedureRisksWrapper = new ProcedureRisksWrapper();
        try {
            if (c.isPresent()) {
                Procedures procedures = c.get();
                Optional<RiskProfile> riskProfile=riskProfileRepository.findByIdAndIntrash(riskProfileId,Constants.intrashNO);
                if(riskProfile.isPresent()){
                    RiskProfile riskProfile1=riskProfile.get();
                    procedureRisksWrapper.setProcedures(procedures);
                    riskProfile1.setProcedures(procedures);
                    userServiceTemplate.saveAuditRails(name, company8, "LINK PROCEDURE TO RISKS", "SUCCESS: Risk Successfully Linked to Procedure");
                    genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                    genericResponseWrapper.setData(riskProfile1);
                    genericResponseWrapper.setMessage("Risk Profile Successfully Linked to the Procedure");
                }
                else{
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("Risk Profile with Id : " + riskProfileId + " NOT Found");
                    userServiceTemplate.saveAuditRails(name, company8, "LINK PROCEDURE TO RISKS", "FAILED: Risk Profile with Id : " + riskProfileId + " NOT Found");
                    genericResponseWrapper.setData(null);
                }

            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "LINK PROCEDURE TO RISKS", "FAILED: Procedure with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "LINK PROCEDURE TO RISKS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewRisksLinkedToProcedure(Long loggedInServiceProviderId, Long Id) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Procedures> c = proceduresRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Procedures procedures = c.get();
                List<OveralRiskDescription> list=overalRiskDescripRepository.findByProcedures(procedures);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "VIEW RISKS LINKED TO A PROCEDURE", "SUCCESS: Successfully");
                genericResponseWrapper.setData(list);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW RISKS LINKED TO A PROCEDURE", "FAILED: Procedure with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW RISKS LINKED TO A PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
