package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.dao.UserCreationDao;
import com.cma.cmaproject.model.User;
import com.cma.cmaproject.wrappers.*;

public interface UserServiceTemplate {

    //create users
    GenericResponseWrapper createUser(Long loggedInUserId, UserCreationDao uwrapper);

    GenericResponseWrapper viewAllUsers(Long loggedInUserId);

    GenericResponseWrapper viewOneUser(Long loggedInUserId, Long userId);

    GenericResponseWrapper viewAllActiveUsers(Long loggedInUserId);

    //superadmin view users
    GenericResponseWrapper viewAllUsersBySuperAdmin(Long loggedUserId);

    //view all active users
    GenericResponseWrapper viewAllInActiveUsers(Long loggedInUserId);

    GenericResponseWrapper deleteUser(Long loggedInUserId, Long userId);

    GenericResponseWrapper updateUser(Long loggedInUserId, Long userId, UserCreationDao user);

    GenericResponseWrapper disableUser(Long loggedInUserId, Long userId);

    GenericResponseWrapper enableUser(Long loggedInUserId, Long userId);

    //assign role to user
    GenericResponseWrapper assignUserRole(Long loggedInUserId, Long userId, RoleIdsWrapper wrapper);

    // update user role
    GenericResponseWrapper updateUserRole(Long loggedInUserId, Long userId, Long RoleID);

    //user login
    GenericResponseWrapper userLogin(UserLoginWrapper userLoginWrapper);

    //user first TimeLogin
    GenericResponseWrapper changePassword(Long loggedUserId, ChangePasswordWrapper changePasswordWrapper);

    //reset password
    GenericResponseWrapper resetPassword(Long loggedInUserId, ResetPasswordWrapper resetPasswordWrapper);

    String generateSixLengthPassword(String firstname);

    String encryptPassword(String plainTextPassword);

    void saveAuditRails(String user, String company, String activity, String description);

    AuditUsernameCompanyWrapper createAudit(Long UserId);
}
