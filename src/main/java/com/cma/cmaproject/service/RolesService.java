package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ApprovedResponseDao;
import com.cma.cmaproject.model.Permissions;
import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.repository.PermisionRepository;
import com.cma.cmaproject.repository.RolesRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.RolesTemplate;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.PermissionsIdWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RolesService implements RolesTemplate {
    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PermisionRepository permisionRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Override
    public GenericResponseWrapper createRole(Long loggedServiceProviderId, GeneralRequestDao roles) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Roles roles1 = new Roles();
        try {
            roles1.setRoleName(roles.getName());
            roles1.setActionStatus(Constants.actionApproved);
            roles1.setIntrash(Constants.intrashNO);
            rolesRepository.save(roles1);
            userServiceTemplate.saveAuditRails(name, company8, "CREATE ROLE", "SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(roles1);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "CREATE ROLE", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper viewAllRoles(Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<Roles> list = rolesRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL ROLE", "SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL ROLE", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewIndividualRole(Long loggedServiceProviderId, Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Roles> c = rolesRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW ONE ROLE", "SUCCESS: Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(c.get());
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                userServiceTemplate.saveAuditRails(name, company8, "VIEW ONE ROLE", "FAILED: Role with Id : " + ordersId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ONE ROLE", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteRole(Long loggedServiceProviderId, Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Roles> c = rolesRepository.findByIdAndIntrash(ordersId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Roles roles = c.get();
                roles.setIntrash(Constants.intrashYES);
                rolesRepository.save(roles);
                userServiceTemplate.saveAuditRails(name, company8, "DELETE ROLE", "SUCCESS: Deleted Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(roles);
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "DELETE ROLE", "FAILED: Role with Id : " + ordersId + " NOT Found");
                genericResponseWrapper.setMessage("Role with Id : " + ordersId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE ROLE", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper updateRole(Long loggedServiceProviderId, Long roleId, Roles role) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Roles> role1 = rolesRepository.findByIdAndIntrash(roleId, Constants.intrashNO);
        try {
            if (role1.isPresent()) {
                Roles roles = role1.get();
                roles.setRoleName(role.getRoleName());
                roles.setActionStatus(Constants.paymentStatus2);
                rolesRepository.save(roles);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE ROLE", "SUCCESS: Updated Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(roles);
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE ROLE", "FAILED: Role with Id : " + roleId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Role with Id : " + roleId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE ROLE", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper approveRole(Long loggedServiceProviderId, ApproveRequestIdsDao roleId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        ApprovedResponseDao approvedResponseDao = new ApprovedResponseDao();
        List<Long> app = new ArrayList<>();
        List<Long> unapp = new ArrayList<>();
        Long[] lksIds = roleId.getListOfIds();

        try {
            for (Long roleId1 : lksIds) {
                Optional<Roles> c = rolesRepository.findByIdAndIntrash(roleId1, Constants.intrashNO);
                if (c.isPresent()) {
                    Roles roles = c.get();
                    roles.setActionStatus(Constants.paymentStatus2);
                    rolesRepository.save(roles);
                    app.add(roleId1);
                } else {
                    unapp.add(roleId1);
                }
            }
            approvedResponseDao.setApprovedIds(app);
            approvedResponseDao.setFailedToBeApprovedIds(unapp);
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE ROLE", "SUCCESS: Approved Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("Approved Successfully");
            genericResponseWrapper.setData(approvedResponseDao);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "APPROVE ROLE", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper assignRolePermissions(Long loggedServiceProviderId, Long roleId, PermissionsIdWrapper wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Long[] permissionsId = wrapper.getPermissionIDs();
        try {
            Optional<Roles> role = rolesRepository.findById(roleId);
            List<Permissions> permissions1 = new ArrayList<>();
            if (role.isPresent()) {
                Roles roled = role.get();
                try {
                    for (Long id : permissionsId) {
                        Optional<Permissions> permissions = permisionRepository.findByIdAndIntrash(id, Constants.intrashNO);
                        if (permissions.isPresent()) {
                            Permissions p = permissions.get();
                            p.setRole(roled);
                            permisionRepository.save(p);
                            permissions1.add(p);
                            userServiceTemplate.saveAuditRails(name, company8, "PERMISSIONS TO ROLE", "SUCCESS: Assigned Successful");
                            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                            genericResponseWrapper.setMessage("Permissions assigned successfully");
                            genericResponseWrapper.setData(permissions1);// list of assigned permissions
                        } else {
                            genericResponseWrapper = customerOrderServiceTemplate.notFound();
                            genericResponseWrapper.setMessage("Permission with ID : " + id + "NOT Found");
                        }
                    }
                } catch (Exception ex) {
                    userServiceTemplate.saveAuditRails(name, company8, "ASSIGN PREMISSIONS TO ROLE", "FAILED: System Error");
                    genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
                    ex.printStackTrace();
                    genericResponseWrapper.setMessage("System Error Occurred");
                }
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "ASSIGN PERMISSIONS TO ROLE", "FAILED: Role with Id : " + roleId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Role with Id : " + roleId + " NOT Found");
                genericResponseWrapper.setData(null);
            }
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "ASSIGN PREMISSIONS TO ROLE", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }
}
