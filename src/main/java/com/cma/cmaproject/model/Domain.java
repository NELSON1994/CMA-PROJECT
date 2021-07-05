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
@Table(name="DOMAINS")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DOMAIN_ID")
    private Long id;
    @Column(name = "DOMAIN_NAME")
    private String domainName;
    @Column(name = "DOMAIN_DESCRIPTION")
    private String domainDescription;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;
}
