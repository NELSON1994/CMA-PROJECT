package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ProcedureExecutionDao;
import com.cma.cmaproject.service.ProcedureExecutionService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.ProcedureExecutionAttributesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/procedure-execution")
public class ProcedureExecutionController {

    @Autowired
    private ProcedureExecutionService procedureExecutionService;

    @PutMapping("/attributes-check/{inputterId}/{procedureExecutionId}")
    public ResponseEntity<GenericResponseWrapper> checkAttributes(@PathVariable Long procedureExecutionId, @PathVariable Long inputterId,@RequestBody ProcedureExecutionAttributesWrapper wrapper) {
        GenericResponseWrapper genericResponseWrapper = procedureExecutionService.attributeChecking(inputterId, procedureExecutionId,wrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PostMapping("/execute/{inputterId}/{procedureAssessmentId}/{procedureId}")
    public ResponseEntity<GenericResponseWrapper> executeProcedure(@PathVariable Long procedureId, @PathVariable Long inputterId, @PathVariable Long procedureAssessmentId,@RequestBody ProcedureExecutionDao procedureExecutionDao) {
        GenericResponseWrapper genericResponseWrapper = procedureExecutionService.executeProcedure(inputterId,procedureExecutionDao, procedureId,procedureAssessmentId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/upload-evidence/{inputterId}/{procedureExecutionId}")
    public ResponseEntity<GenericResponseWrapper> uploadEvidenceToProcedureExecution(@PathVariable Long procedureExecutionId, @PathVariable Long inputterId,@RequestParam("file") MultipartFile file) {
        GenericResponseWrapper genericResponseWrapper = procedureExecutionService.uploadEvidenceFile(inputterId,procedureExecutionId,file);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
