package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.Permissions;
import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.repository.PermisionRepository;
import com.cma.cmaproject.repository.RolesRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.RolesTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
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
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;
    @Override
    public GenericResponseWrapper createRole(Roles roles1) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            roles1.setActionStatus(Constants.actionApproved);
            roles1.setIntrash(Constants.intrashNO);
            rolesRepository.save(roles1);
            genericResponseWrapper=customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(roles1);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper viewAllRoles() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<Roles> list = rolesRepository.findByIntrash(Constants.intrashNO);
            genericResponseWrapper=customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewIndividualRole(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Roles> c=rolesRepository.findByIdAndIntrash(ordersId,Constants.intrashNO);
        try {
            if (c.isPresent()){
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(c.get());
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteRole(Long ordersId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Roles> c=rolesRepository.findByIdAndIntrash(ordersId,Constants.intrashNO);
        try {
            if (c.isPresent()){
                Roles roles=c.get();
                roles.setIntrash(Constants.intrashYES);
                rolesRepository.save(roles);
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(roles);
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
    @Override
    public GenericResponseWrapper updateRole(Long roleId, Roles role) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Roles> role1=rolesRepository.findByIdAndIntrash(roleId,Constants.intrashNO);
        try {
            if (role1.isPresent()){
                Roles roles=role1.get();
                roles.setRoleName(role.getRoleName());
                roles.setActionStatus(Constants.paymentStatus2);
                rolesRepository.save(roles);
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Verified Successfully");
                genericResponseWrapper.setData(roles);
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
    @Override
    public GenericResponseWrapper approveRole(Long roleId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Roles> c=rolesRepository.findByIdAndIntrash(roleId,Constants.intrashNO);
        try {
            if (c.isPresent()){
                Roles roles=c.get();
                roles.setActionStatus(Constants.paymentStatus2);
                rolesRepository.save(roles);
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Verified Successfully");
                genericResponseWrapper.setData(roles);
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper assignRolePermissions(Long roleId, Long[] permissionsId){
        GenericResponseWrapper genericResponseWrapper=new GenericResponseWrapper();
        try{
            Optional<Roles> role=rolesRepository.findById(roleId);
            List<Permissions> permissions1=new ArrayList<>();
            if(role.isPresent()){
                Roles roled=role.get();
                try {
                    for(Long id:permissionsId){
                        Optional<Permissions> permissions=permisionRepository.findByIdAndIntrash(id,Constants.intrashNO);
                        if(permissions.isPresent()){
                            Permissions p=permissions.get();
                            p.setRole(roled);
                            permisionRepository.save(p);
                            permissions1.add(p);
                            genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                            genericResponseWrapper.setMessage("Permissions assigned successfully");
                            genericResponseWrapper.setData(permissions1);// list of assigned permissions
                        }
                        else{
                            genericResponseWrapper=customerOrderServiceTemplate.notFound();
                            genericResponseWrapper.setMessage("Permission with ID : "+id + "NOT Found");
                        }
                    }
                }
                catch(Exception ex){
                    genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
                    ex.printStackTrace();
                    genericResponseWrapper.setMessage("System Error Occurred");
                }
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }
        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }
}
