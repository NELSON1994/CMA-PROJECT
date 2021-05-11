package com.cma.cmaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="PAYMENTS_DETAILS")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PAYMENT_ID")
    private Long id;
    @Column(name = "PAYMENT_MODE")
    private String paymentMode;
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;
    @Column(name = "PAYMENT_REFERENCE")
    private String paymentReference;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "ACTION_STATUS")//verified,not verified
    private String actionStatus;
    @Column(name = "PAYMENT_REFERENCE_CODE")
    private String paymentReferenceCode;// to be generated after payment is successful
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date insertionDate= new Date();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "order_id",referencedColumnName="ORDER_ID")
    @JsonIgnore
    private CustomerOrders customerOrders;
}
