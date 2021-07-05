package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.model.ProcedureExecution;
import com.cma.cmaproject.repository.*;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedurePreparationService {

    @Autowired
    private ProcedureExecutionRepository procedureExecutionRepository;

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper prepare(Long loggedInServiceProviderId, ApproveRequestIdsDao approveRequestIdsDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        List<ProcedureExecution> prepared = new ArrayList<>();
        try {
            Long[] procedureExecutionId1 = approveRequestIdsDao.getListOfIds();
            for (Long procedureExecutionId : procedureExecutionId1) {
                Optional<ProcedureExecution> c = procedureExecutionRepository.findByIdAndIntrash(procedureExecutionId, Constants.intrashNO);
                if (c.isPresent()) {
                    ProcedureExecution procedureExecution = c.get();
                    procedureExecution.setIsProcedurePrepared("YES");
                    procedureExecutionRepository.save(procedureExecution);
                    prepared.add(procedureExecution);

                }
            }
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            userServiceTemplate.saveAuditRails(name, company8, "PREPARE PROCEDURE", "SUCCESS: Prepared Successfully");
            genericResponseWrapper.setMessage("Procedure Prepared Successfully");
            genericResponseWrapper.setData(prepared);

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "PREPARE PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper unprepare(Long loggedInServiceProviderId, ApproveRequestIdsDao approveRequestIdsDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        List<ProcedureExecution> prepared = new ArrayList<>();
        try {
            Long[] procedureExecutionId1 = approveRequestIdsDao.getListOfIds();
            for (Long procedureExecutionId : procedureExecutionId1) {
                Optional<ProcedureExecution> c = procedureExecutionRepository.findByIdAndIntrash(procedureExecutionId, Constants.intrashNO);
                if (c.isPresent()) {
                    ProcedureExecution procedureExecution = c.get();
                    procedureExecution.setIsProcedurePrepared("NO");
                    procedureExecutionRepository.save(procedureExecution);
                    prepared.add(procedureExecution);

                }
            }
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            userServiceTemplate.saveAuditRails(name, company8, "UNPREPARE PROCEDURE", "SUCCESS: Unprepared Successfully");
            genericResponseWrapper.setMessage("Procedure UnPrepared Successfully");
            genericResponseWrapper.setData(prepared);

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UNPREPARE PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
