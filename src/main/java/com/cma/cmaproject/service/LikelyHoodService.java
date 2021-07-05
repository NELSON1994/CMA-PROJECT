package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ApprovedResponseDao;
import com.cma.cmaproject.dao.LikelyHoodDao;
import com.cma.cmaproject.model.Likelyhood;
import com.cma.cmaproject.repository.LikelyHoodRepository;
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
public class LikelyHoodService {
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;
    @Autowired
    private LikelyHoodRepository likelyHoodRepository;
    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper create(LikelyHoodDao likelyHoodDao,Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Likelyhood likelyhood = new Likelyhood();
        try {
            likelyhood.setLikelyhood(likelyHoodDao.getLikelyhood());
            likelyhood.setProbability(likelyHoodDao.getProbability());
            likelyhood.setRating(likelyHoodDao.getRating());
            likelyhood.setActionStatus(Constants.actionUnApproved);
            likelyhood.setIntrash(Constants.intrashNO);
            likelyHoodRepository.save(likelyhood);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE LIKELYHOOD", "SUCCESS: Created Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(likelyhood);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE LIKELYHOOD", "FAILED: System Malfunctioned");
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
            List<Likelyhood> list = likelyHoodRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL LIKELYHOOD", "SUCCESS: Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL LIKELYHOOD", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewIndividual(Long lkId,Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Likelyhood> c = likelyHoodRepository.findByIdAndIntrash(lkId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW LIKELYHOOD", "SUCCESS: Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(c.get());
            } else {
                genericResponseWrapper.setMessage("Likelyhood with Id : " + lkId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW LIKELYHOOD", "FAILED: Likelyhood with ID : " + lkId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW LIKELYHOOD", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper delete(Long Id,Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Likelyhood> c = likelyHoodRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Likelyhood likelyhood = c.get();
                likelyhood.setIntrash(Constants.intrashYES);
                likelyHoodRepository.save(likelyhood);
                userServiceTemplate.saveAuditRails(name, company8, "DELETE LIKELYHOOD", "SUCCESS: Deleted Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(likelyhood);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Likelyhood with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE LIKELYHOOD", "FAILED: Likelyhood with ID : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE LIKELYHOOD", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long Id, LikelyHoodDao likelyHoodDao,Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Likelyhood> c = likelyHoodRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Likelyhood likelyhood = c.get();
                likelyhood.setLikelyhood(likelyHoodDao.getLikelyhood());
                likelyhood.setProbability(likelyHoodDao.getProbability());
                likelyhood.setRating(likelyHoodDao.getRating());
                likelyhood.setActionStatus(Constants.actionUnApproved);
                likelyHoodRepository.save(likelyhood);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE LIKELYHOOD", "SUCCESS: Updated Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(likelyhood);
            } else {
                genericResponseWrapper.setMessage("Likelyhood with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE LIKELYHOOD", "FAILED: Likelyhood with ID : " + Id + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE LIKELYHOOD", "FAILED: System Malfunctioned");
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
        ApprovedResponseDao approvedResponseDao=new ApprovedResponseDao();
        List<Long> app=new ArrayList<>();
        List<Long> unapp=new ArrayList<>();
        Long[] lksIds=approveRequestIdsDao.getListOfIds();
        try {

            for (Long lhId: lksIds){
                Optional<Likelyhood> c = likelyHoodRepository.findByIdAndIntrash(lhId, Constants.intrashNO);
                if (c.isPresent()) {
                    Likelyhood likelyhood = c.get();
                    likelyhood.setActionStatus(Constants.actionApproved);
                    likelyHoodRepository.save(likelyhood);
                    app.add(lhId);
                } else {
               unapp.add(lhId);
                }
            }
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            approvedResponseDao.setApprovedIds(app);
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE LIKELYHOOD", "SUCCESS: Approved Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("Approved Successfully");
            genericResponseWrapper.setData(approvedResponseDao);

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE LIKELYHOOD", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
