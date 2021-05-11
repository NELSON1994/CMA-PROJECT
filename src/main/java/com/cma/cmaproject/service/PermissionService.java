package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.Permissions;
import com.cma.cmaproject.repository.PermisionRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.PermissionTemplate;
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

    @Override
    public GenericResponseWrapper createPermission(Permissions permissions) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            permissions.setActionStatus(Constants.actionApproved);
            permissions.setIntrash(Constants.intrashNO);
            permisionRepository.save(permissions);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(permissions);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper viewAllPermissions() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<Permissions> list = permisionRepository.findByIntrash(Constants.intrashNO);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewIndividualPermission(Long permissionId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Permissions> permissions = permisionRepository.findByIdAndIntrash(permissionId, Constants.intrashNO);
        try {
            if (permissions.isPresent()) {
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(permissions.get());
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteIndividualPermission(Long permissionId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Permissions> c = permisionRepository.findByIdAndIntrash(permissionId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                Permissions permissions = c.get();
                permissions.setIntrash(Constants.intrashYES);
                permisionRepository.save(permissions);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(permissions);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
