package com.cma.cmaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="CONTROLS_SCORES")
@Data
public class ControlScores {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONTROL_SCORE_ID")
    private Long id;
    @Column(name = "SCORE_PERCENTAGE")
    private String controlScore;
    @Column(name = "NUMBER_OF_PROCEDURES")
    private String numberOfProcedures;
    @Column(name = "NUMBER_OF_APPROVED_PREPARED_PROCEDURES")
    private String numberOfPProcedures;
    @Column(name = "TOTAL_SUM_OF_PROCEDURES_SCORES")
    private String totalSumOfProceduresScores;
    @Column(name = "TOTAL_POSSIBLE_SUM_OF_PROCEDURES_SCORES")
    private String totalPossibleSumOfProceduresScores;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "CONTROL_ID",referencedColumnName="CONTROL_ID")
    private Controls controls;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "CLIENT_ID",referencedColumnName="COMPANY_ID")
    private Company company;


}
