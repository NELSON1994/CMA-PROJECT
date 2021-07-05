package com.cma.cmaproject.controllers;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.MaturityLevelDao;
import com.cma.cmaproject.model.MaturityLevel;
import com.cma.cmaproject.repository.MaturityLevelRepository;
import com.cma.cmaproject.service.MaturityLevelService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/maturity-level")
public class MaturityLevelController {

    @Autowired
    private MaturityLevelRepository maturityLevelRepository;

    @Autowired
    private MaturityLevelService maturityLevelService;

    @PostMapping("/create/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> createMaturityLevel(@RequestBody MaturityLevelDao maturityLevelDao, @PathVariable Long loggedInUserId){
        GenericResponseWrapper genericResponseWrapper=maturityLevelService.create(maturityLevelDao,loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> viewAllMaturityLevel(@PathVariable Long loggedInUserId){
        GenericResponseWrapper genericResponseWrapper=maturityLevelService.viewAll(loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedInUserId}/{maturityLevelID}")
    public ResponseEntity<GenericResponseWrapper> viewOneMaturityLevel(@PathVariable Long maturityLevelID,@PathVariable Long loggedInUserId){
        GenericResponseWrapper genericResponseWrapper=maturityLevelService.viewIndividual(maturityLevelID,loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedInUserId}/{maturityLevelID}")
    public ResponseEntity<GenericResponseWrapper> deleteMaturityLevel(@PathVariable Long maturityLevelID,@PathVariable Long loggedInUserId){
        GenericResponseWrapper genericResponseWrapper=maturityLevelService.delete(maturityLevelID,loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/update/{loggedInUserId}/{maturityLevelID}")
    public ResponseEntity<GenericResponseWrapper> updateMaturityLevel(@PathVariable Long maturityLevelID,@RequestBody MaturityLevelDao maturityLevelDao,@PathVariable Long loggedInUserId){
        GenericResponseWrapper genericResponseWrapper=maturityLevelService.update(maturityLevelID,maturityLevelDao,loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/approve/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> approveMaturityLevel(@RequestBody ApproveRequestIdsDao approveRequestIdsDao, @PathVariable Long loggedInUserId){
        GenericResponseWrapper genericResponseWrapper=maturityLevelService.approve(approveRequestIdsDao,loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    //for demo

  //  @GetMapping("/demo/{score}")
    public ResponseEntity<GenericResponseWrapper> demo(@PathVariable int score){
        GenericResponseWrapper genericResponseWrapper=new GenericResponseWrapper();
        List<MaturityLevel> levelList=maturityLevelRepository.findByIntrashAndUpperRangeGreaterThanEqual(Constants.intrashNO,score);
        genericResponseWrapper.setCode(200);
        genericResponseWrapper.setMessage("Done");
        genericResponseWrapper.setStatus("Done");
        genericResponseWrapper.setData(levelList);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
