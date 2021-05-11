package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.CustomerOrders;
import com.cma.cmaproject.model.Licence;
import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.model.User;
import com.cma.cmaproject.repository.CustomerOrdersRepository;
import com.cma.cmaproject.repository.LincenceRepository;
import com.cma.cmaproject.repository.PaymentsRepository;
import com.cma.cmaproject.repository.UserRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.LincenceTemplate;
import com.cma.cmaproject.servicesImpl.RolesTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.GeneratedUserWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LincenseService implements LincenceTemplate {

    @Value("${linencebasicduration}")
    int lduration;

    @Value("${superAdmin}")
    String superAdmin;

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private LincenceRepository lincenceRepository;
    @Autowired
    private CustomerOrdersRepository customerOrdersRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Autowired
    private RolesTemplate rolesTemplate;

    @Autowired
    private UserRepository userRepository;

// generate lincenceReferenceNumber
    public String generateLinceseRefNumber(String companyname){
        String generatedRef=null;
        Date d=new Date();
        SimpleDateFormat  formatter = new SimpleDateFormat("ddMyyyyhhmmss");
        String strDate = formatter.format(d);
        if(companyname.length()<3){
            String cm1="CMA";
            generatedRef=cm1+strDate;
        }
        else {
            String cm1 = companyname.substring(0,3);
            generatedRef=cm1+strDate;
        }
        return generatedRef;
    }

    //calculate Validity of the lincence
    public Date calculateLincenceExpiry(Date activationDate,int linenceDuration){
        Calendar c = Calendar.getInstance();
        c.setTime(activationDate);

        // manipulate date
       // c.add(Calendar.YEAR, 1);
        c.add(Calendar.MONTH, linenceDuration);
        Date currentDatePlusOne = c.getTime();

        return currentDatePlusOne;
    }

    // generate linence
    @Override
    public GenericResponseWrapper generateLincense(Licence licence, Long orderId){
        GenericResponseWrapper genericResponseWrapper=new GenericResponseWrapper();
        try{
            List<CustomerOrders> paidOrders=customerOrdersRepository.findByIdAndActionStatusAndIntrash(orderId,Constants.paymentStatus3,Constants.intrashNO);
            if(paidOrders.size()>0){
                CustomerOrders customerOrders=paidOrders.get(0);
                // generate username  // password
                User user=new User();
                user.setFirstName(customerOrders.getFirstName());
                user.setOtherName(customerOrders.getLastName());
                user.setEmail(customerOrders.getEmail());

                userServiceTemplate.createUser(user);// the user have been created
                Roles role=new Roles();
                role.setRoleName(superAdmin);
                role.setUser(user);;

                rolesTemplate.createRole(role);// save the user role as on config file
                licence.setCustomerOrders(customerOrders);
                licence.setUser(user);
                licence.setIntrash(Constants.intrashNO);
                licence.setLincenceVality(lduration);
                licence.setActionStatus(Constants.licenceStatus1);
                licence.setCreationDate(new Date());
                licence.setLincenceTrackingRef(null);
                licence.setLincenceExpiryDate(new Date());
                lincenceRepository.save(licence);

                GeneratedUserWrapper generatedUserWrapper=new GeneratedUserWrapper();
                generatedUserWrapper.setUser(user);
                generatedUserWrapper.setRole(role);
                generatedUserWrapper.setLicence(licence);

                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Licence,User and Role Created Successfully");
                genericResponseWrapper.setData(generatedUserWrapper);
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Order with ID : "+orderId + "NOT Found");
            }

        }
        catch(Exception ex){
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }

        return genericResponseWrapper;
    }

    //renew licence
    @Override
    public GenericResponseWrapper renewLicence(Long licenceId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Licence> licence = lincenceRepository.findByIdAndIntrashAndActionStatus(licenceId, Constants.intrashNO,Constants.licenceStatus4);
        try {
            if (licence.isPresent()) {
                Licence licence1 = licence.get();
                licence1.setActionStatus(Constants.licenceStatus1);
                lincenceRepository.save(licence1);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Licence Renewed Successfully");
                genericResponseWrapper.setData(licence1);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Licence with ID : " + licenceId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    //activate lincense
    @Override
   // public GenericResponseWrapper activateLicence(Long userId) {
     public GenericResponseWrapper activateLicence(Long userId, Long[] linenceIds) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<User> user=userRepository.findByIdAndIntrash(userId,Constants.intrashNO);
        if (user.isPresent()){
            User user1=user.get();
            Optional<Licence> licence=lincenceRepository.findByUser(user1);// can be list///TODO: CHECK THIS:USER CAN HAVE MANY LICENCES
            List<Licence> userlicence=lincenceRepository.findByUserAndActionStatus(user1,Constants.licenceStatus1);
            List<Licence> activatedLicence=new ArrayList<>();
            //=============list
            if(userlicence.size()>0){
                for(Long id:linenceIds){
                    Licence licence1=lincenceRepository.findByIdAndIntrash(id,Constants.intrashNO).get();
                    licence1.setActionStatus(Constants.licenceStatus2);
                    licence1.setActivationDate(new Date());
                    //Date expiry=calculateLincenceExpiry(licence1.getActivationDate(),licence1.getLincenceVality());
                    Date expiry=calculateLincenceExpiry(licence1.getCreationDate(),licence1.getLincenceVality());
                    licence1.setLincenceExpiryDate(expiry);
                    lincenceRepository.save(licence1);
                    activatedLicence.add(licence1);
                }
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Licence Activated Successfully");
                genericResponseWrapper.setData(activatedLicence);

            }
            else{
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("No Licence to be Activated for the User : " + user1.getFirstName());
                genericResponseWrapper.setData(null);
            }

            //============
//
//            if(licence.isPresent()){
//              Licence licence1=licence.get();
//              licence1.setActionStatus(Constants.licenceStatus2);
//              licence1.setActivationDate(new Date());
//              Date expiry=calculateLincenceExpiry(licence1.getActivationDate(),licence1.getLincenceVality());
//              licence1.setLincenceExpiryDate(expiry);
//              lincenceRepository.save(licence1);
//                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
//                genericResponseWrapper.setMessage("Licence Activated Successfully");
//                genericResponseWrapper.setData(licence1);
//            }
//            else{
//                genericResponseWrapper = customerOrderServiceTemplate.notFound();
//                genericResponseWrapper.setMessage("Licence for the User : " + user1.getFirstName() + " "+ user1.getOtherName() + " Not Found");
//                genericResponseWrapper.setData(null);
//            }
        }
        else{
            genericResponseWrapper = customerOrderServiceTemplate.notFound();
            genericResponseWrapper.setMessage("User with ID : " + userId + " Not Found");
            genericResponseWrapper.setData(null);
        }
        return genericResponseWrapper;
    }

    //deactivate lincense
    @Override
    public GenericResponseWrapper deactivateLicence(Long licenceId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Licence> licence = lincenceRepository.findByIdAndIntrash(licenceId, Constants.intrashNO);
        try {
            if (licence.isPresent()) {
                Licence licence1 = licence.get();
                licence1.setActionStatus(Constants.licenceStatus1);
                lincenceRepository.save(licence1);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Licence Deactivated Successfully");
                genericResponseWrapper.setData(licence1);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Licence with ID : " + licenceId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    //view all licences
    @Override
    public GenericResponseWrapper viewAllLicences() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<CustomerOrders> list = lincenceRepository.findByIntrash(Constants.intrashNO);
            genericResponseWrapper=customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper =customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    //view individual licence
    @Override
    public GenericResponseWrapper viewIndividualLicence(Long licenceId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Optional<Licence> licence=lincenceRepository.findByIdAndIntrash(licenceId,Constants.intrashNO);
        try {
            if (licence.isPresent()){
                genericResponseWrapper=customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(licence.get());
            }
            else{
                genericResponseWrapper=customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Licence with ID : " + licenceId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        }
        catch(Exception ex){
            genericResponseWrapper =customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
