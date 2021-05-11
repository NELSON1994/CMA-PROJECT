package com.cma.cmaproject.controllers;

import com.cma.cmaproject.model.Permissions;
import com.cma.cmaproject.servicesImpl.PermissionTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private PermissionTemplate permissionTemplate;
    @PostMapping("/createPermissions")
    public ResponseEntity<GenericResponseWrapper> createPermission(@RequestBody Permissions permissions){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.createPermission(permissions);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAllPermission")
    public ResponseEntity<GenericResponseWrapper> viewAllPermission(){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.viewAllPermissions();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewOrders/{permissionId}")
    public ResponseEntity<GenericResponseWrapper> viewOnePermission(@PathVariable Long permissionId){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.viewIndividualPermission(permissionId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/deletePermission/{permissionId}")
    public ResponseEntity<GenericResponseWrapper> deletePermission(@PathVariable Long permissionId){
        GenericResponseWrapper genericResponseWrapper=permissionTemplate.deleteIndividualPermission(permissionId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
