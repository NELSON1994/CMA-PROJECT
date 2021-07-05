package com.cma.cmaproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="PAYMENT_MODE")
public class PaymentMode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PAYMENT_MODE_ID")
    private Long id;
    @Column(name = "PAYMENT_MODE_NAME")
    private String modeName;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date insertionDate= new Date();
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate= new Date();
}
