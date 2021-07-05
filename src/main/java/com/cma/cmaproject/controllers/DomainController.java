package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.DomainDao;
import com.cma.cmaproject.service.DomainService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/domain")
public class DomainController {
    @Autowired
    private DomainService domainService;

    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createDomain(@RequestBody DomainDao domainDao, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = domainService.create(loggedServiceProviderId, domainDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllDomains(@PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = domainService.viewAll(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{domainId}")
    public ResponseEntity<GenericResponseWrapper> viewOneDomain(@PathVariable Long domainId, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = domainService.view(loggedServiceProviderId, domainId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{domainId}")
    public ResponseEntity<GenericResponseWrapper> deleteDomain(@PathVariable Long domainId, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = domainService.view(loggedServiceProviderId, domainId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{loggedServiceProviderId}/{domainId}")
    public ResponseEntity<GenericResponseWrapper> updateDomain(@PathVariable Long loggedServiceProviderId, @PathVariable Long domainId, @RequestBody DomainDao domainDao) {
        GenericResponseWrapper genericResponseWrapper = domainService.update(loggedServiceProviderId, domainId, domainDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view-linked-controls-to-domain/{loggedServiceProviderId}/{domainId}")
    public ResponseEntity<GenericResponseWrapper> viewLinkedControlsToDomain(@PathVariable Long loggedServiceProviderId,@PathVariable Long domainId) {
        GenericResponseWrapper genericResponseWrapper = domainService.viewLinked(loggedServiceProviderId, domainId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
