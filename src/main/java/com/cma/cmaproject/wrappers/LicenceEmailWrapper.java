package com.cma.cmaproject.wrappers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class LicenceEmailWrapper {
    private String from;
    private String to;
    private String subject;
    private String content;
    private Date date = new Date();

}
