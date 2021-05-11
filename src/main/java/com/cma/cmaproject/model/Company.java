package com.cma.cmaproject.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="COMPANY")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMPANY_ID")
    private Long id;
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
}
