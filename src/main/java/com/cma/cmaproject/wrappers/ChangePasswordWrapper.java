package com.cma.cmaproject.wrappers;

import lombok.Data;

@Data
public class ChangePasswordWrapper {
    private String username;
    private String newPassword;
    private String confirmPassword;
}
