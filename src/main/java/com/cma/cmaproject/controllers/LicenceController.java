package com.cma.cmaproject.controllers;

import com.cma.cmaproject.model.Licence;
import com.cma.cmaproject.servicesImpl.LincenceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/licence")
public class LicenceController {
    @Autowired
    private LincenceTemplate lincenceTemplate;

    @PostMapping("/generate/{orderId}")
    public ResponseEntity<GenericResponseWrapper> generateLicence(@RequestBody Licence licence,@PathVariable Long orderId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.generateLincense(licence,orderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponseWrapper> viewAll(){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.viewAllLicences();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewLicence/{licenceId}")
    public ResponseEntity<GenericResponseWrapper> viewOneLicence(@PathVariable Long licenceId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.viewIndividualLicence(licenceId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/renew/{licenceId}")
    public ResponseEntity<GenericResponseWrapper> renewLicence(@PathVariable Long licenceId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.renewLicence(licenceId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/activate/{userId}/licence/{licenceIds}")
    public ResponseEntity<GenericResponseWrapper> activateLicence(@PathVariable Long userId,@PathVariable Long[] licenceIds){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.activateLicence(userId,licenceIds);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/deactivate/{licenceId}")
    public ResponseEntity<GenericResponseWrapper> deactivateLicence(@PathVariable Long licenceId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.deactivateLicence(licenceId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
