
/**
 * @Controller: Audit Rails Controller
 * @Aurthor: NelsonM.O
 * @Date:2021
 */

package com.cma.cmaproject.controllers;

import com.cma.cmaproject.service.AuditRailsService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/audit-rails")
public class AuditRailsController {

    @Autowired
    private AuditRailsService auditRailsService;

    @GetMapping("/view/{superAdminId}")
    public ResponseEntity<GenericResponseWrapper> viewAuditRailsSuperAdmin(@PathVariable Long superAdminId){
        GenericResponseWrapper genericResponseWrapper=auditRailsService.viewAuditLogs(superAdminId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view-all/{serviceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAuditRailsServiceProvider(@PathVariable Long serviceProviderId){
        GenericResponseWrapper genericResponseWrapper=auditRailsService.viewAllAuditLogs(serviceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
