package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.service.ProcedureExecutionAttributesService;
import com.cma.cmaproject.service.ProcedureExecutionService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/procedure-execution-attribute")
public class ProcedureExecutionAttributesController {

    @Autowired
    private ProcedureExecutionAttributesService procedureExecutionAttributesService;

    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createAttribute(@RequestBody GeneralRequestDao dao, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = procedureExecutionAttributesService.create(loggedServiceProviderId, dao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllAttributes(@PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = procedureExecutionAttributesService.viewAll(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{attributeId}")
    public ResponseEntity<GenericResponseWrapper> viewOneAttribute(@PathVariable Long attributeId, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = procedureExecutionAttributesService.view(loggedServiceProviderId, attributeId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{attributeId}")
    public ResponseEntity<GenericResponseWrapper> deleteAttribute(@PathVariable Long attributeId, @PathVariable Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = procedureExecutionAttributesService.delete(loggedServiceProviderId, attributeId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{loggedServiceProviderId}/{attributeId}")
    public ResponseEntity<GenericResponseWrapper> updateAttribute(@PathVariable Long loggedServiceProviderId, @PathVariable Long attributeId, @RequestBody GeneralRequestDao dao) {
        GenericResponseWrapper genericResponseWrapper = procedureExecutionAttributesService.update(loggedServiceProviderId, attributeId, dao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
