package com.cma.cmaproject.wrappers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserLoginWrapper {
    private String username;
    private String password;
}
