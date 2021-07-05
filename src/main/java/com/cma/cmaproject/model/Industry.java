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
@Table(name="INDUSTRY")
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "INDUSTRY_ID")
    private Long id;
    @Column(name = "INDUSTRY_NAME")
    private String industryName;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date insertionDate= new Date();
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate= new Date();
}
