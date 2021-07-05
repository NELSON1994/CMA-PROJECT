package com.cma.cmaproject.wrappers;

import com.cma.cmaproject.model.Company;
import com.cma.cmaproject.model.Licence;
import com.cma.cmaproject.model.Roles;
import com.cma.cmaproject.model.User;
import lombok.Data;

@Data
public class GeneratedUserWrapper {
    private User user;
    private Roles role;
    private Licence licence;
    private Company company;
    private DetailsToSendToUser detailsToSendToUser;

}
