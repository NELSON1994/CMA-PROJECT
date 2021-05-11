package com.cma.cmaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="PERMISSIONS")
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERMISSION_ID")
    private Long id;
    @Column(name = "PERMISSION")
    private String permission;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date insertionDate= new Date();
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate= new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",referencedColumnName="ROLE_ID")
    @JsonIgnore
    private Roles role;
}
