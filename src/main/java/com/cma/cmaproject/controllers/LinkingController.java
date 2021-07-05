package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ListOfControlsDao;
import com.cma.cmaproject.dao.ListOfProceduresDao;
import com.cma.cmaproject.service.ControlService;
import com.cma.cmaproject.service.ProcedureService;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.RiskIDsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/link")
public class LinkingController {
    @Autowired
    private ControlService controlService;

    @Autowired
    private ProcedureService procedureService;

    @PutMapping("/controls-to-domain/{loggedServiceProviderId}/{domainID}")
    public ResponseEntity<GenericResponseWrapper> linkControlsToDomain(@PathVariable Long loggedServiceProviderId,@PathVariable Long domainID,@RequestBody ListOfControlsDao listOfControlsDao){
        GenericResponseWrapper genericResponseWrapper=controlService.linkControlsToDomain(loggedServiceProviderId,domainID,listOfControlsDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/procedures-to-control/{loggedServiceProviderId}/{controlID}")
    public ResponseEntity<GenericResponseWrapper> linkProceduresToControl(@PathVariable Long loggedServiceProviderId, @PathVariable Long controlID, @RequestBody ListOfProceduresDao listOfProceduresDao){
        GenericResponseWrapper genericResponseWrapper=controlService.linkProceduresToControl(loggedServiceProviderId,controlID,listOfProceduresDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/risks-to-procedure/{loggedServiceProviderId}/{procedureId}/{riskProfileId}")
    public ResponseEntity<GenericResponseWrapper> linkRisksToProcedure(@PathVariable Long loggedServiceProviderId, @PathVariable Long procedureId, @PathVariable Long riskProfileId){
        GenericResponseWrapper genericResponseWrapper=procedureService.linkProcedureToRisks(loggedServiceProviderId,procedureId,riskProfileId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
