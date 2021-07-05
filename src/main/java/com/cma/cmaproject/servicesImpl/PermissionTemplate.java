package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;

public interface PermissionTemplate {

    GenericResponseWrapper createPermission(Long loggedServiceProviderId, GeneralRequestDao wrapper);

    GenericResponseWrapper viewAllPermissions(Long loggedServiceProviderId);

    GenericResponseWrapper viewIndividualPermission(Long loggedServiceProviderId, Long permissionId);

    GenericResponseWrapper deleteIndividualPermission(Long loggedServiceProviderId, Long permissionId);

    GenericResponseWrapper update(Long loggedServiceProviderId, Long Id, GeneralRequestDao wrapper);
}
