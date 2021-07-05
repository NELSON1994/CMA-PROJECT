package com.cma.cmaproject.wrappers;

import lombok.Data;

import java.util.Date;

@Data
public class LatestPaymentDetailsWrapper {
    private String paymentMode;
    private String emailAddress;
    private String paymentReference;
    private int amount;
    private String status;
    private Date paymentDate;
}
