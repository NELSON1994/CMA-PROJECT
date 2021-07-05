package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.dao.ProcedureAssessmentDao;
import com.cma.cmaproject.model.*;
import com.cma.cmaproject.repository.*;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedureAssessmentService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProcedureAssessmentRepository procedureAssessmentRepository;

    @Autowired
    private ProcedureExecutionRepository procedureExecutionRepository;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ControlScoresRepository controlScoresRepository;

    @Autowired
    private DomainScoresRepository domainScoresRepository;

    @Autowired
    private RiskMatrixRepository riskMatrixRepository;

    @Autowired
    private EvidenceFileRepository evidenceFileRepository;


    public GenericResponseWrapper create(Long loggedInServiceProviderId, ProcedureAssessmentDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Company company=null;
            Optional<User> user=userRepository.findByIdAndIntrash(loggedInServiceProviderId,Constants.intrashNO);
            if(user.isPresent()){
                User user1=user.get();
                company=user1.getCompany();
            }
            ProcedureAssessment assessment=new ProcedureAssessment();
            assessment.setAssessmentName(wrapper.getAssessmentName());
            assessment.setStartDate(wrapper.getStartDate());
            assessment.setEndDate(wrapper.getEndDate());
            assessment.setActionStatus(Constants.actionApproved);
            assessment.setIntrash(Constants.intrashNO);
            assessment.setCompany(company);
            procedureAssessmentRepository.save(assessment);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE PROCEDURE ASSESSMENT", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(assessment);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE PROCEDURE ASSESSMENT", "FAILED: System Malfunctioned");
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
            Company company=null;
            List<ProcedureAssessment> procedureAssessments=new ArrayList<>();
            Optional<User> user=userRepository.findByIdAndIntrash(loggedInServiceProviderId,Constants.intrashNO);
            if(user.isPresent()){
                User user1=user.get();
                company=user1.getCompany();
            }
            if(company != null){
                procedureAssessments = procedureAssessmentRepository.findByIntrashAndCompany(Constants.intrashNO,company);
            }
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL PROCEDURE ASSESSMENT", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(procedureAssessments);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL PROCEDURE ASSESSMENT", "FAILED: System Malfunctioned");
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
        Optional<ProcedureAssessment> procedures = procedureAssessmentRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        RiskMatrix riskMatrix=null;
        EvidenceFileUpload evidenceFileUpload=null;
        Procedures procedures1=null;
        Controls controls=null;
        Domain domain=null;
        ControlScores controlScores=null;
        DomainScores domainScores=null;
        AssessmentViewWrapper assessmentViewWrappers=null;
        AssessmentWrapper22 assessmentWrapper22=null;
        AssessmentWrapper3 assessmentWrapper3=null;
        AssessmentWrapper33 assessmentWrapper33=null;
        List<AssessmentWrapper3> assessmentWrapper3List=new ArrayList<>();
        try {
            if (procedures.isPresent()) {
                ProcedureAssessment procedureAssessment=procedures.get();
                List<ProcedureExecution> procedureExecution=procedureExecutionRepository.findByIntrashAndProcedureAssessment(Constants.intrashYES,procedureAssessment);
                if(procedureExecution.size()>0) {
                    for(ProcedureExecution procedureExecution1:procedureExecution){
                        evidenceFileUpload = procedureExecution1.getEvidenceFileUpload();
                        procedures1 = procedureExecution1.getProcedures();
                        riskMatrix = procedureExecution1.getRiskMatrix();
                        controls = procedures1.getControls();
                        domain = controls.getDomain();
                        Optional<ControlScores> controlScores1=controlScoresRepository.findByControls(controls);
                        if(controlScores1.isPresent()){
                            controlScores=controlScores1.get();
                        }
                        Optional<DomainScores> domainScores1=domainScoresRepository.findByDomain(domain);
                        if(domainScores1.isPresent()){
                            domainScores=domainScores1.get();
                        }

                        assessmentWrapper33.setEvidenceFileUpload(evidenceFileUpload);
                        assessmentWrapper33.setProcedureExecution(procedureExecution1);
                        assessmentWrapper33.setRiskMatrix(riskMatrix);

                        assessmentWrapper3.setAssessmentWrapper33(assessmentWrapper33);
                        assessmentWrapper3.setProcedures(procedures1);

                        assessmentWrapper3List.add(assessmentWrapper3);
                    }

                    assessmentWrapper22.setControls(controls);
                    assessmentWrapper22.setControlScore(controlScores.getControlScore());
                    assessmentWrapper22.setAssessmentWrapper3(assessmentWrapper3List);

                    assessmentViewWrappers.setAssessmentWrapper22(assessmentWrapper22);
                    assessmentViewWrappers.setDomainScore(domainScores.getDomainScore());
                    assessmentViewWrappers.setDomain(domain);
                    assessmentViewWrappers.setProcedureAssessment(procedureAssessment);

                    genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                    genericResponseWrapper.setData(assessmentViewWrappers);
                    userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE ASSESSMENT", "SUCCESS: Successfull");
                }
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure Assessment with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE ASSESSMENT", "FAILED: Procedure Assessment with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW PROCEDURE ASSESSMENT", "FAILED: System Malfunctioned");
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
        Optional<ProcedureAssessment> c = procedureAssessmentRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                ProcedureAssessment procedures = c.get();
                procedures.setIntrash(Constants.intrashYES);
                procedureAssessmentRepository.save(procedures);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE ASSESSMENT", "SUCCESS: Deleted Succesfully");
                genericResponseWrapper.setMessage("Deleted Successfully");
                genericResponseWrapper.setData(procedures);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure Assessment with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE ASSESSMENT", "FAILED: Procedure Assessment with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE PROCEDURE ASSESSMENT", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long loggedInServiceProviderId, Long Id, ProcedureAssessmentDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<ProcedureAssessment> c = procedureAssessmentRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                ProcedureAssessment assessment = c.get();
                assessment.setAssessmentName(wrapper.getAssessmentName());
                assessment.setStartDate(wrapper.getStartDate());
                assessment.setEndDate(wrapper.getEndDate());
                assessment.setActionStatus(Constants.actionApproved);
                procedureAssessmentRepository.save(assessment);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE ASSESSMENT", "SUCCESS: Updated Successfully");
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(assessment);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure Assessment with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE ASSESSMENT", "FAILED: Procedure Assessment with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE PROCEDURE ASSESSMENT", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
