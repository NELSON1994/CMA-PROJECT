package com.cma.cmaproject.controllers;

import com.cma.cmaproject.model.Payment;
import com.cma.cmaproject.servicesImpl.PaymentServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentServiceTemplate paymentServiceTemplate;

    @PostMapping("/makePayment")
    public ResponseEntity<GenericResponseWrapper> createPayments(@RequestBody Payment payment){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.makePayment(payment);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAllPayments")
    public ResponseEntity<GenericResponseWrapper> viewAllPayments(){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.viewPaymentAll();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewPayment/{paymentId}")
    public ResponseEntity<GenericResponseWrapper> viewOnePayment(@PathVariable Long paymentId){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.viewIndividualPayment(paymentId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/deletePayment/{paymentId}")
    public ResponseEntity<GenericResponseWrapper> deletePayment(@PathVariable Long paymentId){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.deleteIndividualPayment(paymentId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/verifyPayment/{paymentId}")
    public ResponseEntity<GenericResponseWrapper> verifyPayment(@PathVariable Long paymentId){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.verifyIndividualPayment(paymentId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }



}
