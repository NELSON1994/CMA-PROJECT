package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.model.Permissions;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;

public interface PermissionTemplate {
    GenericResponseWrapper createPermission(Permissions permissions);

    GenericResponseWrapper viewAllPermissions();

    GenericResponseWrapper viewIndividualPermission(Long permissionId);

    GenericResponseWrapper deleteIndividualPermission(Long permissionId);
}
