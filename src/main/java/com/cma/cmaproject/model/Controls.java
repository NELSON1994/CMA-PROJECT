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
@Table(name="CONTROLS")
public class Controls {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONTROL_ID")
    private Long id;
    @Column(name = "CONTROL_NAME")
    private String  controlName;
    @Column(name = "CONTROL_STATEMENT")
    private String controlStatement;
    @Column(name = "CONTROL_STANDARD")
    private String controlStandard;
    @Column(name = "CONTROL_STANDARD_CLAUSE")
    private String controlStandardClause;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "domain_id",referencedColumnName="DOMAIN_ID")
    @JsonIgnore
    private Domain domain;
}
