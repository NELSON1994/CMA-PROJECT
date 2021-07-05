package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.OveralRiskDescriptDao;
import com.cma.cmaproject.service.OveralRiskDescriptService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/overal-risk-description")
public class OveralRiskDescriptionController {
    @Autowired
    private OveralRiskDescriptService overalRiskDescriptService;
    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createOveralRiskDescription(@RequestBody OveralRiskDescriptDao overalRiskDescriptDao,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=overalRiskDescriptService.create(overalRiskDescriptDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllOveralRiskDescription(@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=overalRiskDescriptService.viewAll(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{overalRiskId}")
    public ResponseEntity<GenericResponseWrapper> viewOneOveralRiskDescription(@PathVariable Long overalRiskId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=overalRiskDescriptService.viewIndividual(overalRiskId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{overalRiskId}")
    public ResponseEntity<GenericResponseWrapper> deleteOveralRiskDescription(@PathVariable Long overalRiskId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=overalRiskDescriptService.delete(overalRiskId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/update/{loggedServiceProviderId}/{overalRiskId}")
    public ResponseEntity<GenericResponseWrapper> updateOveralRiskDescription(@PathVariable Long overalRiskId,@RequestBody OveralRiskDescriptDao overalRiskDescriptDao,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=overalRiskDescriptService.update(overalRiskId,overalRiskDescriptDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/approve/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> approveOveralRiskDescription(@RequestBody ApproveRequestIdsDao approveRequestIdsDao, @PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=overalRiskDescriptService.approve(approveRequestIdsDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
