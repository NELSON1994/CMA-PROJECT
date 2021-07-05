package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.servicesImpl.RolesTemplate;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.PermissionsIdWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/roles")
public class RolesController {
    @Autowired
    private RolesTemplate rolesTemplate;

    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createRole(@PathVariable Long loggedServiceProviderId,@RequestBody GeneralRequestDao roles){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.createRole(loggedServiceProviderId,roles);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllRoles(@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.viewAllRoles(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{roleId}")
    public ResponseEntity<GenericResponseWrapper> viewOneRole(@PathVariable Long loggedServiceProviderId,@PathVariable Long roleId){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.viewIndividualRole(loggedServiceProviderId,roleId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{roleId}")
    public ResponseEntity<GenericResponseWrapper> deleteRole(@PathVariable Long loggedServiceProviderId,@PathVariable Long roleId){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.deleteRole(loggedServiceProviderId,roleId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/update/{loggedServiceProviderId}/{roleId}")
    public ResponseEntity<GenericResponseWrapper> updateRole(@PathVariable Long loggedServiceProviderId,@PathVariable Long roleId,@RequestBody Roles role){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.updateRole(loggedServiceProviderId,roleId,role);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/approve/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> verifyRole(@PathVariable Long loggedServiceProviderId, @RequestBody ApproveRequestIdsDao approveRequestIdsDao){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.approveRole(loggedServiceProviderId,approveRequestIdsDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/assign/{loggedServiceProviderId}/{roleId}/permisions")
    public ResponseEntity<GenericResponseWrapper> assignPermissionsToRole(@PathVariable Long loggedServiceProviderId, @PathVariable Long roleId, @RequestBody PermissionsIdWrapper permissionIDs){
        GenericResponseWrapper genericResponseWrapper=rolesTemplate.assignRolePermissions(loggedServiceProviderId,roleId,permissionIDs);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
