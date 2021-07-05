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
@Table(name="EVIDENCE_FILES")
public class EvidenceFileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVIDENCE_FILE_ID")
    private Long id;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_DOWNLOAD_URI")
    private String fileDownloadUri;
    @Column(name = "FILE_TYPE")
    private String fileType;
    @Column(name = "FILE_SIZE")
    private long fileSize;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "IN_TRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    private Date creationDate= new Date();
}
