package com.cma.cmaproject.servicesImpl;

import com.cma.cmaproject.model.Licence;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;

public interface LincenceTemplate {
    // generate linence
    GenericResponseWrapper generateLincense(Licence licence, Long orderId);

    //renew licence
    GenericResponseWrapper renewLicence(Long licenceId);

    //activate lincense
   // GenericResponseWrapper activateLicence(Long userId);

    //activate lincense
    GenericResponseWrapper activateLicence(Long userId, Long[] linenceIds);

    //deactivate lincense
    GenericResponseWrapper deactivateLicence(Long licenceId);

    //view all licences
    GenericResponseWrapper viewAllLicences();

    //view individual licence
    GenericResponseWrapper viewIndividualLicence(Long licenceId);
}
