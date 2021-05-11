package com.cma.cmaproject.controllers;

import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customerOrders")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @PostMapping("/placeOrder")
    public ResponseEntity<GenericResponseWrapper> createOrders(@RequestBody CustomerOrders customerOrders){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.createOrder(customerOrders);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAllOrders")
    public ResponseEntity<GenericResponseWrapper> viewAllOrders(){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.viewOrdersAll();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewOrders/{orderId}")
    public ResponseEntity<GenericResponseWrapper> viewOneOrder(@PathVariable Long orderId){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.viewIndividualOrders(orderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/deleteOrders/{orderId}")
    public ResponseEntity<GenericResponseWrapper> deleteOrder(@PathVariable Long orderId){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.deleteIndividualOrders(orderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/verifyOrders/{orderId}")
    public ResponseEntity<GenericResponseWrapper> verifyOrder(@PathVariable Long orderId){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.verifyIndividualOrders(orderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
