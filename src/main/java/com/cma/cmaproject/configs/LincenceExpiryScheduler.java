package com.cma.cmaproject.configs;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.Licence;
import com.cma.cmaproject.repository.LincenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class LincenceExpiryScheduler {

    @Autowired
    private LincenceRepository lincenceRepository;

    // at 12:00 AM every day
    @Scheduled(cron="0 0 0 * * ?")
    public void licenceExpiry() {
        System.out.println("******=> VALIDATING L.EXPIRY <=******");
        List<Licence> activeLicences=lincenceRepository.findByIntrashAndActionStatus(Constants.intrashNO,Constants.licenceStatus2);
        Date date=new Date();
        for(Licence licence:activeLicences){
        Date exp=licence.getLincenceExpiryDate();
            if(date.equals(exp)){
                licence.setActionStatus(Constants.licenceStatus4);
            }
            lincenceRepository.save(licence);

        }

    }
}
