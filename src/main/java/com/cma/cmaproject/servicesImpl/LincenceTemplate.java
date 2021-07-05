package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import com.cma.cmaproject.dao.LicenceDao;

public interface LincenceTemplate {
    // generate linence
    GenericResponseWrapper generateLincense(LicenceDao lwrap, Long companyId, Long loggedServiceProviderId);

    //renew licence
    GenericResponseWrapper renewLicence(Long companyId, Long loggedServiceProviderId);

    //activate lincense
    GenericResponseWrapper activateLicence(Long userId);

    //deactivate lincense
    GenericResponseWrapper deactivateLicence(Long companyId, Long loggedServiceProviderId);

    //view all licences
    GenericResponseWrapper viewAllLicences(Long loggedServiceProviderId);

    //view individual licence
    GenericResponseWrapper viewIndividualLicence(Long licenceId, Long loggedServiceProviderId);
}
