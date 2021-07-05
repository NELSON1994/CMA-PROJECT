package com.cma.cmaproject.configs;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.model.User;
import com.cma.cmaproject.repository.CompanyRepository;
import com.cma.cmaproject.repository.RolesRepository;
import com.cma.cmaproject.repository.UserRepository;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.IpAndNameWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Configuration
public class HostConfigurationDetails {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CompanyRepository companyRepository;

    @Value("${spRole}")
    String spRole;

    @Value("${companyEmail}")
    String companyEmail;

    @Value("${defaultPass}")
    String defaultPass;

    @Value("${companyName}")
    String companyName;

    public IpAndNameWrapper ipAndHostName() {
        IpAndNameWrapper ipAndNameWrapper = new IpAndNameWrapper();

        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            ipAndNameWrapper.setIp(ip.toString());
            ipAndNameWrapper.setHostName(hostname);

        } catch (UnknownHostException e) {
            e.printStackTrace();

        }
        return ipAndNameWrapper;
    }

    @PostConstruct
    public void generateDefaultServiceProviderCredentials() {
        String fname = "cma";
        String othername = "sy-intelli";
        String email = companyEmail;
        String encPass = passwordEncoder.encode(defaultPass);
        User user = new User();
        Optional<User> user1 = userRepository.findByFirstNameAndOtherNameAndEmail(fname, othername, email);
        if (user1.isPresent()) {
            User user11 = user1.get();
            System.out.println("***********************************************************************");
            System.out.println("====  DEFAULT SERVICE PROVIDER ALREADY CREATED  ====");
            System.out.println("***********************************************************************");
        } else {
            user.setFirstName(fname);
            user.setOtherName(othername);
            user.setEmail(email);
            user.setActionStatus(Constants.userActive);
            user.setPassword(encPass);
            user.setFirstTimeLogIn(true);
            user.setUsername(user.getEmail().trim().toUpperCase());
            user.setIntrash(Constants.intrashNO);
            Company company=new Company();
            Optional<Company> company1=companyRepository.findByCompanyNameAndIntrash(companyName,Constants.intrashNO);
            if(company1.isPresent()){
                Company company2=company1.get();
                user.setCompany(company2);
            }
            else{
                company.setIndustry("Auditing");
                company.setNumberOfEmployees(0);
                company.setCompanyName(companyName);
                company.setIntrash(Constants.intrashNO);
                company.setActionStatus(Constants.actionApproved);
                companyRepository.save(company);
                user.setCompany(company);
            }
            userRepository.save(user);
            Optional<Roles> roles = rolesRepository.findByRoleNameAndIntrash(spRole, Constants.intrashNO);
            if (roles.isPresent()) {
                Roles roles1 = roles.get();
                roles1.setUser(user);
                rolesRepository.save(roles1);
            } else {
                Roles role = new Roles();
                role.setActionStatus(Constants.actionApproved);
                role.setRoleName(spRole);
                role.setIntrash(Constants.intrashNO);
                role.setUser(user);
                rolesRepository.save(role);
            }

        }

    }
}
