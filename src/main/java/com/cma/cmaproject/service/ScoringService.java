package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.model.*;
import com.cma.cmaproject.repository.*;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScoringService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScoringService.class.getName());

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private ProceduresRepository proceduresRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaturityLevelRepository maturityLevelRepository;

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
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper scoresForControls(Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            //Get list of controls
            List<Controls> controls = controlRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.actionApproved);
            logger.info("NUMBER OF CONTROLS : {}", controls.size());
            //for each control > get list of procedures and there scores
            int totalScore = 0;
            if (controls.size() > 0) {
                List<ControlScores> controlScoresList = new ArrayList<>();
                for (Controls controls1 : controls) {
                    List<Procedures> procedures = proceduresRepository.findByControlsAndIntrash(controls1, Constants.intrashNO);
                    logger.info("NUMBER OF PROCEDURES FOR CONTROL WITH ID : {} IS : {}", controls1.getId(), procedures.size());
                    if (procedures.size() > 0) {
                        int tallyied = 0;
                        for (Procedures procedures1 : procedures) {
                            Optional<ProcedureExecution> procedureExecution = procedureExecutionRepository.findByProceduresAndIntrashAndActionStatus(procedures1, Constants.intrashNO, Constants.actionApproved);
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
                        int finalScoreForTheControl = (totalScore * 100) / allOver;
                        logger.info(" ++++++++++++++++++++++   FINAL SCORE ATTAINED FOR THE CONTROL WITH ID {} IS : {}", controls1.getId(), finalScoreForTheControl);

                        ControlScores controlScores = new ControlScores();
                        controlScores.setControls(controls1);
                        controlScores.setNumberOfPProcedures(String.valueOf(noOfSummed));
                        controlScores.setControlScore(String.valueOf(finalScoreForTheControl));
                        controlScores.setNumberOfProcedures(String.valueOf(nOfProcedures));
                        controlScores.setTotalSumOfProceduresScores(String.valueOf(totalScore));
                        controlScores.setTotalPossibleSumOfProceduresScores(String.valueOf(allOver));
                        controlScoresRepository.save(controlScores);
                        logger.info("++++++++++++++  SAVING THE SCORE FOR THE CONTROL  DONE");
                        controlScoresList.add(controlScores);

                    }

                }
                userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL PROCEDURES > CONTROL", "SUCCESS: Controls Scoring Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Scoring Successful");
                genericResponseWrapper.setData(controlScoresList);

            } else {
                userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL PROCEDURES > CONTROL", "FAILED: No Controls Found");
                genericResponseWrapper.setCode(300);
                genericResponseWrapper.setStatus("FAILED");
                genericResponseWrapper.setMessage("No Controls Found");
                genericResponseWrapper.setData(null);

            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL PROCEDURES > CONTROL", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }

        return genericResponseWrapper;
    }

    public GenericResponseWrapper scoresForDomains(Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            //Get list of domains
            List<Domain> domains = domainRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.actionApproved);
            logger.info("NUMBER OF DOMAINS : {}", domains.size());
            //for each control > get list of procedures and there scores
            int totalScore = 0;
            int totalPossibleScoreforAllControlsInDomain = 0;
            int totalScoreForAllControlsInDomain = 0;
            if (domains.size() > 0) {
                List<DomainScores> domainScoresList = new ArrayList<>();
                for (Domain domain : domains) {
                    List<Controls> controls = controlRepository.findByDomainAndIntrash(domain, Constants.intrashNO);
                    logger.info("NUMBER OF CONTROLS FOR DOMAIN WITH ID : {} IS : {}", domain.getId(), controls.size());
                    if (controls.size() > 0) {
                        for (Controls controls1 : controls) {
                            Optional<ControlScores> controlScores = controlScoresRepository.findByControls(controls1);
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
                        int score = totalScore / no;
                        logger.info(" ++++++++++++++++++++++   SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), score);

                        String tPSD = String.valueOf(totalPossibleScoreforAllControlsInDomain);
                        String tSD = String.valueOf(totalScoreForAllControlsInDomain);

                        logger.info(" ++++++++++++++++++++++  POSSIBLE SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), tPSD);
                        logger.info(" ++++++++++++++++++++++  SUM SCORE FOR THE DOMAIN WITH ID : {} IS : {}", domain.getId(), tSD);

                        DomainScores domainScores = new DomainScores();
                        domainScores.setDomain(domain);
                        domainScores.setBenchMarkScore(tPSD);
                        domainScores.setRatingMark(tSD);
                        domainScores.setDomainScore(String.valueOf(score));
                        domainScores.setNoOfcontrols(String.valueOf(no));
                        domainScoresRepository.save(domainScores);
                        logger.info("++++++++++++++  SAVING THE SCORE FOR THE DOMAIN DONE");
                        domainScoresList.add(domainScores);
                        logger.info("==============================================================\n");

                    }

                }
                userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL CONTROLS > DOMAIN", "SUCCESS: Domains Scoring Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Scoring Successful");
                genericResponseWrapper.setData(domainScoresList);

            } else {
                userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL CONTROLS > DOMAIN", "FAILED: No Domain Found");
                genericResponseWrapper.setCode(300);
                genericResponseWrapper.setStatus("FAILED");
                genericResponseWrapper.setMessage("No Domain Found");
                genericResponseWrapper.setData(null);

            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL CONTROLS > DOMAIN", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }

        return genericResponseWrapper;
    }

    public GenericResponseWrapper scoresForControls2(Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        List<ScoringControlsWrapper> list = new ArrayList<>();
        ScoringControlsWrapper scoringControlsWrapper = new ScoringControlsWrapper();
        Optional<User> user = userRepository.findByIdAndIntrash(loggedServiceProviderId, Constants.intrashNO);
        try {
            if(user.isPresent()){
                User user1=user.get();
                Company company=user1.getCompany();
                List<Controls> controlsList = controlRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.actionApproved);
                if (controlsList.size() > 0) {
                    for (Controls controls : controlsList) {
                        //  Optional<ControlScores> controlScores = controlScoresRepository.findByControls(controls);
                        Optional<ControlScores> controlScores = controlScoresRepository.findByControlsAndCompany(controls,company);
                        if (controlScores.isPresent()) {
                            ControlScores controlScores1 = controlScores.get();
                            scoringControlsWrapper.setControlScore(controlScores1.getControlScore());
                            scoringControlsWrapper.setNumberOfProcedures(controlScores1.getNumberOfProcedures());
                            scoringControlsWrapper.setTotalPossibleScoreForAllProcedures(controlScores1.getTotalPossibleSumOfProceduresScores());
                            scoringControlsWrapper.setTotalAttainedScoreForPreparedProcedures(controlScores1.getTotalSumOfProceduresScores());
                        }
                        scoringControlsWrapper.setControlName(controls.getControlName());
                        list.add(scoringControlsWrapper);
                    }
                    userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL PROCEDURES > CONTROL", "SUCCESS: Domains Scoring Successful");
                    genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                    genericResponseWrapper.setMessage("Controls Scoring Successful");
                    genericResponseWrapper.setData(list);

                } else {
                    userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL PROCEDURES > CONTROL", "FAILED: No Domain Found");
                    genericResponseWrapper.setCode(300);
                    genericResponseWrapper.setStatus("FAILED");
                    genericResponseWrapper.setMessage("No Controls Found");
                    genericResponseWrapper.setData(null);
                }
            }
            else{
                userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL PROCEDURES > CONTROL", "FAILED :No Client Found for User with ID: "+loggedServiceProviderId);
                genericResponseWrapper.setCode(404);
                genericResponseWrapper.setStatus("FAILED");
                genericResponseWrapper.setMessage("No Client Found for User with ID: "+loggedServiceProviderId);
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL PROCEDURES > CONTROL", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper scoresForDomains2(Long loggedServiceProviderId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedServiceProviderId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        List<DomainScoringWrapper> list = new ArrayList<>();
        DomainScoringWrapper wrapper = new DomainScoringWrapper();
        Optional<User> user = userRepository.findByIdAndIntrash(loggedServiceProviderId, Constants.intrashNO);
        try {
            if(user.isPresent()){
                User user1=user.get();
                Company company=user1.getCompany();
                List<Domain> domainList = domainRepository.findByIntrashAndActionStatus(Constants.intrashNO, Constants.actionApproved);
                if (domainList.size() > 0) {
                    for (Domain domain : domainList) {
                       // Optional<DomainScores> domainScores = domainScoresRepository.findByDomain(domain);
                        Optional<DomainScores> domainScores = domainScoresRepository.findByDomainAndCompany(domain,company);
                        if (domainScores.isPresent()) {
                            DomainScores domainScores1 = domainScores.get();
                            wrapper.setDomainScore(domainScores1.getDomainScore());
                            wrapper.setBenchMarkScore(domainScores1.getBenchMarkScore());
                            wrapper.setNumberOfControls(domainScores1.getNoOfcontrols());
                            wrapper.setRatingMark(domainScores1.getRatingMark());
                        }
                        wrapper.setDomainName(domain.getDomainName());
                        list.add(wrapper);
                    }
                    userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL CONTROLS > DOMAIN", "SUCCESS: Domains Scoring Successful");
                    genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                    genericResponseWrapper.setMessage("Domains Scoring Successful");
                    genericResponseWrapper.setData(list);

                } else {
                    userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL CONTROLS > DOMAIN", "FAILED: No Domain Found");
                    genericResponseWrapper.setCode(300);
                    genericResponseWrapper.setStatus("FAILED");
                    genericResponseWrapper.setMessage("No Domains Found");
                    genericResponseWrapper.setData(null);
                }

            }
            else{
                userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL CONTROLS > DOMAIN", "FAILED :No Client Found for User with ID: "+loggedServiceProviderId);
                genericResponseWrapper.setCode(404);
                genericResponseWrapper.setStatus("FAILED");
                genericResponseWrapper.setMessage("No Client Found for User with ID: "+loggedServiceProviderId);
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "SCORING ALL CONTROLS > DOMAIN", "FAILED: System Error");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper graphPresentation(Long loggedInUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<User> user = userRepository.findByIdAndIntrash(loggedInUserId, Constants.intrashNO);
        try {
            if(user.isPresent()){
                User user1=user.get();
                Company company=user1.getCompany();
                int totalScoreForAllDomains = 0;
                int totalBenchMarkScoresForAllDomains = 0;
               // List<DomainScores> list = domainScoresRepository.findAll();
                List<DomainScores> list = domainScoresRepository.findByCompany(company);
                List<DomainScoreGraphDataWrapper> scoreGraphDataWrappers = new ArrayList<>();
                for (DomainScores domainScores : list) {
                    totalScoreForAllDomains = totalScoreForAllDomains + Integer.parseInt(domainScores.getRatingMark());
                    totalBenchMarkScoresForAllDomains = totalBenchMarkScoresForAllDomains + Integer.parseInt(domainScores.getBenchMarkScore());
                    Domain domain = domainScores.getDomain();
                    DomainScoreGraphDataWrapper domainScoreGraphDataWrapper = new DomainScoreGraphDataWrapper();
                    domainScoreGraphDataWrapper.setDomainName(domain.getDomainName().toUpperCase());
                    domainScoreGraphDataWrapper.setDomainBenchMarkScore(Integer.parseInt(domainScores.getBenchMarkScore()));
                    domainScoreGraphDataWrapper.setDomainRatingScore(Integer.parseInt(domainScores.getRatingMark()));
                    domainScoreGraphDataWrapper.setDomainScoreInPercentage(Integer.parseInt(domainScores.getDomainScore()));
                    domainScoreGraphDataWrapper.setNumberOfControls(Integer.parseInt(domainScores.getNoOfcontrols()));
                    scoreGraphDataWrappers.add(domainScoreGraphDataWrapper);
                }
                //do calculations  to get overal risk maturity level
                logger.info("==================== OVERRAL BENCHMARK SCORED FOR ALL DOMAINS : {}", totalBenchMarkScoresForAllDomains);
                logger.info("==================== OVERRAL RATING SCORED FOR ALL DOMAINS : {}", totalScoreForAllDomains);
                double score = (totalScoreForAllDomains * 100) / totalBenchMarkScoresForAllDomains;
                logger.info("==================== CALCULATED SCORE FOR ALL DOMAINS : {}", score);

                Long a = Math.round(score);
                int aa = a.intValue();
                logger.info("==================== CALCULATED SCORE FOR ALL DOMAINS a222 : {}", aa);
                FinalOveralScoreData finalOveralScoreData = new FinalOveralScoreData();
                List<MaturityLevel> maturityLevels = maturityLevelRepository.findByIntrashAndUpperRangeGreaterThanEqual(Constants.intrashNO, aa);
                if (maturityLevels.size() > 0) {
                    MaturityLevel maturityLevel = maturityLevels.get(0);
                    finalOveralScoreData.setMaturityLevel(maturityLevel.getOveralReadinessLevel());
                    finalOveralScoreData.setMaturityLevelDescription(maturityLevel.getDescription());
                    finalOveralScoreData.setMaturityLevelName(maturityLevel.getName());
                    finalOveralScoreData.setMaturityLowerRange(maturityLevel.getLowerRange());
                    finalOveralScoreData.setMaturityUpperRange(maturityLevel.getUpperRange());
                }
                finalOveralScoreData.setFinalScore(aa);
                finalOveralScoreData.setDomainScoreGraphDataWrapperList(scoreGraphDataWrappers);

                userServiceTemplate.saveAuditRails(name, company8, "GRAPH PRESENTATION", "SUCCESS: Successful");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(finalOveralScoreData);
            }
            else{
                userServiceTemplate.saveAuditRails(name, company8, "GRAPH PRESENTATION", "FAILED :No Client Found for User with ID: "+loggedInUserId);
                genericResponseWrapper.setCode(404);
                genericResponseWrapper.setStatus("FAILED");
                genericResponseWrapper.setMessage("No Client Found for User with ID: "+loggedInUserId);
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            userServiceTemplate.saveAuditRails(name, company8, "GRAPH PRESENTATION", "FAILED: System Error");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

}
