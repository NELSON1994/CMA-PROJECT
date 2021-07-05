package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.RiskMatrixDao;
import com.cma.cmaproject.service.RiskMatrixService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/risk-matrix")
public class RiskMatrixController {

    @Autowired
    private RiskMatrixService riskMatrixService;

    @PostMapping("/create/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> createRiskMatrix(@RequestBody RiskMatrixDao riskMatrixDao, @PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskMatrixService.create(riskMatrixDao, loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> viewAllRiskMatrix(@PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskMatrixService.viewAll(loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedInUserId}/{riskMatrixID}")
    public ResponseEntity<GenericResponseWrapper> viewOneRiskMatrix(@PathVariable Long riskMatrixID, @PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskMatrixService.viewIndividual(riskMatrixID, loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedInUserId}/{riskMatrixID}")
    public ResponseEntity<GenericResponseWrapper> deleteRiskMatrix(@PathVariable Long riskMatrixID, @PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskMatrixService.delete(riskMatrixID, loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{loggedInUserId}/{riskMatrixID}")
    public ResponseEntity<GenericResponseWrapper> updateRiskMatrix(@PathVariable Long riskMatrixID, @RequestBody RiskMatrixDao riskMatrixDao, @PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskMatrixService.update(riskMatrixID, riskMatrixDao, loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/approve/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> approveRiskMatrix(@RequestBody ApproveRequestIdsDao approveRequestIdsDao, @PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskMatrixService.approve(approveRequestIdsDao, loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
