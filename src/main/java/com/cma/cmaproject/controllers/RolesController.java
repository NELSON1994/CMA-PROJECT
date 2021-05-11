package com.cma.cmaproject.controllers;

import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.servicesImpl.RolesTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RolesController {
    @Autowired
    private RolesTemplate rolesTemplate;

    @PostMapping("/createRole")
    public ResponseEntity<GenericResponseWrapper> createRole(@RequestBody Roles roles){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.createRole(roles);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAllRoles")
    public ResponseEntity<GenericResponseWrapper> viewAllRoles(){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.viewAllRoles();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewRoles/{roleId}")
    public ResponseEntity<GenericResponseWrapper> viewOneRole(@PathVariable Long roleId){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.viewIndividualRole(roleId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/deleteRole/{roleId}")
    public ResponseEntity<GenericResponseWrapper> deleteRole(@PathVariable Long roleId){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.deleteRole(roleId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/updateRole/{roleId}")
    public ResponseEntity<GenericResponseWrapper> updateRole(@PathVariable Long roleId,@RequestBody Roles role){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.updateRole(roleId,role);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/approveRole/{roleId}")
    public ResponseEntity<GenericResponseWrapper> verifyRole(@PathVariable Long roleId){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.approveRole(roleId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/assignRole/{roleId}/permisions/{permissionIDs}")
    public ResponseEntity<GenericResponseWrapper> assignPermissionsToRole(@PathVariable Long roleId, @PathVariable Long[] permissionIDs){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.assignRolePermissions(roleId,permissionIDs);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
