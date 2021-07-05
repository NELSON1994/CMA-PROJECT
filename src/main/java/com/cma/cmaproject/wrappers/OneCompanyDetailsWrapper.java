package com.cma.cmaproject.wrappers;

import lombok.Data;

import java.util.Date;

@Data
public class OneCompanyDetailsWrapper {
    private String companyName;
    private String companyStatus;
    private Date companyCreationDate;
    private String licenceRef;
    private String licenceStatus;
    private Date licenceIssuedDate;
    private Date licenceActivationDate;
    private Date licenceExpiryDate;
   // private String latestPaymentStatus;
    private LatestPaymentDetailsWrapper latestPaymentDetailsWrapper;
}
