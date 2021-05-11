package com.cma.cmaproject.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="AUDIT_LOGS")
public class AuditLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOG_ID")
    private Long id;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTIVITY_TYPE",updatable = false)
    private String activityType;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "USER")
    private String user;
    @Column(name = "CREATION_DATE",updatable = false)
    private Date insertionDate= new Date();
}
