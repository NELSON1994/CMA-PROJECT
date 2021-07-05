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
@Table(name="OVERAL_RISK_DESCRIPTION")
public class OveralRiskDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "OVERAL_RISK_ID")
    private Long id;
    @Column(name = "RISK_NAME")
    private String riskName;
    @Column(name = "RISK_DESCRIPTION")
    private String riskDescription;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate= new Date();

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "procedure_id",referencedColumnName="PROCEDURE_ID")
    @JsonIgnore
    private Procedures procedures;

}
