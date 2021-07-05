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
@Table(name="LIKELYHOOD")
public class Likelyhood {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LIKELYHOOD_ID")
    private Long id;
    @Column(name = "LIKELYHOOD")
    private String likelyhood;
    @Column(name = "RATING")
    private int rating;
    @Column(name = "PROBABILITY")
    private String probability;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
}
