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
@Table(name="RISK_PROFILES")
@Data
public class RiskProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RISK_PROFILE_ID")
    private Long id;

    @Column(name = "ASSESER_NAME")
    private String accesserName;

    @Column(name = "ASSESERS_COMMENTS")
    private String assesersComments;

    @Column(name = "REVIEWER_NAME")
    private String reviewerName;

    @Column(name = "REVIEWERS_COMMENTS")
    private String reviewersComments;

    @Column(name = "ASSESSMENT_DATE")
    private Date assessmentDate;

    @Column(name = "REVIEWING_DATE")
    private Date reviewingDate= new Date();

    @Column(name = "ASSESSMENT_STATUS")
    private String assessmentStatus;

    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Column(name = "IN_TRASH")
    private String intrash;

    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "company_id",referencedColumnName="COMPANY_ID")
    @JsonIgnore
    private Company company;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "overal_risk_id",referencedColumnName="OVERAL_RISK_ID")
    @JsonIgnore
    private OveralRiskDescription overalRiskDescription;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "impact_id",referencedColumnName="IMPACT_ID")
    @JsonIgnore
    private Impacts impacts;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "likelyhood_id",referencedColumnName="LIKELYHOOD_ID")
     @JsonIgnore
    private Likelyhood likelyhood;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "procedure_id",referencedColumnName="PROCEDURE_ID")
    @JsonIgnore
    private Procedures procedures;
}
