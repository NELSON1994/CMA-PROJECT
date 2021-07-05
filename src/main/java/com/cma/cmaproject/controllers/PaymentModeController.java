package com.cma.cmaproject.controllers;

import com.cma.cmaproject.service.PaymentModeService;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment-mode")
public class PaymentModeController {

    @Autowired
    private PaymentModeService paymentModeService;

    @PostMapping("/create")
    public ResponseEntity<GenericResponseWrapper> createPaymentMode(@RequestBody GeneralRequestDao wrapper){
        GenericResponseWrapper genericResponseWrapper=paymentModeService.createPMode(wrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponseWrapper> viewAllPaymentMode(){
        GenericResponseWrapper genericResponseWrapper=paymentModeService.viewAllPMode();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewOne/{paymentModeId}")
    public ResponseEntity<GenericResponseWrapper> viewOnePaymentMode(@PathVariable Long paymentModeId){
        GenericResponseWrapper genericResponseWrapper=paymentModeService.viewPMode(paymentModeId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{paymentModeId}")
    public ResponseEntity<GenericResponseWrapper> deletePaymentMode(@PathVariable Long paymentModeId){
        GenericResponseWrapper genericResponseWrapper=paymentModeService.deletePMode(paymentModeId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }
}
