package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;

public interface RolesTemplate {
    GenericResponseWrapper createRole(Roles roles1);

    GenericResponseWrapper viewAllRoles();

    GenericResponseWrapper viewIndividualRole(Long ordersId);

    GenericResponseWrapper deleteRole(Long ordersId);

    GenericResponseWrapper updateRole(Long roleId, Roles role);

    GenericResponseWrapper approveRole(Long roleId);

    GenericResponseWrapper assignRolePermissions(Long roleId, Long[] permissionsId);
}
