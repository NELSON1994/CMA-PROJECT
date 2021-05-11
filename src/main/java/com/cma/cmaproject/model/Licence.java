package com.cma.cmaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="CUSTOMER_LICENSE")
public class Licence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LICENCE_ID")
    private Long id;
    @Column(name = "LICENCE_REF")// will be used for file
    private String lincenceRef;
    @Column(name = "LICENCE_VALIDITY_DURATION")
    private int lincenceVality;
    @Column(name = "LICENCE_TRACKING_REF")
    private String lincenceTrackingRef;
    @Column(name = "LICENCE_EXPIRY_DATE")//activation date +lincense vality
    private Date lincenceExpiryDate;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;//activated,inactive,expired
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate= new Date();
    @Column(name = "ACTIVATION_DATE")
    private Date activationDate= new Date();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "order_id",referencedColumnName="ORDER_ID")
    @JsonIgnore
    private CustomerOrders customerOrders;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName="USER_ID")
    @JsonIgnore
    private User user;
}
