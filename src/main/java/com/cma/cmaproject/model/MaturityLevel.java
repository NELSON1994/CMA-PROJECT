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
@Table(name="MATURITY_LEVEL")
public class MaturityLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MATURITYLEVEL_ID")
    private Long id;

    @Column(name = "OVERAL_READINESS_LEVEL")
    private String overalReadinessLevel;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LOWER_RANGE")
    private int lowerRange;

    @Column(name = "UPPER_RANGE")
    private int upperRange;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Column(name = "IN_TRASH")
    private String intrash;

    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
}
