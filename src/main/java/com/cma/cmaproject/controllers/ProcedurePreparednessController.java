package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.service.ProcedurePreparationService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/procedure-execution")
public class ProcedurePreparednessController {
    @Autowired
    private ProcedurePreparationService procedurePreparationService;

    @PutMapping("/prepare/{inputterId}")
    public ResponseEntity<GenericResponseWrapper> prepareProcedure(@PathVariable Long inputterId, @RequestBody ApproveRequestIdsDao approveRequestIdsDao){
        GenericResponseWrapper genericResponseWrapper=procedurePreparationService.prepare(inputterId,approveRequestIdsDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/unprepare/{inputterId}")
    public ResponseEntity<GenericResponseWrapper> unprepareProcedure(@PathVariable Long inputterId, @RequestBody ApproveRequestIdsDao approveRequestIdsDao){
        GenericResponseWrapper genericResponseWrapper=procedurePreparationService.unprepare(inputterId,approveRequestIdsDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }


}
