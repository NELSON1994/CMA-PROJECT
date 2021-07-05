package com.cma.cmaproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="RISK_MATRIX")
@Data
public class RiskMatrix {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RISK_MATRIX_ID")
    private Long id;

    @Column(name = "RISK_MATRIX_NAME")
    private String riskMatrixName;

    @Column(name = "RISK_MATRIX_LOWER_LIMIT")
    private int riskLevelLowLimit;

    @Column(name = "RISK_MATRIX_UPPER_LIMIT")
    private int riskLevelUpperLimit;

    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Column(name = "IN_TRASH")
    private String intrash;

    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
}
