package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.model.User;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.ResetPasswordWrapper;
import com.cma.cmaproject.wrappers.UserLoginWrapper;

public interface UserServiceTemplate {
    //create user
    GenericResponseWrapper createUser(User user);

    //view all users
    GenericResponseWrapper viewAllUsers();

    //view one user
    GenericResponseWrapper viewOneUser(Long userId);

    //view active user
    GenericResponseWrapper viewAllActiveUsers();

    GenericResponseWrapper viewAllInActiveUsers();

    //delete user
    GenericResponseWrapper deleteUser(Long userId);

    //update user
    GenericResponseWrapper updateUser(Long userId, User user);

    //disable user
    GenericResponseWrapper disableUser(Long userId);

    //activate user
    GenericResponseWrapper enableUser(Long userId);

    GenericResponseWrapper assignUserRole(Long userId, Long[] rolesIds);

    GenericResponseWrapper userLogin(UserLoginWrapper userLoginWrapper);

    GenericResponseWrapper resetPassword(ResetPasswordWrapper resetPasswordWrapper);

    //change password==> for first time log in
    String generateSixLengthPassword(String firstname);

    String encryptPassword(String plainTextPassword);
}
