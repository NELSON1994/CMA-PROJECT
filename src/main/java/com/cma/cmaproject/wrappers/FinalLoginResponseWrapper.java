package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.User;
import lombok.Data;

@Data
public class FinalLoginResponseWrapper {
        private User user;
        private String userRole;

}
