package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.GeneralRequestDao;
import com.cma.cmaproject.dao.LicenceDao;
import com.cma.cmaproject.model.*;
import com.cma.cmaproject.repository.*;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.LincenceTemplate;
import com.cma.cmaproject.servicesImpl.RolesTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.*;
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
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Autowired
    private RolesTemplate rolesTemplate;

    @Autowired
    private CompanyRepository companyRepository;

    // generate lincenceReferenceNumber
    public String generateLinceseRefNumber(String companyname) {
        String generatedRef = null;
        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMyyyyhhmmss");
        String strDate = formatter.format(d);
        if (companyname.length() < 3) {
            String cm1 = "CMA";
            generatedRef = cm1 + strDate;
        } else {
            String cm1 = companyname.substring(0, 3);
            generatedRef = cm1 + strDate;
        }
        return generatedRef;
    }

    //calculate Validity of the lincence
    public Date calculateLincenceExpiry(Date activationDate, int linenceDuration) {
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
    public GenericResponseWrapper generateLincense(LicenceDao lwrap, Long companyId,Long loggedServiceProviderId) {
        Licence licence=new Licence();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<Company> approvedCompanies = companyRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.actionApproved);
            Optional<Company> company = companyRepository.findByIntrashAndId(Constants.intrashNO, companyId);
            Date date=new Date();
            SimpleDateFormat  formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String strDate = formatter.format(date);
            Date dd=formatter.parse(strDate);
            if (company.isPresent()) {
                Company c = company.get();
                if (c.getActionStatus().equalsIgnoreCase(Constants.actionApproved)) {
                    licence.setCompany(c);
                    licence.setLincenceRef(lwrap.getLicenceRef());
                    licence.setIntrash(Constants.intrashNO);
                    licence.setLincenceVality(lduration);
                    licence.setActionStatus(Constants.licenceStatus1);
                    licence.setCreationDate(dd);
                    String ltrack=generateLinceseRefNumber(c.getCompanyName());
                    licence.setLincenceTrackingRef(ltrack);
                    Date exp = calculateLincenceExpiry(licence.getCreationDate(), lduration);
                    licence.setLincenceExpiryDate(exp);
                    licence.setActivationDate(dd);
                    lincenceRepository.save(licence);
                    userServiceTemplate.saveAuditRails(name,company8,"GENERATE LICENCE","SUCCESS: Licence generated Successfully");
                    // create superadmin credentials to the user
                    List<Payment> p = paymentsRepository.findPaymentByCompany(c);
                    Payment pp=p.get(0);
                    CustomerOrders customerOrders = pp.getCustomerOrders();
                    //set user using above credentials
                    User user = new User();
                    user.setFirstName(customerOrders.getFirstName());
                    user.setOtherName(customerOrders.getLastName());
                    user.setEmail(customerOrders.getEmail());
                    user.setCompany(c);
                    user.setActionStatus(Constants.userActive);
                    user.setFirstTimeLogIn(true);
                    user.setUsername(user.getEmail().trim().toUpperCase());
                    user.setIntrash(Constants.intrashNO);
                    //PASSWORD TO SEND ON EMAIL
                    String genPass=userService.generateSixLengthPassword(user.getFirstName());
                    String encP=userService.encryptPassword(genPass);
                    user.setPassword(encP);
                    userRepository.save(user);
                    userServiceTemplate.saveAuditRails(name,company8,"GENERATE LICENCE","SUCCESS:SuperAdmin Credentials generated Successfully");
                    //create role and assign to user
                    Roles role = new Roles();
                    GeneralRequestDao g=new GeneralRequestDao();
                    g.setName(superAdmin);
                    role.setUser(user);
                    role.setRoleName(superAdmin);
                    role.setIntrash(Constants.intrashNO);
                    role.setActionStatus(Constants.actionApproved);
                    rolesRepository.save(role);
                    userServiceTemplate.saveAuditRails(name,company8,"GENERATE LICENCE","SUCCESS:Role for SuperAdmin generated Successfully");
                    DetailsToSendToUser d=new DetailsToSendToUser();
                    d.setCompany(c.getCompanyName().toUpperCase());
                    d.setPassword(genPass);
                    d.setUserName(user.getUsername());
                    d.setRole(role.getRoleName());
                    d.setLicenceActiveDate(licence.getActivationDate());
                    d.setLicenceExpiryDate(licence.getLincenceExpiryDate());
                    d.setLicenceTrackingReference(licence.getLincenceTrackingRef());
                    GeneratedUserWrapper generatedUserWrapper = new GeneratedUserWrapper();
                    generatedUserWrapper.setUser(user);
                    generatedUserWrapper.setRole(role);
                    generatedUserWrapper.setLicence(licence);
                    generatedUserWrapper.setCompany(c);
                    generatedUserWrapper.setDetailsToSendToUser(d);

                    genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                    genericResponseWrapper.setMessage("Licence,User and Role Created Successfully");
                    genericResponseWrapper.setData(generatedUserWrapper);

                } else {
                    userServiceTemplate.saveAuditRails(name,company8,"GENERATE LICENCE","FAILED: The Client Status is NOT Approved");
                    genericResponseWrapper.setCode(403);
                    genericResponseWrapper.setData(c);
                    genericResponseWrapper.setMessage("The Client Status is NOT Approved");
                }
            } else {
                userServiceTemplate.saveAuditRails(name,company8,"GENERATE LICENCE","FAILED: Client with ID : " + companyId + "NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Client with ID : " + companyId + "NOT Found");
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name,company8,"GENERATE LICENCE","FAILED: System Error Occurred");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }

        return genericResponseWrapper;
    }

    //renew licence
    @Override
    public GenericResponseWrapper renewLicence(Long companyId,Long loggedServiceProviderId) {
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            Optional<Company> company1=companyRepository.findByIntrashAndId(Constants.intrashNO,companyId);
            if(company1.isPresent()){
                Company company=company1.get();
                Optional<Licence> licence=lincenceRepository.findLicenceByCompany(company);
                if(licence.isPresent()){
                    Licence licence1=licence.get();
                    if(licence1.getActionStatus().equalsIgnoreCase(Constants.licenceStatus4)){
                        licence1.setModifiedDate(new Date());
                        licence1.setRenewedDate(new Date());
                        Date exp = calculateLincenceExpiry(licence1.getRenewedDate(), lduration);
                        licence1.setLincenceExpiryDate(exp);
                        licence1.setActionStatus(Constants. licenceStatus2);
                        lincenceRepository.save(licence1);
                        userServiceTemplate.saveAuditRails(name,company8,"RENEW LICENCE","SUCCESS: Licence Renewed Successfully");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Licence Renewed Successfully");
                        genericResponseWrapper.setData(licence1);
                    }
                    else{
                        userServiceTemplate.saveAuditRails(name,company8,"RENEW LICENCE","FAILED: Licence had been Renewed");
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setStatus("Failed");
                        genericResponseWrapper.setMessage("This Licence had been Activated");
                        genericResponseWrapper.setData(licence1);
                    }
                }
                else {
                    userServiceTemplate.saveAuditRails(name,company8,"RENEW LICENCE","FAILED: No Licence Found");
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("No Licence Found");
                    genericResponseWrapper.setData(null);
                }
            }
            else{
                userServiceTemplate.saveAuditRails(name,company8,"RENEW LICENCE","FAILED: CLIENT with ID : "+companyId+" NOT FOUND");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Client with ID : "+companyId+" NOT FOUND");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name,company8,"RENEW LICENCE","FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    //activate lincense
    @Override
     public GenericResponseWrapper activateLicence(Long userId) {
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(userId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try{
            Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                Company company=user1.getCompany();
                Optional<Licence> licence=lincenceRepository.findLicenceByCompany(company);
                if(licence.isPresent()){
                    Licence licence1=licence.get();
                    if(licence1.getActionStatus().equalsIgnoreCase(Constants.licenceStatus1)){
                        licence1.setActivationDate(new Date());
                        licence1.setActionStatus(Constants.licenceStatus2);
                        lincenceRepository.save(licence1);
                        userServiceTemplate.saveAuditRails(name,company8,"ACTIVATE LICENCE","SUCCESS: Licence activated Successfully");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Licence Activated Successfully");
                        genericResponseWrapper.setData(licence1);
                    }
                    else{
                        userServiceTemplate.saveAuditRails(name,company8,"ACTIVATE LICENCE","FAILED: The Licence had been activated");
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setStatus("Failed");
                        genericResponseWrapper.setMessage("This Licence had been Activated");
                        genericResponseWrapper.setData(licence1);
                    }
                }
                else {
                    userServiceTemplate.saveAuditRails(name,company8,"ACTIVATE LICENCE","FAILED: No Licence Found");
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("No Licence Found");
                    genericResponseWrapper.setData(null);
                }

            } else {
                userServiceTemplate.saveAuditRails(name,company8,"ACTIVATE LICENCE","FAILED: No User Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("No User Found");
                genericResponseWrapper.setData(null);
            }
        }catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name,company8,"ACTIVATE LICENCE","FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }

        return genericResponseWrapper;
    }

    //deactivate lincense
    @Override
    public GenericResponseWrapper deactivateLicence(Long companyId,Long loggedServiceProviderId) {
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            Optional<Company> company1=companyRepository.findByIntrashAndId(Constants.intrashNO,companyId);
            if(company1.isPresent()){
                Company company=company1.get();
                Optional<Licence> licence=lincenceRepository.findLicenceByCompany(company);
                if(licence.isPresent()){
                    Licence licence1=licence.get();
                    if(licence1.getActionStatus().equalsIgnoreCase(Constants.licenceStatus2)){
                        licence1.setModifiedDate(new Date());
                        licence1.setActionStatus(Constants. licenceStatus5);
                        lincenceRepository.save(licence1);
                        userServiceTemplate.saveAuditRails(name,company8,"DE-ACTIVATE LICENCE","SUCCESS: Licence DeActivated Successfully");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Licence DeActivated Successfully");
                        genericResponseWrapper.setData(licence1);
                    }
                    else{
                        userServiceTemplate.saveAuditRails(name,company8,"DE-ACTIVATE LICENCE","FAILED: This Licence had been Activated");
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setStatus("Failed");
                        genericResponseWrapper.setMessage("This Licence had been Activated");
                        genericResponseWrapper.setData(licence1);
                    }
                }
                else {
                    userServiceTemplate.saveAuditRails(name,company8,"DE-ACTIVATE LICENCE","FAILED: No Licence Found");
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("No Licence Found");
                    genericResponseWrapper.setData(null);
                }
            }
            else{
                userServiceTemplate.saveAuditRails(name,company8,"DE-ACTIVATE LICENCE","FAILED: Company with ID : "+companyId+" NOT FOUND");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Company with ID : "+companyId+" NOT FOUND");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name,company8,"DE-ACTIVATE LICENCE","FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");

        }
        return genericResponseWrapper;

    }

    //view all licences
    @Override
    public GenericResponseWrapper viewAllLicences(Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            List<Licence> list = lincenceRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name,company8,"VIEW ALL LICENCE","SUCCESS: Successful");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name,company8,"VIEW ALL LICENCE","FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    //view individual licence
    @Override
    public GenericResponseWrapper viewIndividualLicence(Long licenceId,Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<Licence> licence = lincenceRepository.findByIdAndIntrash(licenceId, Constants.intrashNO);
        try {
            if (licence.isPresent()) {
                userServiceTemplate.saveAuditRails(name,company8,"VIEW INDIVIDUAL LICENCE","SUCCESS: Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(licence.get());
            } else {
                userServiceTemplate.saveAuditRails(name,company8,"VIEW INDIVIDUAL LICENCE","FAILED: Licence with ID : " + licenceId + " Not Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Licence with ID : " + licenceId + " Not Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name,company8,"VIEW INDIVIDUAL LICENCE","FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }
}
