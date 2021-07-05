package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ApprovedResponseDao;
import com.cma.cmaproject.dao.MaturityLevelDao;
import com.cma.cmaproject.model.MaturityLevel;
import com.cma.cmaproject.repository.MaturityLevelRepository;
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
public class MaturityLevelService {
    @Autowired
    private MaturityLevelRepository maturityLevelRepository;
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper create(MaturityLevelDao maturityLevelDao, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        MaturityLevel maturityLevel = new MaturityLevel();
        try {
            maturityLevel.setName(maturityLevelDao.getName());
            maturityLevel.setDescription(maturityLevelDao.getDescription());
            maturityLevel.setOveralReadinessLevel(maturityLevelDao.getOveralReadinessLevel());
            maturityLevel.setLowerRange(maturityLevelDao.getLowerRange());
            maturityLevel.setUpperRange(maturityLevelDao.getUpperRange());
            maturityLevel.setActionStatus(Constants.actionUnApproved);
            maturityLevel.setIntrash(Constants.intrashNO);
            maturityLevelRepository.save(maturityLevel);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE MATURITY LEVEL", "SUCCESS: Created Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(maturityLevel);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE MATURITY LEVEL", "FAILED: System Malfunctioned");
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
            List<MaturityLevel> list = maturityLevelRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL MATURITY LEVEL", "SUCCESS: Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL MATURITY LEVEL", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewIndividual(Long lkId, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<MaturityLevel> c = maturityLevelRepository.findByIdAndIntrash(lkId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW MATURITY LEVEL", "SUCCESS: Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(c.get());
            } else {
                genericResponseWrapper.setMessage("Maturity Level with Id : " + lkId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW MATURITY LEVEL", "FAILED: Maturity Level with ID : " + lkId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW MATURITY LEVEL", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper delete(Long Id, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<MaturityLevel> c = maturityLevelRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                MaturityLevel maturityLevel = c.get();
                maturityLevel.setIntrash(Constants.intrashYES);
                maturityLevelRepository.save(maturityLevel);
                userServiceTemplate.saveAuditRails(name, company8, "DELETE MATURITY LEVEL", "SUCCESS: Deleted Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(maturityLevel);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Maturity Level with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE MATURITY LEVEL", "FAILED: Maturity Level with ID : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE MATURITY LEVEL", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long Id, MaturityLevelDao maturityLevelDao, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<MaturityLevel> c = maturityLevelRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                MaturityLevel maturityLevel = c.get();
                maturityLevel.setName(maturityLevelDao.getName());
                maturityLevel.setDescription(maturityLevelDao.getDescription());
                maturityLevel.setOveralReadinessLevel(maturityLevelDao.getOveralReadinessLevel());
                maturityLevel.setLowerRange(maturityLevelDao.getLowerRange());
                maturityLevel.setUpperRange(maturityLevelDao.getUpperRange());
                maturityLevel.setActionStatus(Constants.actionUnApproved);
                maturityLevelRepository.save(maturityLevel);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE  MATURITY LEVEL", "SUCCESS: Updated Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(maturityLevel);
            } else {
                genericResponseWrapper.setMessage("Maturity Level with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE  MATURITY LEVEL", "FAILED: Maturity Level with ID : " + Id + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE MATURITY LEVEL", "FAILED: System Malfunctioned");
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
        ApprovedResponseDao approvedResponseDao = new ApprovedResponseDao();
        List<Long> app = new ArrayList<>();
        List<Long> unapp = new ArrayList<>();
        Long[] lksIds = approveRequestIdsDao.getListOfIds();
        try {
            for (Long lhId : lksIds) {
                Optional<MaturityLevel> c = maturityLevelRepository.findByIdAndIntrash(lhId, Constants.intrashNO);
                if (c.isPresent()) {
                    MaturityLevel maturityLevel = c.get();
                    maturityLevel.setActionStatus(Constants.actionApproved);
                    maturityLevelRepository.save(maturityLevel);
                    app.add(lhId);
                } else {
                    unapp.add(lhId);
                }
            }
            approvedResponseDao.setApprovedIds(app);
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE  MATURITY LEVEL", "SUCCESS: Approved Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("Approved Successfully");
            genericResponseWrapper.setData(approvedResponseDao);

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE MATURITY LEVEL", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
