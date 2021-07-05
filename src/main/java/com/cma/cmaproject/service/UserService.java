package com.cma.cmaproject.service;

import com.cma.cmaproject.configs.HostConfigurationDetails;
import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.UserCreationDao;
import com.cma.cmaproject.model.*;
import com.cma.cmaproject.repository.AuditRailsRepository;
import com.cma.cmaproject.repository.LincenceRepository;
import com.cma.cmaproject.repository.RolesRepository;
import com.cma.cmaproject.repository.UserRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceTemplate {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LincenceRepository lincenceRepository;

    @Autowired
    private HostConfigurationDetails hostConfigurationDetails;
    @Autowired
    private AuditRailsRepository auditRailsRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

    //create users
    @Override
    public GenericResponseWrapper createUser(Long loggedInUserId, UserCreationDao uwrapper) {
        User user = new User();
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> u = userRepository.findByIdAndIntrash(loggedInUserId, Constants.intrashNO);
            if (u.isPresent()) {
                User user1 = u.get();
                if (!user1.isFirstTimeLogIn()) {
                    Company company = user1.getCompany();
                    user.setEmail(uwrapper.getEmail());
                    user.setFirstName(uwrapper.getFirstName());
                    user.setOtherName(uwrapper.getOtherName());
                    user.setActionStatus(Constants.userActive);
                    user.setFirstTimeLogIn(true);
                    user.setUsername(user.getEmail().trim().toUpperCase());
                    user.setIntrash(Constants.intrashNO);
                    String generatePass = generateSixLengthPassword(user.getFirstName());
                    String encrptedPass = encryptPassword(generatePass);
                    user.setPassword(encrptedPass);
                    user.setCompany(company);
                    userRepository.save(user);
                    saveAuditRails(name, company8, "CREATE USER", "SUCCESS: User Created Successfully");
                    genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                    genericResponseWrapper.setMessage("User created successfully:  Pass >>: "+generatePass);
                    genericResponseWrapper.setData(user);
                } else {
                    saveAuditRails(name, company8, "CREATE USER", "FAILED: User needs to Change Log In Credention to create the user");
                    genericResponseWrapper.setStatus("Failed");
                    genericResponseWrapper.setCode(300);
                    genericResponseWrapper.setMessage("Kindly,Change your Log In Credention to create the user");
                    genericResponseWrapper.setData(user1);
                }
            } else {
                saveAuditRails(name, company8, "CREATE USER", "FAILED: User with ID : " + loggedInUserId + "Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + loggedInUserId + "Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "CREATE USER", "FAILED: System Error Occurred");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper viewAllUsers(Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<User> list = userRepository.findByIntrash(Constants.intrashNO);
            saveAuditRails(name, company8, "VIEW ALL USERS", "SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "VIEW ALL USERS", "FAILED: System Error");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewOneUser(Long loggedInUserId, Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user.isPresent()) {
                saveAuditRails(name, company8, "VIEW INDIVIDUAL USER", "SUCCESS: Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(user.get());
            } else {
                saveAuditRails(name, company8, "VIEW INDIVIDUAL USER", "FAILED: User with ID : " + userId + "Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + "Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "VIEW INDIVIDUAL USER", "FAILED: System Error Occurred");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewAllActiveUsers(Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<User> list = userRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.userActive);
            saveAuditRails(name, company8, "VIEW ACTIVE USERS", "SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "VIEW ACTIVE USERS", "FAILED: System Error");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    //superadmin view users
    @Override
    public GenericResponseWrapper viewAllUsersBySuperAdmin(Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(loggedUserId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                if (user1.getActionStatus().equalsIgnoreCase(Constants.userActive)) {
                    Company c = user1.getCompany();
                    List<User> users = userRepository.findUsersByCompanyAndIntrash(c, Constants.intrashNO);
                    saveAuditRails(name, company8, "VIEW ACTIVE USERS", "SUCCESS: Successful");
                    genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                    genericResponseWrapper.setMessage("Users Fetched Successfully");
                    genericResponseWrapper.setData(users);
                } else {
                    saveAuditRails(name, company8, "VIEW ACTIVE USERS", "FAILED: User Status is : " + user1.getActionStatus());
                    genericResponseWrapper.setStatus("Failed");
                    genericResponseWrapper.setCode(300);
                    genericResponseWrapper.setMessage("User Status is : " + user1.getActionStatus());
                }
            } else {
                saveAuditRails(name, company8, "VIEW ACTIVE USERS", "FAILED: User with ID : " + loggedUserId + "Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + loggedUserId + "Not Found");
                genericResponseWrapper.setData(null);
            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "VIEW ACTIVE USERS", "FAILED : System Error");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }


    //view all active users
    @Override
    public GenericResponseWrapper viewAllInActiveUsers(Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<User> list = userRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.userInactive);
            saveAuditRails(name, company8, "VIEW ACTIVE USERS", "SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "VIEW ACTIVE USERS", "FAILED: System Error Occurred");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteUser(Long loggedInUserId, Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user.isPresent()) {
                User user1 = user.get();
                user1.setIntrash(Constants.intrashYES);
                userRepository.save(user1);
                saveAuditRails(name, company8, "DELETING USER", "User Deleted Succesfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("User Deleted Succesfully");
                genericResponseWrapper.setData(user1);
            } else {
                saveAuditRails(name, company8, "DELETING USER", "FAILED: User with ID : " + userId + " Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "DELETING USER", "FAILED: System Error Occurred");
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper updateUser(Long loggedInUserId, Long userId, UserCreationDao user) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<User> user1 = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user1.isPresent()) {
                User user2 = user1.get();
                user2.setEmail(user.getEmail());
                user2.setUsername(user.getEmail().trim().toUpperCase());
                user2.setFirstName(user.getFirstName());
                user2.setOtherName(user.getOtherName());
                userRepository.save(user2);
                saveAuditRails(name, company8, "UPDATING USER", "SUCCESS: User Updated Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("User Updated Successfully");
                genericResponseWrapper.setData(user2);
            } else {
                saveAuditRails(name, company8, "UPDATING USER", "FAILED: User with ID : " + userId + " Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "UPDATING USER", "FAILED: System Error");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper disableUser(Long loggedInUserId, Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user.isPresent()) {
                User user1 = user.get();
                user1.setActionStatus(Constants.userInactive);
                userRepository.save(user1);
                saveAuditRails(name, company8, "DE-ACTIVATING USER", "SUCCESS: User Deactivated Succesfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("User Deactivated Succesfully");
                genericResponseWrapper.setData(user1);
            } else {
                saveAuditRails(name, company8, "DE-ACTIVATING USER", "FAILED: User with ID : " + userId + " Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            saveAuditRails(name, company8, "DE-ACTIVATING USER", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper enableUser(Long loggedInUserId, Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user.isPresent()) {
                User user1 = user.get();
                user1.setActionStatus(Constants.userActive);
                userRepository.save(user1);
                saveAuditRails(name, company8, "ACTIVATING USER", "SUCCESS: User Activated Succesfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("User Activated Succesfully");
                genericResponseWrapper.setData(user1);
            } else {
                saveAuditRails(name, company8, "ACTIVATING USER", "FAILED: User with ID : " + userId + " Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            saveAuditRails(name, company8, "ACTIVATING USER", "FAILED: System Error Occurred");
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    //assign role to user
    @Override
    public GenericResponseWrapper assignUserRole(Long loggedInUserId, Long userId, RoleIdsWrapper wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Long[] rolesIds=wrapper.getRoleIDs();
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                try {
                    for (Long id : rolesIds) {
                        Optional<Roles> roles = rolesRepository.findByIdAndIntrash(id, Constants.intrashNO);
                        if (roles.isPresent()) {
                            Roles roles1 = roles.get();
                            roles1.setUser(user1);
                            rolesRepository.save(roles1);
                            saveAuditRails(name, company8, "ASSIGN USER ROLES", "SUCCESS: Roles assigned successfully");
                            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                            genericResponseWrapper.setMessage("Roles assigned successfully");
                            genericResponseWrapper.setData(roles1.getUser());
                        } else {
                            saveAuditRails(name, company8, "ASSIGN USER ROLES", "FAILED :Role with ID : " + id + "NOT Found");
                            genericResponseWrapper = customerOrderServiceTemplate.notFound();
                            genericResponseWrapper.setMessage("Role with ID : " + id + "NOT Found");
                        }
                    }
                } catch (Exception ex) {
                    saveAuditRails(name, company8, "ASSIGN USER ROLES", "FAILED: System Error Occurred");
                    genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
                    ex.printStackTrace();
                    genericResponseWrapper.setMessage("System Error Occurred");
                }
            } else {
                saveAuditRails(name, company8, "ASSIGN USER ROLES", "FAILED: User NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User NOT Found");
                genericResponseWrapper.setData(null);
            }
        } catch (Exception ex) {
            saveAuditRails(name, company8, "ASSIGN USER ROLES", "FAILED :System Error Occurred");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    // update user role
    @Override
    public GenericResponseWrapper updateUserRole(Long loggedInUserId, Long userId, Long RoleID) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                Optional<Roles> roles = rolesRepository.findByIdAndIntrash(RoleID, Constants.intrashNO);
                if (roles.isPresent()) {
                    Roles role = roles.get();
                    role.setUser(user1);
                    rolesRepository.save(role);
                    saveAuditRails(name, company8, "UPDATE USER ROLES", "SUCCESS: User :" + user1.getFirstName() + " assigned Role :" + role.getRoleName());
                    genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                    genericResponseWrapper.setMessage("User :" + user1.getFirstName() + " assigned Role :" + role.getRoleName());
                    genericResponseWrapper.setData(role);
                } else {
                    saveAuditRails(name, company8, "UPDATE USER ROLES", "FAILED: Role with ID : " + RoleID + " NOT Found");
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("Role with ID : " + RoleID + " NOT Found");
                }
            } else {
                saveAuditRails(name, company8, "UPDATE USER ROLES", "FAILED:m User with ID : " + userId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " NOT Found");
            }
        } catch (Exception ex) {
            saveAuditRails(name, company8, "UPDATE USER ROLES", "FAILED: System Error Occured");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    //user login
    @Override
    public GenericResponseWrapper userLogin(UserLoginWrapper userLoginWrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            String username = userLoginWrapper.getUsername().trim().toUpperCase();
            Optional<User> u = userRepository.findByUsernameAndIntrash(username, Constants.intrashNO);
            System.out.println("++++++++++++++++++++++++++");
            if (u.isPresent()) {
                System.out.println("++++++++++++++++++++++++++");
                String encodedPassword = encryptPassword(userLoginWrapper.getPassword().trim());
                String pass = userLoginWrapper.getPassword().trim();
                User user1 = u.get();
                Company c = user1.getCompany();
                if (passwordEncoder.matches(pass, user1.getPassword())) {
                    if(!user1.isFirstTimeLogIn()){
                        if (user1.getActionStatus().equalsIgnoreCase(Constants.userActive)) {
                            FinalLoginResponseWrapper finalLoginResponseWrapper = new FinalLoginResponseWrapper();
                            Roles role = rolesRepository.findByUser(user1);
                            finalLoginResponseWrapper.setUser(user1);
                            finalLoginResponseWrapper.setUserRole(role.getRoleName());
                            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                            genericResponseWrapper.setMessage("Login Successful");
                            genericResponseWrapper.setData(finalLoginResponseWrapper);
                            Company company = user1.getCompany();
                            AuditUsernameCompanyWrapper a = new AuditUsernameCompanyWrapper();
                            a.setUser(user1.getUsername());
                            a.setCompany(c.getCompanyName());
                            saveAuditRails(user1.getUsername(), c.getCompanyName(), "LOG IN", "SUCCESS: Login Successful");

                        } else {
                                genericResponseWrapper.setData(userLoginWrapper);
                                genericResponseWrapper.setCode(300);
                                genericResponseWrapper.setStatus("Failed");
                                genericResponseWrapper.setMessage("User Status is : " + user1.getActionStatus());
                                saveAuditRails(user1.getUsername(), c.getCompanyName(), "LOG IN", "FAILED: Login Failed,User STATUS is : " + user1.getActionStatus());

                        }
                    }
                    else{
                        genericResponseWrapper.setData(user1);
                        genericResponseWrapper.setCode(301);
                        genericResponseWrapper.setStatus("Failed");
                        genericResponseWrapper.setMessage("Change Your Password");
                        saveAuditRails(user1.getUsername(), c.getCompanyName(), "LOG IN", "FAILED: Login Failed,User needs to Change Password First");

                    }

                } else {
                    genericResponseWrapper.setData(userLoginWrapper);
                    genericResponseWrapper.setCode(300);
                    genericResponseWrapper.setStatus("Failed");
                    genericResponseWrapper.setMessage("Invalid Password");
                    Company c1 = user1.getCompany();
                    saveAuditRails(user1.getUsername(), c1.getCompanyName(), "LOG IN", "FAILED: Login Failed,User Provided invalid Password");
                }
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with Usename : " + username + " NOT Found");
                saveAuditRails(username, null, "LOG IN", "FAILED: User with Usename : " + username + " NOT Found");
            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
            saveAuditRails(null, null, "LOG IN", "FAILED: System Error");
        }
        return genericResponseWrapper;
    }

    //user first TimeLogin
    @Override
    public GenericResponseWrapper changePassword(Long loggedUserId, ChangePasswordWrapper changePasswordWrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> u = userRepository.findByIdAndIntrash(loggedUserId, Constants.intrashNO);
            if (u.isPresent()) {
                User user1 = u.get();
                if (user1.isFirstTimeLogIn()) {
                    if (changePasswordWrapper.getNewPassword().equalsIgnoreCase(changePasswordWrapper.getConfirmPassword())) {
                        String en = encryptPassword(changePasswordWrapper.getNewPassword().trim());
                        user1.setPassword(en);
                        user1.setModifiedDate(new Date());
                        user1.setActionStatus(Constants.userActive);
                        user1.setFirstTimeLogIn(false);
                        userRepository.save(user1);
                        saveAuditRails(name, company8, "FIRST TIME LOGIN", "SUCCESS: Password Changed Successfully");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Password Changed Successfully");
                        genericResponseWrapper.setData(user1);

                    } else {
                        saveAuditRails(name, company8, "FIRST TIME LOGIN", "FAILED: Mismatching Password provided");
                        genericResponseWrapper.setStatus("Failed");
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setMessage("Mismatching Passwords");
                        genericResponseWrapper.setData(changePasswordWrapper);
                    }
                } else {
                    saveAuditRails(name, company8, "FIRST TIME LOGIN", "FAILED: User Had Changed there Password");
                    genericResponseWrapper.setStatus("Failed");
                    genericResponseWrapper.setCode(300);
                    genericResponseWrapper.setMessage("User Had Changed there Password");
                    genericResponseWrapper.setData(user1);
                }
            } else {
                saveAuditRails(null, null, "FIRST TIME LOGIN", "FAILED: User with id : " + loggedUserId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with id : " + loggedUserId + " NOT Found");
            }
        } catch (Exception ex) {
            saveAuditRails(null, null, "FIRST TIME LOGIN", "FAILED: System error occurs");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }

        return genericResponseWrapper;
    }

    //reset password
    @Override
    public GenericResponseWrapper resetPassword(Long loggedInUserId, ResetPasswordWrapper resetPasswordWrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> user = userRepository.findByUsernameAndIntrashAndActionStatus(resetPasswordWrapper.getUsername().trim().toUpperCase(), Constants.intrashNO, Constants.userActive);
            if (user.isPresent()) {
                User user1 = user.get();
                user1.setFirstTimeLogIn(true);
                String generatedPass = generateSixLengthPassword(user1.getFirstName().trim());
                String encrptedPass = encryptPassword(generatedPass);
                user1.setPassword(encrptedPass);
                user1.setActionStatus(Constants.userActive);
                userRepository.save(user1);
                saveAuditRails(name, company8, "RESET PASSWORD", "SUCCESS: Password reset was successfull");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Password Reset Successfully :NEW PASS >>" + generatedPass);
                genericResponseWrapper.setData(user1);
            } else {
                saveAuditRails(name, company8, "RESET PASSWORD", "FAILED: User with username : " + resetPasswordWrapper.getUsername() + " Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with username : " + resetPasswordWrapper.getUsername() + " Not Found");
            }

        } catch (Exception ex) {
            saveAuditRails(name, company8, "RESET PASSWORD", "FAILED: System Error Occurred");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    //generate password
    @Override
    public String generateSixLengthPassword(String firstname) {
        SecureRandom secureRandom = new SecureRandom();
        int num = secureRandom.nextInt(100000);
        String formatted = String.format("%05d", num);
        String passwordGenerated = null;
        String appenddd = null;
        if (firstname != null && firstname.length() > 1) {
            appenddd = firstname.substring(0, 1);
        } else {
            appenddd = "c";
        }
        passwordGenerated = formatted + appenddd;
        return passwordGenerated;
    }

    @Override
    public String encryptPassword(String plainTextPassword) {
        String encryptedPassword = null;
        encryptedPassword = passwordEncoder.encode(plainTextPassword);
        return encryptedPassword;
    }

    @Override
    public void saveAuditRails(String user, String company, String activity, String description) {
        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setActivityType(activity);
        auditLogs.setDescription(description);
        IpAndNameWrapper a = hostConfigurationDetails.ipAndHostName();
        auditLogs.setIpAddress(a.getIp());
        auditLogs.setInsertionDate(new Date());
        auditLogs.setUser(user);
        auditLogs.setCompanyName(company);
        auditRailsRepository.save(auditLogs);
    }

    @Override
    public AuditUsernameCompanyWrapper createAudit(Long UserId) {
        AuditUsernameCompanyWrapper c = new AuditUsernameCompanyWrapper();
        Optional<User> user = userRepository.findByIdAndIntrash(UserId, Constants.intrashNO);
        if (user.isPresent()) {
            User user1 = user.get();
            c.setUser(user1.getUsername());
            Company company = user1.getCompany();
            if (company != null) {
                c.setCompany(company.getCompanyName());
            } else {
                c.setCompany(null);
            }
        } else {
            c.setCompany(null);
            c.setUser(null);
        }

        return c;
    }

}
