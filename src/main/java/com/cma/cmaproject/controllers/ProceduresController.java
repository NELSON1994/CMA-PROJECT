package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ProcedureDao;
import com.cma.cmaproject.service.ProcedureService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/procedures")
public class ProceduresController {
    @Autowired
    private ProcedureService procedureService;

    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createProcedure(@RequestBody ProcedureDao procedureDao, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = procedureService.create(loggedServiceProviderId, procedureDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllProcedure(@PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = procedureService.viewAll(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{procedureId}")
    public ResponseEntity<GenericResponseWrapper> viewOneProcedure(@PathVariable Long procedureId, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = procedureService.view(loggedServiceProviderId, procedureId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{procedureId}")
    public ResponseEntity<GenericResponseWrapper> deleteProcedure(@PathVariable Long procedureId, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = procedureService.view(loggedServiceProviderId, procedureId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{loggedServiceProviderId}/{procedureId}")
    public ResponseEntity<GenericResponseWrapper> updateProcedure(@PathVariable Long loggedServiceProviderId, @PathVariable Long procedureId, @RequestBody ProcedureDao procedureDao) {
        GenericResponseWrapper genericResponseWrapper = procedureService.update(loggedServiceProviderId, procedureId, procedureDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view-linked-risks-to-procedure/{loggedServiceProviderId}/{procedureId}")
    public ResponseEntity<GenericResponseWrapper> viewLinkedRisksToProcedure(@PathVariable Long loggedServiceProviderId,@PathVariable Long procedureId) {
        GenericResponseWrapper genericResponseWrapper = procedureService.viewRisksLinkedToProcedure(loggedServiceProviderId, procedureId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
