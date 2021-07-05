package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.dao.LikelyHoodDao;
import com.cma.cmaproject.service.LikelyHoodService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/likelyhood")
public class LikelyHoodController {
    @Autowired
    private LikelyHoodService likelyHoodService;

    @PostMapping("/create/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> createLikelyhood(@RequestBody LikelyHoodDao likelyHoodDao,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=likelyHoodService.create(likelyHoodDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> viewAllLikelyhood(@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=likelyHoodService.viewAll(loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedServiceProviderId}/{likelyHoodId}")
    public ResponseEntity<GenericResponseWrapper> viewOneLikelyhood(@PathVariable Long likelyHoodId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=likelyHoodService.viewIndividual(likelyHoodId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedServiceProviderId}/{likelyHoodId}")
    public ResponseEntity<GenericResponseWrapper> deleteLikelyhood(@PathVariable Long likelyHoodId,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=likelyHoodService.delete(likelyHoodId,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/update/{loggedServiceProviderId}/{likelyHoodId}")
    public ResponseEntity<GenericResponseWrapper> updateLikelyhood(@PathVariable Long likelyHoodId,@RequestBody LikelyHoodDao likelyHoodDao,@PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=likelyHoodService.update(likelyHoodId,likelyHoodDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
    @PutMapping("/approve/{loggedServiceProviderId}")
    public ResponseEntity<GenericResponseWrapper> approveLikelyhood(@RequestBody ApproveRequestIdsDao approveRequestIdsDao, @PathVariable Long loggedServiceProviderId){
        GenericResponseWrapper genericResponseWrapper=likelyHoodService.approve(approveRequestIdsDao,loggedServiceProviderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
