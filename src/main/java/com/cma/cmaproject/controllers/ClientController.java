package com.cma.cmaproject.controllers;

import com.cma.cmaproject.service.CompanyService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private CompanyService companyService;

   // @CrossOrigin(origins = "http://localhost:9099")
    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponseWrapper> viewAllClients(){
        GenericResponseWrapper genericResponseWrapper=companyService.viewCompanyAll();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewOne{companyId}")
    public ResponseEntity<GenericResponseWrapper> viewOneClient(@PathVariable Long companyId){
        GenericResponseWrapper genericResponseWrapper=companyService.viewCompany(companyId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
