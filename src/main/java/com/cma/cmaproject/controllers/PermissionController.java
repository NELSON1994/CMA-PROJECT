package com.cma.cmaproject.controllers;

import com.cma.cmaproject.servicesImpl.PermissionTemplate;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private PermissionTemplate permissionTemplate;
    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createPermission(@PathVariable Long loggedServiceProviderId,@RequestBody GeneralRequestDao permissions){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.createPermission(loggedServiceProviderId,permissions);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllPermission(@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.viewAllPermissions(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewOne/{loggedServiceProviderId}/{permissionId}")
    public ResponseEntity<GenericResponseWrapper> viewOnePermission(@PathVariable Long loggedServiceProviderId,@PathVariable Long permissionId){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.viewIndividualPermission(loggedServiceProviderId,permissionId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{permissionId}")
    public ResponseEntity<GenericResponseWrapper> deletePermission(@PathVariable Long loggedServiceProviderId,@PathVariable Long permissionId){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.deleteIndividualPermission(loggedServiceProviderId,permissionId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{loggedServiceProviderId}/{permissionId}")
    public ResponseEntity<GenericResponseWrapper> updatePermission(@PathVariable Long loggedServiceProviderId,@PathVariable Long permissionId,@RequestBody GeneralRequestDao permissions){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.update(loggedServiceProviderId,permissionId,permissions);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
