package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.Permissions;
import com.cma.cmaproject.repository.PermisionRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.PermissionTemplate;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements PermissionTemplate {

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private PermisionRepository permisionRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;
    @Override
    public GenericResponseWrapper createPermission(Long loggedServiceProviderId, GeneralRequestDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Permissions permissions=new Permissions();
        try {
            permissions.setPermission(wrapper.getName());
            permissions.setActionStatus(Constants.actionApproved);
            permissions.setIntrash(Constants.intrashNO);
            permisionRepository.save(permissions);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE PERMISSION", "SUCCESS: Created Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(permissions);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE PERMISSION", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper viewAllPermissions(Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<Permissions> list = permisionRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL PERMISSION", "SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL PERMISSION", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewIndividualPermission(Long loggedServiceProviderId, Long permissionId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Permissions> permissions = permisionRepository.findByIdAndIntrash(permissionId, Constants.intrashNO);
        try {
            if (permissions.isPresent()) {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW ONE PERMISSION", "SUCCESS: Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(permissions.get());
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW ONE PERMISSION", "FAILED: Permission with Id : " + permissionId + " NOT Found");
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Permission with Id : " + permissionId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ONE PERMISSION", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteIndividualPermission(Long loggedServiceProviderId, Long permissionId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Permissions> c = permisionRepository.findByIdAndIntrash(permissionId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Permissions permissions = c.get();
                permissions.setIntrash(Constants.intrashYES);
                permisionRepository.save(permissions);
                userServiceTemplate.saveAuditRails(name, company8, "DELETE PERMISSION", "SUCCESS: Deleted Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(permissions);
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "DELETE PERMISSION", "FAILED: Permission with Id : " + permissionId + " NOT Found");
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Permission with Id : " + permissionId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE PERMISSION", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper update(Long loggedServiceProviderId,Long Id, GeneralRequestDao wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Permissions> p=permisionRepository.findByIdAndIntrash(Id,Constants.intrashNO);
        try {
            if (p.isPresent()){
                Permissions permissions=p.get();
                permissions.setPermission(wrapper.getName());
                permissions.setActionStatus(Constants.actionApproved);
                permisionRepository.save(permissions);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PERMISSION", "SUCCESS: Updated Successful");
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(permissions);
            }
            else{
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE PERMISSION", "FAILED: Permission with Id : " + Id + " NOT Found");
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Permission with Id : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE PERMISSION", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
