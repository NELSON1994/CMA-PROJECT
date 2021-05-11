package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.model.User;
import com.cma.cmaproject.repository.RolesRepository;
import com.cma.cmaproject.repository.UserRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.ResetPasswordWrapper;
import com.cma.cmaproject.wrappers.UserLoginWrapper;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
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

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public GenericResponseWrapper createUser(User user) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            user.setActionStatus(Constants.userActive);
            user.setFirstTimeLogIn(true);
            user.setUsername(user.getEmail().trim().toUpperCase());
            user.setIntrash(Constants.intrashNO);
            String generatePass = generateSixLengthPassword(user.getFirstName());
            String encrptedPass = encryptPassword(generatePass);
            user.setPassword(encrptedPass);
            userRepository.save(user);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setMessage("User created successfully");
            genericResponseWrapper.setData(user);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper viewAllUsers() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<User> list = userRepository.findByIntrash(Constants.intrashNO);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewOneUser(Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user.isPresent()) {
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(user.get());
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + "Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewAllActiveUsers() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<User> list = userRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.userActive);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper viewAllInActiveUsers() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<User> list = userRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.userInactive);
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper deleteUser(Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user.isPresent()) {
                User user1 = user.get();
                user1.setIntrash(Constants.intrashYES);
                userRepository.save(user1);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("User Deleted Succesfully");
                genericResponseWrapper.setData(user1);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper updateUser(Long userId, User user) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<User> user1 = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user1.isPresent()) {
                User user2 = user1.get();
                user2.setEmail(user.getEmail());
                user2.setUsername(user.getEmail().trim().toUpperCase());
                user2.setFirstName(user.getFirstName());
                user2.setOtherName(user.getOtherName());
                userRepository.save(user2);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("User Updated Successfully");
                genericResponseWrapper.setData(user2);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper disableUser(Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user.isPresent()) {
                User user1 = user.get();
                user1.setActionStatus(Constants.userInactive);
                userRepository.save(user1);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("User Deactivated Succesfully");
                genericResponseWrapper.setData(user1);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper enableUser(Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
        try {
            if (user.isPresent()) {
                User user1 = user.get();
                user1.setActionStatus(Constants.userActive);
                userRepository.save(user1);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("User Activated Succesfully");
                genericResponseWrapper.setData(user1);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    @Override
    public GenericResponseWrapper assignUserRole(Long userId, Long[] rolesIds) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                User user1 = user.get();
                try {
                    for (Long id : rolesIds) {
                        Optional<Roles> roles = rolesRepository.findByIdAndIntrash(id, Constants.intrashNO);
                        if (roles.isPresent()) {
                            Roles roles1 = roles.get();
                            roles1.setUser(user1);
                            rolesRepository.save(roles1);
                            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                            genericResponseWrapper.setMessage("Roles assigned successfully");
                            genericResponseWrapper.setData(roles1.getUser());
                        } else {
                            genericResponseWrapper = customerOrderServiceTemplate.notFound();
                            genericResponseWrapper.setMessage("Role with ID : " + id + "NOT Found");
                        }
                    }
                } catch (Exception ex) {
                    genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
                    ex.printStackTrace();
                    genericResponseWrapper.setMessage("System Error Occurred");
                }
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    // update user role

    @Override
    public GenericResponseWrapper userLogin(UserLoginWrapper userLoginWrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            String encodedPassword=encryptPassword(userLoginWrapper.getPassword());
            String username=userLoginWrapper.getUsername().trim().toUpperCase();
            Optional<User> u=userRepository.findByUsernameAndIntrash(username, Constants.intrashNO);
            Optional<User> user=userRepository.findByUsernameAndPasswordAndIntrash(username,encodedPassword,Constants.intrashNO);
            if(u.isPresent()){
                if(user.isPresent()){
                    User user1=user.get();
                    if(!user1.isFirstTimeLogIn()){
                        if(user1.getActionStatus().equalsIgnoreCase(Constants.userActive)){
                            genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                            genericResponseWrapper.setMessage("Login Successful");
                            genericResponseWrapper.setData(user1);
                        }
                        else{
                            genericResponseWrapper.setData(user1);
                            genericResponseWrapper.setCode(204);
                            genericResponseWrapper.setStatus("SUCCESS");
                            genericResponseWrapper.setMessage("User Account is INACTIVE");
                        }

                    }
                    else{
                        genericResponseWrapper.setCode(202);
                        genericResponseWrapper.setMessage("Kindly Change your Password.>>(First Time Login)");
                        genericResponseWrapper.setStatus("SUCCESS");
                        genericResponseWrapper.setDate(new Date());
                        genericResponseWrapper.setData(user1);
                    }
                }
                else{
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("Invalid Login Credentials");
                }

            }
            else{
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with Usename : " + username + " NOT Found");
            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");
        }

        return genericResponseWrapper;
    }

    @Override
    public GenericResponseWrapper resetPassword(ResetPasswordWrapper resetPasswordWrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<User> user = userRepository.findByUsernameAndIntrashAndActionStatus(resetPasswordWrapper.getUsername().trim().toUpperCase(), Constants.intrashNO, Constants.userActive);
        try {
            if (user.isPresent()) {
                User user1 = user.get();
                user1.setFirstTimeLogIn(true);
                String generatedPass = generateSixLengthPassword(user1.getFirstName().trim());
                String encrptedPass = encryptPassword(generatedPass);
                user1.setPassword(encrptedPass);
                user1.setActionStatus(Constants.userActive);
                userRepository.save(user1);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Password Reset Successfully :NEW PASS >>" + generatedPass);
                genericResponseWrapper.setData(user1);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with username : " + resetPasswordWrapper.getUsername() + " Not Found");

            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            logger.error("=========ERROR====  : {}", ex.getMessage());
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    //change password==> for first time log in
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
        logger.info("******* Generated Password  = : {}", passwordGenerated);
        return passwordGenerated;
    }

    @Override
    public String encryptPassword(String plainTextPassword) {
        String encryptedPassword = null;
        encryptedPassword = passwordEncoder.encode(plainTextPassword);
        logger.info("*********** ENCODED PASSWORD : {}", encryptedPassword);
        return encryptedPassword;
    }


}
