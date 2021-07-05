package com.cma.cmaproject.controllers;


import com.cma.cmaproject.dao.ProcedureAssessmentDao;
import com.cma.cmaproject.service.ProcedureAssessmentService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/procedure-assessment")
public class ProcedureAssessmentController {
    @Autowired
    private ProcedureAssessmentService procedureAssessmentService;

    @PostMapping("/create/{loggedUserId}")
    public ResponseEntity<GenericResponseWrapper> createProcedureAssessment(@RequestBody ProcedureAssessmentDao dao, @PathVariable Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = procedureAssessmentService.create(loggedUserId, dao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedUserId}")
    public ResponseEntity<GenericResponseWrapper> viewAllProcedureAssessment(@PathVariable Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = procedureAssessmentService.viewAll(loggedUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedUserId}/{procedureAssessmentId}")
    public ResponseEntity<GenericResponseWrapper> viewOneProcedureAssessment(@PathVariable Long procedureAssessmentId, @PathVariable Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = procedureAssessmentService.view(loggedUserId, procedureAssessmentId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedUserId}/{procedureAssessmentId}")
    public ResponseEntity<GenericResponseWrapper> deleteProcedureAssessment(@PathVariable Long procedureAssessmentId, @PathVariable Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = procedureAssessmentService.delete(loggedUserId, procedureAssessmentId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{loggedUserId}/{procedureAssessmentId}")
    public ResponseEntity<GenericResponseWrapper> updateProcedureAssessment(@PathVariable Long loggedUserId, @PathVariable Long procedureAssessmentId, @RequestBody ProcedureAssessmentDao dao) {
        GenericResponseWrapper genericResponseWrapper = procedureAssessmentService.update(loggedUserId, procedureAssessmentId, dao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
