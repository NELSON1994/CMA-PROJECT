package com.cma.cmaproject.wrappers;

import lombok.Data;

@Data
public class JwtWrapper {
    private String username;
    private String token;
    private String tokenVality;
}
