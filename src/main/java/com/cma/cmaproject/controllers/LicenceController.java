package com.cma.cmaproject.controllers;

import com.cma.cmaproject.servicesImpl.LincenceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.dao.LicenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/licence")
public class LicenceController {
    @Autowired
    private LincenceTemplate lincenceTemplate;

    @PostMapping("/generate/{loggedServiceProviderId}/{clientId}")
    public ResponseEntity<GenericResponseWrapper> generateLicence(@RequestBody LicenceDao licence, @PathVariable Long clientId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.generateLincense(licence,clientId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllLicence(@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.viewAllLicences(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{licenceId}")
    public ResponseEntity<GenericResponseWrapper> viewOneLicence(@PathVariable Long licenceId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.viewIndividualLicence(licenceId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/renew/{loggedServiceProviderId}/{clientId}")
    public ResponseEntity<GenericResponseWrapper> renewLicence(@PathVariable Long clientId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.renewLicence(clientId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/activate/{superAdminId}")
    public ResponseEntity<GenericResponseWrapper> activateLicence(@PathVariable Long superAdminId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.activateLicence(superAdminId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/deactivate/{loggedServiceProviderId}/{clientId}")
    public ResponseEntity<GenericResponseWrapper> deactivateLicence(@PathVariable Long clientId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=lincenceTemplate.deactivateLicence(clientId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
