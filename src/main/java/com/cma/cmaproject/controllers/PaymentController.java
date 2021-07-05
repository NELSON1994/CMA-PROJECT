package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.servicesImpl.PaymentServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.dao.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentServiceTemplate paymentServiceTemplate;

    @PostMapping("/makePayment")
    public ResponseEntity<GenericResponseWrapper> createPayments(@RequestBody PaymentDao payment){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.makePayment(payment);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponseWrapper> viewAllPayments(){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.viewPaymentAll();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{paymentId}")
    public ResponseEntity<GenericResponseWrapper> viewOnePayment(@PathVariable Long paymentId){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.viewIndividualPayment(paymentId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{paymentId}")
    public ResponseEntity<GenericResponseWrapper> deletePayment(@PathVariable Long paymentId){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.deleteIndividualPayment(paymentId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/approve")
    public ResponseEntity<GenericResponseWrapper> verifyPayment(@RequestBody ApproveRequestIdsDao approveRequestIdsDao){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.verifyIndividualPayment(approveRequestIdsDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/decline")
    public ResponseEntity<GenericResponseWrapper> declinePayment(@RequestBody ApproveRequestIdsDao approveRequestIdsDao){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.declinePayment(approveRequestIdsDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{userId}/{paymentId}")
    public ResponseEntity<GenericResponseWrapper> updatePayment(@PathVariable Long userId,@PathVariable Long paymentId,@RequestBody PaymentDao paymentDao){
        GenericResponseWrapper genericResponseWrapper=paymentServiceTemplate.updatePayment(userId,paymentId,paymentDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }





}
