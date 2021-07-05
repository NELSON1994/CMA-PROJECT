package com.cma.cmaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="PROCEDURE_EXECUTION")
public class ProcedureExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROCEDURE_EXECUTION_ID")
    private Long id;

    @Column(name = "OBSERVATION")
    private String Observation;

    @Column(name = "IS_ISSUE_NOTED")
    private String isIssueNoted;

    @Column(name = "IS_PROCEDURE_PREPARED")
    private String isProcedurePrepared;

    @Column(name = "RISK_RATING")
    private String riskRating;  // impact* likelyhood

    @Column(name = "SCORE")
    private String score;

    @Column(name = "INPUTTER_NAME")
    private String inputterName;

    @Column(name = "REVIEWER_NAME")
    private String reviewerName;

    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Column(name = "IN_TRASH")
    private String intrash;

    @Column(name = "CREATION_DATE")
    private Date insertionDate= new Date();

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROCEDURE_ID",referencedColumnName="PROCEDURE_ID")
    @JsonIgnore
    private Procedures procedures;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EVIDENCE_FILE_ID",referencedColumnName="EVIDENCE_FILE_ID")
    @JsonIgnore
    private EvidenceFileUpload evidenceFileUpload;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RISK_MATRIX_ID",referencedColumnName="RISK_MATRIX_ID")
    @JsonIgnore
    private RiskMatrix riskMatrix;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROCEDURE_ASSESSMENT_ID",referencedColumnName="PROCEDURE_ASSESSMENT_ID")
    @JsonIgnore
    private ProcedureAssessment procedureAssessment;
//=========================added now
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CLIENT_ID",referencedColumnName="COMPANY_ID")
    @JsonIgnore
    private Company company;
}
