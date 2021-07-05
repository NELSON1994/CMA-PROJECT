package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ControlDao;
import com.cma.cmaproject.service.ControlService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/control")
public class ControlsController {
    @Autowired
    private ControlService controlService;

    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createControl(@RequestBody ControlDao controlDao, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = controlService.create(loggedServiceProviderId, controlDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllControls(@PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = controlService.viewAll(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{controlId}")
    public ResponseEntity<GenericResponseWrapper> viewOneControl(@PathVariable Long controlId, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = controlService.view(loggedServiceProviderId, controlId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{controlId}")
    public ResponseEntity<GenericResponseWrapper> deleteControl(@PathVariable Long controlId, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = controlService.view(loggedServiceProviderId, controlId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{loggedServiceProviderId}/{controlId}")
    public ResponseEntity<GenericResponseWrapper> updateControl(@PathVariable Long loggedServiceProviderId, @PathVariable Long controlId, @RequestBody ControlDao controlDao) {
        GenericResponseWrapper genericResponseWrapper = controlService.update(loggedServiceProviderId, controlId, controlDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view-linked-procedures-to-control/{loggedServiceProviderId}/{controlId}")
    public ResponseEntity<GenericResponseWrapper> viewLinkedProceduresToControl(@PathVariable Long loggedServiceProviderId,@PathVariable Long controlId) {
        GenericResponseWrapper genericResponseWrapper = controlService.viewLinked(loggedServiceProviderId,controlId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
