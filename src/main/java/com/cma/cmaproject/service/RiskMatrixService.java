package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ApprovedResponseDao;
import com.cma.cmaproject.dao.RiskMatrixDao;
import com.cma.cmaproject.model.RiskMatrix;
import com.cma.cmaproject.repository.RiskMatrixRepository;
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
public class RiskMatrixService {
    @Autowired
    private RiskMatrixRepository riskMatrixRepository;
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper create(RiskMatrixDao riskMatrixDao, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        RiskMatrix riskMatrix=new RiskMatrix();
        try {
            riskMatrix.setRiskMatrixName(riskMatrixDao.getRiskMatrixName());
            riskMatrix.setRiskLevelLowLimit(riskMatrixDao.getRiskLevelLowLimit());
            riskMatrix.setRiskLevelUpperLimit(riskMatrixDao.getRiskLevelUpperLimit());
           riskMatrix.setActionStatus(Constants.actionUnApproved);
            riskMatrix.setIntrash(Constants.intrashNO);
            riskMatrixRepository.save(riskMatrix);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE RISK MATRIX", "SUCCESS: Created Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(riskMatrix);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE RISK MATRIX", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper viewAll(Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<RiskMatrix> list = riskMatrixRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL RISK MATRIX", "SUCCESS: Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL RISK MATRIX", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewIndividual(Long lkId,Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<RiskMatrix> c = riskMatrixRepository.findByIdAndIntrash(lkId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW RISK MATRIX", "SUCCESS: Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(c.get());
            } else {
                genericResponseWrapper.setMessage("Risk Matrix with Id : " + lkId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW RISK MATRIX", "FAILED: Risk Matrix with ID : " + lkId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW RISK MATRIX", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper delete(Long Id,Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<RiskMatrix> c = riskMatrixRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                RiskMatrix riskMatrix = c.get();
                riskMatrix.setIntrash(Constants.intrashYES);
                riskMatrixRepository.save(riskMatrix);
                userServiceTemplate.saveAuditRails(name, company8, "DELETE RISK MATRIX", "SUCCESS: Deleted Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(riskMatrix);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Risk Matrix with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE RISK MATRIX", "FAILED: Risk Matrix with ID : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE RISK MATRIX", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long Id, RiskMatrixDao riskMatrixDao, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<RiskMatrix> c = riskMatrixRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                RiskMatrix riskMatrix = c.get();
                riskMatrix.setRiskMatrixName(riskMatrixDao.getRiskMatrixName());
                riskMatrix.setRiskLevelLowLimit(riskMatrixDao.getRiskLevelLowLimit());
                riskMatrix.setRiskLevelUpperLimit(riskMatrixDao.getRiskLevelUpperLimit());
                riskMatrix.setActionStatus(Constants.actionUnApproved);
                riskMatrixRepository.save(riskMatrix);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK MATRIX", "SUCCESS: Updated Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(riskMatrix);
            } else {
                genericResponseWrapper.setMessage("Risk Matrix with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK MATRIX", "FAILED: Risk Matrix with ID : " + Id + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK MATRIX", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper approve(ApproveRequestIdsDao approveRequestIdsDao, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        ApprovedResponseDao approvedResponseDao=new ApprovedResponseDao();
        List<Long> app=new ArrayList<>();
        List<Long> unapp=new ArrayList<>();
        Long[] lksIds=approveRequestIdsDao.getListOfIds();
        try {
            for(Long lhId:lksIds){
                Optional<RiskMatrix> c = riskMatrixRepository.findByIdAndIntrash(lhId, Constants.intrashNO);
                if (c.isPresent()) {
                    RiskMatrix riskMatrix = c.get();
                    riskMatrix.setActionStatus(Constants.actionApproved);
                    riskMatrixRepository.save(riskMatrix);
                    app.add(lhId);
                } else {
                   unapp.add(lhId);
                }
            }
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            approvedResponseDao.setApprovedIds(app);
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE  RISK MATRIX", "SUCCESS: Approved Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("Approved Successfully");
            genericResponseWrapper.setData(approvedResponseDao);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE RISK MATRIX", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
