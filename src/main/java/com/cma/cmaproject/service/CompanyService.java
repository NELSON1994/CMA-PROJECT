package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.Licence;
import com.cma.cmaproject.model.Payment;
import com.cma.cmaproject.repository.CompanyRepository;
import com.cma.cmaproject.repository.LincenceRepository;
import com.cma.cmaproject.repository.PaymentsRepository;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.wrappers.LatestPaymentDetailsWrapper;
import com.cma.cmaproject.wrappers.OneCompanyDetailsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private LincenceRepository lincenceRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    public GenericResponseWrapper viewCompanyAll() {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        try {
            List<Company> list = companyRepository.findByIntrashAndActionStatus(Constants.intrashNO,Constants.actionApproved);
            genericResponseWrapper=customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewCompany(Long companyId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        OneCompanyDetailsWrapper cdm=new OneCompanyDetailsWrapper();
        try {
            Optional<Company> company=companyRepository.findByIntrashAndId(Constants.intrashNO,companyId);
            if(company.isPresent()){
                Company company1=company.get();
                cdm.setCompanyCreationDate(company1.getCreationDate());
                cdm.setCompanyName(company1.getCompanyName());
                cdm.setCompanyStatus(company1.getActionStatus());
                Optional<Licence> licence=lincenceRepository.findLicenceByCompanyAndIntrash(company1,Constants.intrashNO);
                if(licence.isPresent()){
                    Licence licence1=licence.get();
                    cdm.setLicenceRef(licence1.getLincenceRef());
                    cdm.setLicenceActivationDate(licence1.getActivationDate());
                    cdm.setLicenceIssuedDate(licence1.getCreationDate());
                    cdm.setLicenceExpiryDate(licence1.getLincenceExpiryDate());
                    cdm.setLicenceStatus(licence1.getActionStatus());
                }
                List<Payment> pays=paymentsRepository.findPaymentByCompany(company1);
                if(pays.size()>0){
                    Payment payment=pays.get(0);
                    LatestPaymentDetailsWrapper wrapper=new LatestPaymentDetailsWrapper();
                    wrapper.setAmount(payment.getAmount());
                    wrapper.setEmailAddress(payment.getEmailAddress());
                    wrapper.setPaymentDate(payment.getInsertionDate());
                    wrapper.setPaymentMode(payment.getPaymentMode());
                    wrapper.setPaymentReference(payment.getPaymentReference());
                    wrapper.setStatus(payment.getActionStatus());
                    cdm.setLatestPaymentDetailsWrapper(wrapper);
                }
                else{
                    cdm.setLatestPaymentDetailsWrapper(null);
                }
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(cdm);

            }else{
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("No client with ID :"+companyId+ " Found");
            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
