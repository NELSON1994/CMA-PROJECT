package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.RiskAssessmentDao;
import com.cma.cmaproject.dao.RiskReviewerDao;
import com.cma.cmaproject.service.RiskAssessmentService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/assessment")

public class RiskProfileController {
    @Autowired
    private RiskAssessmentService riskAssessmentService;

    @PostMapping("/do-assessment")
    public ResponseEntity<GenericResponseWrapper> doAssessment(@RequestBody RiskAssessmentDao riskAssessmentDao) {
        GenericResponseWrapper genericResponseWrapper = riskAssessmentService.doAssessment(riskAssessmentDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/review")
    public ResponseEntity<GenericResponseWrapper> reviewAssessment(@RequestBody RiskReviewerDao riskReviewerDao) {
        GenericResponseWrapper genericResponseWrapper = riskAssessmentService.reviewers(riskReviewerDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> viewAllAssessments(@PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskAssessmentService.viewCompanyRiskAssessment(loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedInUserId}/{riskAssementId}")
    public ResponseEntity<GenericResponseWrapper> viewOneAssessment(@PathVariable Long riskAssementId, @PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskAssessmentService.viewOneRiskAssessmentDetails(riskAssementId, loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedInUserId}/{riskAssementId}")
    public ResponseEntity<GenericResponseWrapper> deleteAssessment(@PathVariable Long riskAssementId, @PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = riskAssessmentService.deleteRiskAssessmentDetails(riskAssementId, loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update-assessment-inputter/{riskAssementId}")
    public ResponseEntity<GenericResponseWrapper> updateRiskDetailsByInputter(@PathVariable Long riskAssementId, @RequestBody RiskAssessmentDao riskAssessmentDao) {
        GenericResponseWrapper genericResponseWrapper = riskAssessmentService.updateRiskAssessmentByInputter(riskAssementId, riskAssessmentDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update-assessment-reviewer")
    public ResponseEntity<GenericResponseWrapper> updateRiskDetailsByReviewer(@RequestBody RiskReviewerDao riskReviewerDao) {
        GenericResponseWrapper genericResponseWrapper = riskAssessmentService.updateRiskAssessmentByReviewer(riskReviewerDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
