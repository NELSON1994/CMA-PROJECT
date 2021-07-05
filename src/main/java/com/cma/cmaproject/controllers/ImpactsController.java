package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.ImpactsDao;
import com.cma.cmaproject.service.ImpactsService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/impacts")
public class ImpactsController {
    @Autowired
    private ImpactsService impactsService;

    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createImpact(@RequestBody ImpactsDao impactsDao,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=impactsService.createImpact(impactsDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllImpacts(@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=impactsService.viewAll(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{impactId}")
    public ResponseEntity<GenericResponseWrapper> viewOneImpact(@PathVariable Long impactId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=impactsService.viewIndividualImpacts(impactId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{impactId}")
    public ResponseEntity<GenericResponseWrapper> deleteImpact(@PathVariable Long impactId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=impactsService.deleteImpact(impactId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/update/{loggedServiceProviderId}/{impactId}")
    public ResponseEntity<GenericResponseWrapper> updateImpact(@PathVariable Long impactId, @RequestBody ImpactsDao impactsDao,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=impactsService.updateImpact(impactId,impactsDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/approve/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> approveImpact(@RequestBody ApproveRequestIdsDao approveRequestIdsDao, @PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=impactsService.approveImpact(approveRequestIdsDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
