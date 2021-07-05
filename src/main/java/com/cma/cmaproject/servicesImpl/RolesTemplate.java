package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.PermissionsIdWrapper;

public interface RolesTemplate {

    GenericResponseWrapper createRole(Long loggedServiceProviderId, GeneralRequestDao roles);

    GenericResponseWrapper viewAllRoles(Long loggedServiceProviderId);

    GenericResponseWrapper viewIndividualRole(Long loggedServiceProviderId, Long ordersId);

    GenericResponseWrapper deleteRole(Long loggedServiceProviderId, Long ordersId);

    GenericResponseWrapper updateRole(Long loggedServiceProviderId, Long roleId, Roles role);

    GenericResponseWrapper approveRole(Long loggedServiceProviderId, ApproveRequestIdsDao roleId);

    GenericResponseWrapper assignRolePermissions(Long loggedServiceProviderId, Long roleId, PermissionsIdWrapper wrapper);
}
