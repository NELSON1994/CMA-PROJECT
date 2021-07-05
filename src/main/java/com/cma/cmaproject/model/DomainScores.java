package com.cma.cmaproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="DOMAINS_SCORES")
@Data
public class DomainScores {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DOMAIN_SCORE_ID")
    private Long id;

    @Column(name = "SCORE_PERCENTAGE")
    private String domainScore;

    @Column(name = "NO_OF_CONTROLS")
    private String noOfcontrols;

    @Column(name = "BENCH_MARK_SCORE")
    private String benchMarkScore;

    @Column(name = "RATING_MARK")
    private String ratingMark;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "DOMAIN_ID",referencedColumnName="DOMAIN_ID")
    private Domain domain;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "CLIENT_ID",referencedColumnName="COMPANY_ID")
    private Company company;
}
