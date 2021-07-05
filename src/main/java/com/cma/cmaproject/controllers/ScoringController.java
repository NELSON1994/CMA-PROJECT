package com.cma.cmaproject.controllers;


import com.cma.cmaproject.service.ScoringService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/scoring")
public class ScoringController {

    @Autowired
    private ScoringService scoringService;

    @GetMapping("/controls/{loggedInUserID}")
    public ResponseEntity<GenericResponseWrapper> scoringControls(@PathVariable Long loggedInUserID) {
        GenericResponseWrapper genericResponseWrapper =scoringService.scoresForControls2(loggedInUserID);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/domains/{loggedInUserID}")
    public ResponseEntity<GenericResponseWrapper> scoringDomains(@PathVariable Long loggedInUserID) {
        GenericResponseWrapper genericResponseWrapper =scoringService.scoresForDomains2(loggedInUserID);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/graph/{loggedInUserID}")
    public ResponseEntity<GenericResponseWrapper> graphData(@PathVariable Long loggedInUserID) {
        GenericResponseWrapper genericResponseWrapper =scoringService.graphPresentation(loggedInUserID);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
