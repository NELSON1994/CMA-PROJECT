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
@Table(name="PROCEDURES")
public class Procedures {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROCEDURE_ID")
    private Long id;

    @Column(name = "PROCEDURE_NAME")
    private String procedureName;
    @Column(name = "PROCEDURE_DESCRIPTION")
    private String procedureDescription;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "control_id",referencedColumnName="CONTROL_ID")
    @JsonIgnore
    private Controls controls;


}
