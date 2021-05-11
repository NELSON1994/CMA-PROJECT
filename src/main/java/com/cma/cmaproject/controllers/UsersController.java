package com.cma.cmaproject.controllers;

import com.cma.cmaproject.model.User;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.ResetPasswordWrapper;
import com.cma.cmaproject.wrappers.UserLoginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @PostMapping("/create")
    public ResponseEntity<GenericResponseWrapper> createRole(@RequestBody User user) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.createUser(user);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponseWrapper> viewAllUsers() {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewAllUsers();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/activeUsers")
    public ResponseEntity<GenericResponseWrapper> viewAllActiveUsers() {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewAllActiveUsers();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/inactiveUsers")
    public ResponseEntity<GenericResponseWrapper> viewAllInActiveUsers() {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewAllInActiveUsers();
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<GenericResponseWrapper> viewOneRole(@PathVariable Long userId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewOneUser(userId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<GenericResponseWrapper> deleteUser(@PathVariable Long userId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.deleteUser(userId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<GenericResponseWrapper> updateUser(@PathVariable Long userId, @RequestBody User user) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.updateUser(userId, user);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/activate/{userId}")
    public ResponseEntity<GenericResponseWrapper> activateUser(@PathVariable Long userId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.enableUser(userId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<GenericResponseWrapper> deactivateUser(@PathVariable Long userId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.disableUser(userId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/assign/{userId}/role/{roleIDs}")
    public ResponseEntity<GenericResponseWrapper> assignUserRole(@PathVariable Long userId, @PathVariable Long[] roleIDs) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.assignUserRole(userId, roleIDs);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/resetpassword")
    public ResponseEntity<GenericResponseWrapper> resetUserPassword(@RequestBody ResetPasswordWrapper resetPasswordWrapper) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.resetPassword(resetPasswordWrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/login")
    public ResponseEntity<GenericResponseWrapper> login(@RequestBody UserLoginWrapper userLoginWrapper) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.userLogin(userLoginWrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
