package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ApprovedResponseDao;
import com.cma.cmaproject.dao.OveralRiskDescriptDao;
import com.cma.cmaproject.model.OveralRiskDescription;
import com.cma.cmaproject.repository.OveralRiskDescripRepository;
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
public class OveralRiskDescriptService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;
    @Autowired
    private OveralRiskDescripRepository overalRiskDescripRepository;
    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper create(OveralRiskDescriptDao overalRiskDescriptDao, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        OveralRiskDescription overalRiskDescription = new OveralRiskDescription();
        try {
            overalRiskDescription.setRiskName(overalRiskDescriptDao.getRiskName());
            overalRiskDescription.setRiskDescription(overalRiskDescriptDao.getRiskDescription());
            overalRiskDescription.setActionStatus(Constants.actionUnApproved);
            overalRiskDescription.setIntrash(Constants.intrashNO);
            overalRiskDescripRepository.save(overalRiskDescription);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE OVERAL RISK DESCRIPTION", "SUCCESS: Created Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(overalRiskDescription);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE OVERAL RISK DESCRIPTION", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper viewAll(Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<OveralRiskDescription> list = overalRiskDescripRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL OVERAL RISK DESCRIPTION", "SUCCESS: Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL OVERAL RISK DESCRIPTION", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewIndividual(Long lkId, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<OveralRiskDescription> c = overalRiskDescripRepository.findByIdAndIntrash(lkId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW OVERAL RISK DESCRIPTION", "SUCCESS: Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(c.get());
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW OVERAL RISK DESCRIPTION", "FAILED: Risk with ID : " + lkId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Risk with Id : " + lkId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW OVERAL RISK DESCRIPTION", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper delete(Long Id, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<OveralRiskDescription> c = overalRiskDescripRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                OveralRiskDescription overalRiskDescription = c.get();
                overalRiskDescription.setIntrash(Constants.intrashYES);
                overalRiskDescripRepository.save(overalRiskDescription);
                userServiceTemplate.saveAuditRails(name, company8, "DELETE OVERAL RISK DESCRIPTION", "SUCCESS: Deleted Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Successfully");
                genericResponseWrapper.setData(overalRiskDescription);
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "DELETE OVERAL RISK DESCRIPTION", "FAILED: Risk with ID : " + Id + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Risk with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE OVERAL RISK DESCRIPTION", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long Id, OveralRiskDescriptDao overalRiskDescriptDao, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<OveralRiskDescription> c = overalRiskDescripRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                OveralRiskDescription overalRiskDescription = c.get();
                overalRiskDescription.setRiskName(overalRiskDescriptDao.getRiskName());
                overalRiskDescription.setRiskDescription(overalRiskDescriptDao.getRiskDescription());
                overalRiskDescription.setActionStatus(Constants.actionUnApproved);
                overalRiskDescripRepository.save(overalRiskDescription);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE OVERAL RISK DESCRIPTION", "SUCCESS: Updated Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(overalRiskDescription);
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE OVERAL RISK DESCRIPTION", "FAILED: Risk with ID : " + Id + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Risk with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE OVERAL RISK DESCRIPTION", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper approve(ApproveRequestIdsDao approveRequestIdsDao, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
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
                Optional<OveralRiskDescription> c = overalRiskDescripRepository.findByIdAndIntrash(lhId, Constants.intrashNO);
                if (c.isPresent()) {
                    OveralRiskDescription overalRiskDescription = c.get();
                    overalRiskDescription.setActionStatus(Constants.actionApproved);
                    overalRiskDescripRepository.save(overalRiskDescription);
                    app.add(lhId);
                } else {
                    unapp.add(lhId);

                }
            }
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            approvedResponseDao.setApprovedIds(app);
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE OVERAL RISK DESCRIPTION", "SUCCESS: Approved Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("Approved Successfully");
            genericResponseWrapper.setData(approvedResponseDao);

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE OVERAL RISK DESCRIPTION", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
