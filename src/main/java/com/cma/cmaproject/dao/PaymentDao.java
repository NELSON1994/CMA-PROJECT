package com.cma.cmaproject.wrappers;

import lombok.Data;

@Data
public class PaymentDao {
    private String paymentMode;
    private String emailAddress;
    private String paymentReference;
    private int amount;
}
