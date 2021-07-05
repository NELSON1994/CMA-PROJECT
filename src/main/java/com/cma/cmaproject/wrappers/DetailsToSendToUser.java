package com.cma.cmaproject.wrappers;

import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
public class DetailsToSendToUser {
    private String userName;
    private String password;
    private String role;
    private String company;
    private Date licenceActiveDate;
    private Date licenceExpiryDate;
    private String licenceTrackingReference;

}
