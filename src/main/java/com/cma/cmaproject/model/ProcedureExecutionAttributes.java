package com.cma.cmaproject.model;

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
@Table(name="PROCEDURE_EXECUTION_ATTRIBUTES")
public class ProcedureExecutionAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ATTRIBUTE_ID")
    private Long id;

    @Column(name = "ATTRIBUTE_NAME")
    private String name;

    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Column(name = "IN_TRASH")
    private String intrash;

    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();

}
