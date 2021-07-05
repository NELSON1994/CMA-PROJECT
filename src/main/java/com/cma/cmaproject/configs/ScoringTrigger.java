package com.cma.cmaproject.configs;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.*;
import com.cma.cmaproject.repository.*;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ScoringTrigger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScoringTrigger.class.getName());

    @Autowired
    private ProceduresRepository proceduresRepository;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ControlScoresRepository controlScoresRepository;

    @Autowired
    private ProcedureExecutionRepository procedureExecutionRepository;

    @Autowired
    private DomainScoresRepository domainScoresRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Scheduled(cron = "${cronScoring2}")
    public void scoreControls() {
        try {
            // list of clients
            List<Company> companies=companyRepository.findByIntrash(Constants.intrashNO);
            logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            for(Company company: companies){
                //Get list of controls
                List<Controls> controls = controlRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.actionApproved);
                logger.info("+++++++++++++++++++++++++++++++++++++++   NUMBER OF CONTROLS : {}", controls.size());
                int totalScore = 0;
                if (controls.size() > 0) {
                    List<ControlScores> controlScoresList = new ArrayList<>();
                    for (Controls controls1 : controls) {
                        int finalScoreForTheControl = 0;
                        List<Procedures> procedures = proceduresRepository.findByControlsAndIntrash(controls1, Constants.intrashNO);
                        logger.info("+++++++++ NUMBER OF PROCEDURES FOR CONTROL WITH ID : {} IS : {}", controls1.getId(), procedures.size());
                        if (procedures.size() > 0) {
                            int tallyied = 0;
                            for (Procedures procedures1 : procedures) {
                                Optional<ProcedureExecution> procedureExecution = procedureExecutionRepository.findByProceduresAndIntrashAndActionStatusAndCompany(procedures1, Constants.intrashNO, Constants.actionApproved,company);
                                if (procedureExecution.isPresent()) {
                                    ProcedureExecution procedureExecution1 = procedureExecution.get();
                                    String score = procedureExecution1.getScore();
                                    int sc = Integer.parseInt(score);
                                    tallyied = tallyied + 1;
                                    totalScore = totalScore + sc;
                                }

                            }
                            int nOfProcedures = procedures.size();
                            int noOfSummed = tallyied;
                            logger.info(" ++++++++++++++++++++++   NO OF PROCEDURE IN DB : {}", nOfProcedures);
                            logger.info(" ++++++++++++++++++++++   NO OF PROCEDURE IN SUMMED : {}", noOfSummed);
                            logger.info(" ++++++++++++++++++++++   SUM OF PROCEDURE SCORES : {}", totalScore);
                            // do computations and save data in the db
                            int allOver = nOfProcedures * 10;
                            logger.info(" ++++++++++++++++++++++   POSSIBLE ALL SCORE FOR THE CONTROL : {}", allOver);
                            finalScoreForTheControl = (totalScore * 100) / allOver;
                            logger.info(" ++++++++++++++++++++++   FINAL SCORE ATTAINED FOR THE CONTROL WITH ID {} IS : {}", controls1.getId(), finalScoreForTheControl);

                            Optional<ControlScores> controlScores1=controlScoresRepository.findByControlsAndCompany(controls1,company);
                            if(controlScores1.isPresent()){
                                ControlScores controlScores=controlScores1.get();
                                controlScores.setNumberOfPProcedures(String.valueOf(noOfSummed));
                                controlScores.setControlScore(String.valueOf(finalScoreForTheControl));
                                controlScores.setNumberOfProcedures(String.valueOf(nOfProcedures));
                                controlScores.setTotalSumOfProceduresScores(String.valueOf(totalScore));
                                controlScores.setTotalPossibleSumOfProceduresScores(String.valueOf(allOver));
                                controlScores.setCompany(company);// added for test
                                controlScoresRepository.save(controlScores);
                            }
                            else{

                                ControlScores controlScores = new ControlScores();
                                controlScores.setControls(controls1);
                                controlScores.setCompany(company);// added for test
                                controlScores.setNumberOfPProcedures(String.valueOf(noOfSummed));
                                controlScores.setControlScore(String.valueOf(finalScoreForTheControl));
                                controlScores.setNumberOfProcedures(String.valueOf(nOfProcedures));
                                controlScores.setTotalSumOfProceduresScores(String.valueOf(totalScore));
                                controlScores.setTotalPossibleSumOfProceduresScores(String.valueOf(allOver));
                                controlScoresRepository.save(controlScores);
                            }
                        }
                        else{
                            ControlScores controlScores = new ControlScores();
                            controlScores.setControls(controls1);
                            controlScores.setCompany(company);
                            controlScores.setNumberOfPProcedures("0");
                            controlScores.setControlScore("0");
                            controlScores.setNumberOfProcedures("0");
                            controlScores.setTotalSumOfProceduresScores("0");
                            controlScores.setTotalPossibleSumOfProceduresScores("0");
                            controlScoresRepository.save(controlScores);
                        }
                        logger.info("++++++++++++++  SAVING THE SCORE FOR THE CONTROL  DONE");
                        logger.info(" +>>>>>>>>>> CONTROL SCORE FOR CONTROL WITH ID : {} IS : {}", controls1.getId(), finalScoreForTheControl);
                    }
                }
            }

            //==========================================================================================================================
//            logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            //Get list of controls
//            List<Controls> controls = controlRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.actionApproved);
//            logger.info("+++++++++++++++++++++++++++++++++++++++   NUMBER OF CONTROLS : {}", controls.size());
//            //for each control > get list of procedures and there scores
//            int totalScore = 0;
//            if (controls.size() > 0) {
//                List<ControlScores> controlScoresList = new ArrayList<>();
//                for (Controls controls1 : controls) {
//                    int finalScoreForTheControl = 0;
//                    List<Procedures> procedures = proceduresRepository.findByControlsAndIntrash(controls1, Constants.intrashNO);
//                    logger.info("+++++++++ NUMBER OF PROCEDURES FOR CONTROL WITH ID : {} IS : {}", controls1.getId(), procedures.size());
//                    if (procedures.size() > 0) {
//                        int tallyied = 0;
//                        for (Procedures procedures1 : procedures) {
//                            Optional<ProcedureExecution> procedureExecution = procedureExecutionRepository.findByProceduresAndIntrashAndActionStatus(procedures1, Constants.intrashNO, Constants.actionApproved);
//                            if (procedureExecution.isPresent()) {
//                                ProcedureExecution procedureExecution1 = procedureExecution.get();
//                                ProcedureAssessment procedureAssessment=procedureExecution1.getProcedureAssessment();
//                                Company company=procedureAssessment.getCompany();
//                                String score = procedureExecution1.getScore();
//                                int sc = Integer.parseInt(score);
//                                tallyied = tallyied + 1;
//                                totalScore = totalScore + sc;
//                            }
//
//                        }
//                        int nOfProcedures = procedures.size();
//                        int noOfSummed = tallyied;
//                        logger.info(" ++++++++++++++++++++++   NO OF PROCEDURE IN DB : {}", nOfProcedures);
//                        logger.info(" ++++++++++++++++++++++   NO OF PROCEDURE IN SUMMED : {}", noOfSummed);
//                        logger.info(" ++++++++++++++++++++++   SUM OF PROCEDURE SCORES : {}", totalScore);
//                        // do computations and save data in the db
//                        int allOver = nOfProcedures * 10;
//                        logger.info(" ++++++++++++++++++++++   POSSIBLE ALL SCORE FOR THE CONTROL : {}", allOver);
//                        finalScoreForTheControl = (totalScore * 100) / allOver;
//                        logger.info(" ++++++++++++++++++++++   FINAL SCORE ATTAINED FOR THE CONTROL WITH ID {} IS : {}", controls1.getId(), finalScoreForTheControl);
//
//                        Optional<ControlScores> controlScores1=controlScoresRepository.findByControls(controls1);
//                        if(controlScores1.isPresent()){
//                            ControlScores controlScores=controlScores1.get();
//                            controlScores.setNumberOfPProcedures(String.valueOf(noOfSummed));
//                            controlScores.setControlScore(String.valueOf(finalScoreForTheControl));
//                            controlScores.setNumberOfProcedures(String.valueOf(nOfProcedures));
//                            controlScores.setTotalSumOfProceduresScores(String.valueOf(totalScore));
//                            controlScores.setTotalPossibleSumOfProceduresScores(String.valueOf(allOver));
//                            controlScoresRepository.save(controlScores);
//                        }
//                        else{
//
//                            ControlScores controlScores = new ControlScores();
//                            controlScores.setControls(controls1);
//                            controlScores.setNumberOfPProcedures(String.valueOf(noOfSummed));
//                            controlScores.setControlScore(String.valueOf(finalScoreForTheControl));
//                            controlScores.setNumberOfProcedures(String.valueOf(nOfProcedures));
//                            controlScores.setTotalSumOfProceduresScores(String.valueOf(totalScore));
//                            controlScores.setTotalPossibleSumOfProceduresScores(String.valueOf(allOver));
//                            controlScoresRepository.save(controlScores);
//                        }
//                    }
//                    else{
//                        ControlScores controlScores = new ControlScores();
//                        controlScores.setControls(controls1);
//                        controlScores.setNumberOfPProcedures("0");
//                        controlScores.setControlScore("0");
//                        controlScores.setNumberOfProcedures("0");
//                        controlScores.setTotalSumOfProceduresScores("0");
//                        controlScores.setTotalPossibleSumOfProceduresScores("0");
//                        controlScoresRepository.save(controlScores);
//                    }
//                    logger.info("++++++++++++++  SAVING THE SCORE FOR THE CONTROL  DONE");
//                    logger.info(" +>>>>>>>>>> CONTROL SCORE FOR CONTROL WITH ID : {} IS : {}", controls1.getId(), finalScoreForTheControl);
//                }
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Scheduled(cron = "${cronScoring1}")
    public void scoreDomains() {
        try {
            List<Domain> domains = domainRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.actionApproved);
            logger.info("++++++++    NUMBER OF DOMAINS : {}", domains.size());
            int totalScore = 0;
            int totalPossibleScoreforAllControlsInDomain = 0;
            int totalScoreForAllControlsInDomain = 0;
            //======================================================================================
            List<Company> companies=companyRepository.findByIntrash(Constants.intrashNO);
            logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            for(Company company: companies){
                if (domains.size() > 0) {
                    for (Domain domain : domains) {
                        int score2 = 0;
                        List<Controls> controls = controlRepository.findByDomainAndIntrash(domain, Constants.intrashNO);
                        logger.info("++++++++  NUMBER OF CONTROLS FOR DOMAIN WITH ID : {} IS : {}", domain.getId(), controls.size());
                        if (controls.size() > 0) {
                            for (Controls controls1 : controls) {
                                Optional<ControlScores> controlScores = controlScoresRepository.findByControlsAndCompany(controls1,company);
                                if (controlScores.isPresent()) {
                                    ControlScores controlScores1 = controlScores.get();
                                    String score = controlScores1.getControlScore();
                                    int sc = Integer.parseInt(score);
                                    totalScore = totalScore + sc;
                                    totalPossibleScoreforAllControlsInDomain = totalPossibleScoreforAllControlsInDomain + Integer.parseInt(controlScores1.getTotalPossibleSumOfProceduresScores());
                                    totalScoreForAllControlsInDomain = totalScoreForAllControlsInDomain + Integer.parseInt(controlScores1.getTotalSumOfProceduresScores());
                                }

                            }
                            logger.info(" ++++++++++++++++++++++   SUM OF CONTROLS SCORES : {}", totalScore);
                            // do computations and save data in the db
                            int no = controls.size();
                            logger.info(" ++++++++++++++++++++++   NUMBER OF CONTROLS IN THE DOMAIN : {}", no);
                            score2 = totalScore / no;
                            logger.info(" ++++++++++++++++++++++   SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), score2);

                            String tPSD = String.valueOf(totalPossibleScoreforAllControlsInDomain);
                            String tSD = String.valueOf(totalScoreForAllControlsInDomain);

                            logger.info(" ++++++++++++++++++++++  POSSIBLE SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), tPSD);
                            logger.info(" ++++++++++++++++++++++  SUM SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), tSD);


                            Optional<DomainScores> domainScores1=domainScoresRepository.findByDomainAndCompany(domain,company);
                            if(domainScores1.isPresent()){
                                DomainScores domainScores=domainScores1.get();
                                domainScores.setCompany(company);
                                domainScores.setBenchMarkScore(tPSD);
                                domainScores.setRatingMark(tSD);
                                domainScores.setDomainScore(String.valueOf(score2));
                                domainScores.setNoOfcontrols(String.valueOf(no));
                                domainScoresRepository.save(domainScores);
                            }
                            else{
                                DomainScores domainScores = new DomainScores();
                                domainScores.setCompany(company);
                                domainScores.setDomain(domain);
                                domainScores.setBenchMarkScore(tPSD);
                                domainScores.setRatingMark(tSD);
                                domainScores.setDomainScore(String.valueOf(score2));
                                domainScores.setNoOfcontrols(String.valueOf(no));
                                domainScoresRepository.save(domainScores);
                            }

                            logger.info("++++++++++++++  SAVING THE SCORE FOR THE DOMAIN DONE");
                        }
                        else{
                            Optional<DomainScores> domainScores1=domainScoresRepository.findByDomain(domain);
                            if(!domainScores1.isPresent()){
                                DomainScores domainScores=new DomainScores();
                                domainScores.setCompany(company);
                                domainScores.setBenchMarkScore("0");
                                domainScores.setRatingMark("0");
                                domainScores.setDomainScore("0");
                                domainScores.setNoOfcontrols("0");
                                domainScoresRepository.save(domainScores);
                            }
                        }
                        logger.info(" +>>>>>>>>>> DOMAIN SCORE FOR DOMAIN WITH ID : {} IS : {} FOR CLIENT NAME : {}", domain.getId(), score2,company.getCompanyName());
                    }
                }
            }
            //==================================================================================

//            if (domains.size() > 0) {
//                for (Domain domain : domains) {
//                    int score2 = 0;
//                    List<Controls> controls = controlRepository.findByDomainAndIntrash(domain, Constants.intrashNO);
//                    logger.info("++++++++  NUMBER OF CONTROLS FOR DOMAIN WITH ID : {} IS : {}", domain.getId(), controls.size());
//                    if (controls.size() > 0) {
//                        for (Controls controls1 : controls) {
//                            Optional<ControlScores> controlScores = controlScoresRepository.findByControls(controls1);
//                            if (controlScores.isPresent()) {
//                                ControlScores controlScores1 = controlScores.get();
//                                String score = controlScores1.getControlScore();
//                                int sc = Integer.parseInt(score);
//                                totalScore = totalScore + sc;
//                                totalPossibleScoreforAllControlsInDomain = totalPossibleScoreforAllControlsInDomain + Integer.parseInt(controlScores1.getTotalPossibleSumOfProceduresScores());
//                                totalScoreForAllControlsInDomain = totalScoreForAllControlsInDomain + Integer.parseInt(controlScores1.getTotalSumOfProceduresScores());
//                            }
//
//                        }
//                        logger.info(" ++++++++++++++++++++++   SUM OF CONTROLS SCORES : {}", totalScore);
//                        // do computations and save data in the db
//                        int no = controls.size();
//                        logger.info(" ++++++++++++++++++++++   NUMBER OF CONTROLS IN THE DOMAIN : {}", no);
//                        score2 = totalScore / no;
//                        logger.info(" ++++++++++++++++++++++   SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), score2);
//
//                        String tPSD = String.valueOf(totalPossibleScoreforAllControlsInDomain);
//                        String tSD = String.valueOf(totalScoreForAllControlsInDomain);
//
//                        logger.info(" ++++++++++++++++++++++  POSSIBLE SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), tPSD);
//                        logger.info(" ++++++++++++++++++++++  SUM SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), tSD);
//
//
//                        Optional<DomainScores> domainScores1=domainScoresRepository.findByDomain(domain);
//                        if(domainScores1.isPresent()){
//                            DomainScores domainScores=domainScores1.get();
//                            domainScores.setBenchMarkScore(tPSD);
//                            domainScores.setRatingMark(tSD);
//                            domainScores.setDomainScore(String.valueOf(score2));
//                            domainScores.setNoOfcontrols(String.valueOf(no));
//                            domainScoresRepository.save(domainScores);
//                        }
//                        else{
//                            DomainScores domainScores = new DomainScores();
//                            domainScores.setDomain(domain);
//                            domainScores.setBenchMarkScore(tPSD);
//                            domainScores.setRatingMark(tSD);
//                            domainScores.setDomainScore(String.valueOf(score2));
//                            domainScores.setNoOfcontrols(String.valueOf(no));
//                            domainScoresRepository.save(domainScores);
//                        }
//
//                        logger.info("++++++++++++++  SAVING THE SCORE FOR THE DOMAIN DONE");
//                    }
//                    else{
//                        Optional<DomainScores> domainScores1=domainScoresRepository.findByDomain(domain);
//                        if(!domainScores1.isPresent()){
//                            DomainScores domainScores=new DomainScores();
//                            domainScores.setBenchMarkScore("0");
//                            domainScores.setRatingMark("0");
//                            domainScores.setDomainScore("0");
//                            domainScores.setNoOfcontrols("0");
//                            domainScoresRepository.save(domainScores);
//                        }
//                    }
//                    logger.info(" +>>>>>>>>>> DOMAIN SCORE FOR DOMAIN WITH ID : {} IS : {}", domain.getId(), score2);
//                }
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
