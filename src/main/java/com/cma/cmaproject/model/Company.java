package com.cma.cmaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="COMPANY")
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMPANY_ID")
    private Long id;
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "INDUSTRY")
    private String industry;
    @Column(name = "NO_OF_EMPLOYEES")
    private int numberOfEmployees;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();

}
