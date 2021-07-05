package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ApprovedResponseDao;
import com.cma.cmaproject.dao.ImpactsDao;
import com.cma.cmaproject.model.Impacts;
import com.cma.cmaproject.repository.ImpactsRepository;
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
public class ImpactsService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private ImpactsRepository impactsRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper createImpact(ImpactsDao impactsDao, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Impacts impacts = new Impacts();
        try {
            impacts.setImpactName(impactsDao.getName());
            impacts.setRating(impactsDao.getRating());
            impacts.setCriteria(impactsDao.getCriterias());
            impacts.setActionStatus(Constants.actionUnApproved);
            impacts.setIntrash(Constants.intrashNO);
            impactsRepository.save(impacts);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE IMPACTS", "SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(impacts);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE IMPACTS", "FAILED: System Malfunctioned");
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
            List<Impacts> list = impactsRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL IMPACTS", "SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL IMPACTS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewIndividualImpacts(Long impactsId, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Impacts> c = impactsRepository.findByIdAndIntrash(impactsId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW IMPACTS", "SUCCESS: Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(c.get());
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Impact with Id : " + impactsId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW IMPACTS", "FAILED: Impact with ID : " + impactsId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW IMPACTS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper deleteImpact(Long impactId, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Impacts> c = impactsRepository.findByIdAndIntrash(impactId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Impacts impacts = c.get();
                impacts.setIntrash(Constants.intrashYES);
                impactsRepository.save(impacts);
                userServiceTemplate.saveAuditRails(name, company8, "DELETE IMPACTS", "SUCCESS: Deleted Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(impacts);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Impact with Id : " + impactId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE IMPACTS", "FAILED: Impact with ID : " + impactId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE IMPACTS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper updateImpact(Long impactId, ImpactsDao impactsDao, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Impacts> impacts = impactsRepository.findByIdAndIntrash(impactId, Constants.intrashNO);
        try {
            if (impacts.isPresent()) {
                Impacts impacts1 = impacts.get();
                impacts1.setCriteria(impactsDao.getCriterias());
                impacts1.setRating(impactsDao.getRating());
                impacts1.setImpactName(impactsDao.getName());
                impacts1.setActionStatus(Constants.actionUnApproved);
                impactsRepository.save(impacts1);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE IMPACTS", "SUCCESS: Updated Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(impacts1);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Impact with Id : " + impactId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE IMPACTS", "FAILED: Impact with ID : " + impactId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE IMPACTS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper approveImpact(ApproveRequestIdsDao approveRequestIdsDao, Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        ApprovedResponseDao approvedResponseDao=new ApprovedResponseDao();
        List<Long> app=new ArrayList<>();
        List<Long> unapp=new ArrayList<>();
        Long[] impactIds=approveRequestIdsDao.getListOfIds();
        try {
            for(Long  impactId: impactIds){
                Optional<Impacts> c = impactsRepository.findByIdAndIntrash(impactId, Constants.intrashNO);
                if (c.isPresent()) {
                    Impacts impacts = c.get();
                    impacts.setActionStatus(Constants.actionApproved);
                    impactsRepository.save(impacts);
                    app.add(impactId);
                } else {
                    unapp.add(impactId);
                }
            }
            approvedResponseDao.setApprovedIds(app);
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE IMPACTS", "SUCCESS: Successfull");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("Approved Successfully");
            genericResponseWrapper.setData(approvedResponseDao);

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE IMPACTS", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
