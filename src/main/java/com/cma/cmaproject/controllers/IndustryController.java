package com.cma.cmaproject.controllers;

import com.cma.cmaproject.service.IndustryService;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/industry")
public class IndustryController {

    @Autowired
    private IndustryService industryService;

    @PostMapping("/create")
    public ResponseEntity<GenericResponseWrapper> createIndustry(@RequestBody GeneralRequestDao wrapper){
        GenericResponseWrapper genericResponseWrapper=industryService.createIndustry(wrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponseWrapper> viewAllIndustry(){
        GenericResponseWrapper genericResponseWrapper=industryService.viewAllIndustry();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewOne/{industryId}")
    public ResponseEntity<GenericResponseWrapper> viewOneIndustry(@PathVariable Long industryId){
        GenericResponseWrapper genericResponseWrapper=industryService.viewIndustry(industryId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{industryId}")
    public ResponseEntity<GenericResponseWrapper> deleteIndustry(@PathVariable Long industryId){
        GenericResponseWrapper genericResponseWrapper=industryService.deleteIndustry(industryId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
