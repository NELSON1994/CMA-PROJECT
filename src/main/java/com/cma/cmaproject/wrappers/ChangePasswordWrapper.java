package com.cma.cmaproject.wrappers;

import lombok.Data;

@Data
public class ChangePasswordWrapper {
    private String username;
    private String oldPassword;
    private String newPassword;
}
