package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.ApproveRequestIdsDao;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.dao.OrdersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer-order")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @PostMapping("/place")
    public ResponseEntity<GenericResponseWrapper> createOrders(@RequestBody OrdersDao ordersDao){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.createOrder(ordersDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponseWrapper> viewAllOrders(){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.viewOrdersAll();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{orderId}")
    public ResponseEntity<GenericResponseWrapper> viewOneOrder(@PathVariable Long orderId){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.viewIndividualOrders(orderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<GenericResponseWrapper> deleteOrder(@PathVariable Long orderId){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.deleteIndividualOrders(orderId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/verify")
    public ResponseEntity<GenericResponseWrapper> verifyOrder(@RequestBody ApproveRequestIdsDao approveRequestIdsDao){
        GenericResponseWrapper genericResponseWrapper=customerOrderServiceTemplate.verifyIndividualOrders(approveRequestIdsDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
