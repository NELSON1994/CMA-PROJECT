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
@Table(name="PROCEDURE_ASSESSMENT")
public class ProcedureAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROCEDURE_ASSESSMENT_ID")
    private Long id;
    @Column(name = "PROCEDURE_ASSESSMENT_NAME")
    private String assessmentName;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "START_DATE")
    private String startDate;
    @Column(name = "END_DATE")
    private String endDate;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date insertionDate= new Date();
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "CLIENT_ID",referencedColumnName="COMPANY_ID")
    @JsonIgnore
    private Company company;

}
