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
@Table(name="IMPACTS")
public class Impacts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IMPACT_ID")
    private Long id;

    @Column(name = "IMPACT_NAME")
    private String impactName;

    @Column(name = "IMPACT_RATING")
    private int rating;

    @Column(name = "CRITERIA")
    private String[] criteria;

    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Column(name = "IN_TRASH")
    private String intrash;

    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
}
