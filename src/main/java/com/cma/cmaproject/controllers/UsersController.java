package com.cma.cmaproject.controllers;

import com.cma.cmaproject.dao.UserCreationDao;
import com.cma.cmaproject.model.User;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @PostMapping("/create/{loggedInUserID}")
    public ResponseEntity<GenericResponseWrapper> createUser(@RequestBody UserCreationDao userCreationDao, @PathVariable Long loggedInUserID) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.createUser(loggedInUserID, userCreationDao);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewAll/{loggedInUserID}")
    public ResponseEntity<GenericResponseWrapper> viewAllUsersByServiceProvider( @PathVariable Long loggedInUserID) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewAllUsers(loggedInUserID);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/activeUsers/{loggedInUserID}")
    public ResponseEntity<GenericResponseWrapper> viewAllActiveUsersByServiceProvider( @PathVariable Long loggedInUserID) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewAllActiveUsers(loggedInUserID);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/inactiveUsers/{loggedInUserID}")
    public ResponseEntity<GenericResponseWrapper> viewAllInActiveUsersByServiceProvider( @PathVariable Long loggedInUserID) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewAllInActiveUsers(loggedInUserID);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/view/{loggedInUserID}/{userId}")
    public ResponseEntity<GenericResponseWrapper> viewOneUser( @PathVariable Long loggedInUserID,@PathVariable Long userId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewOneUser(loggedInUserID,userId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @DeleteMapping("/delete/{loggedInUserID}/{userId}")
    public ResponseEntity<GenericResponseWrapper> deleteUser( @PathVariable Long loggedInUserID,@PathVariable Long userId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.deleteUser(loggedInUserID,userId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/update/{loggedInUserID}/{userId}")
    public ResponseEntity<GenericResponseWrapper> updateUser( @PathVariable Long loggedInUserID,@PathVariable Long userId, @RequestBody UserCreationDao user) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.updateUser(loggedInUserID,userId, user);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/activate/{loggedInUserID}/{userId}")
    public ResponseEntity<GenericResponseWrapper> activateUser( @PathVariable Long loggedInUserID,@PathVariable Long userId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.enableUser(loggedInUserID,userId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/deactivate/{loggedInUserID}/{userId}")
    public ResponseEntity<GenericResponseWrapper> deactivateUser( @PathVariable Long loggedInUserID,@PathVariable Long userId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.disableUser(loggedInUserID,userId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/assign/{loggedInUserID}/{userId}/roles")
    public ResponseEntity<GenericResponseWrapper> assignUserRole( @PathVariable Long loggedInUserID,@PathVariable Long userId, @RequestBody RoleIdsWrapper wrapper) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.assignUserRole(loggedInUserID,userId, wrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/{loggedInUserID}/resetpassword")
    public ResponseEntity<GenericResponseWrapper> resetUserPassword( @PathVariable Long loggedInUserID,@RequestBody ResetPasswordWrapper resetPasswordWrapper) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.resetPassword(loggedInUserID,resetPasswordWrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PutMapping("/changepassword/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> changePassword(@PathVariable Long loggedInUserId,@RequestBody ChangePasswordWrapper changePasswordWrapper) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.changePassword(loggedInUserId,changePasswordWrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponseWrapper> login(@RequestBody UserLoginWrapper userLoginWrapper) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.userLogin(userLoginWrapper);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

    @GetMapping("/viewUsers/{loggedInUserId}")
    public ResponseEntity<GenericResponseWrapper> viewUsersBySuperAdmin(@PathVariable Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = userServiceTemplate.viewAllUsersBySuperAdmin(loggedInUserId);
        return ResponseEntity.status(genericResponseWrapper.getCode()).body(genericResponseWrapper);
    }

}
