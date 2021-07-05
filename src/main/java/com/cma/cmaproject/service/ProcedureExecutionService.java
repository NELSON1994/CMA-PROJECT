package com.cma.cmaproject.service;

import com.cma.cmaproject.configs.FileStorageProperties;
import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.ProcedureExecutionDao;
import com.cma.cmaproject.dao.ReviewerDao;
import com.cma.cmaproject.exceptions.FileStorageException;
import com.cma.cmaproject.model.*;
import com.cma.cmaproject.repository.*;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedureExecutionService {
    @Autowired
    private ProcedureAssessmentRepository procedureAssessmentRepository;

    @Autowired
    private EvidenceFileRepository evidenceFileRepository;

    @Autowired
    private ProcedureExecutionRepository procedureExecutionRepository;

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private ProceduresRepository proceduresRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    @Autowired
    private OveralRiskDescripRepository overalRiskDescripRepository;

    @Autowired
    private RiskMatrixRepository riskMatrixRepository;

    @Autowired
    private RiskProfileRepository riskProfileRepository;

    @Autowired
    private ProcedureExecutionAttributesRepo procedureExecutionAttributesRepo;

    private final Path fileStorageLocation;

    @Autowired
    public ProcedureExecutionService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public GenericResponseWrapper executeProcedure(Long loggedInUserId, ProcedureExecutionDao procedureExecutionDao, Long procedureId,Long procedureAssessmentID) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        RiskMatrix riskMatrix1 = null;
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(loggedInUserId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                String usernames = user1.getFirstName() + " " + user1.getOtherName();
                Optional<Procedures> procedures = proceduresRepository.findByIdAndIntrash(procedureId, Constants.intrashNO);
                if (procedures.isPresent()) {
                    //changed now
                    Optional<ProcedureAssessment> procedureAssessment=procedureAssessmentRepository.findByIdAndIntrash(procedureAssessmentID,Constants.intrashNO);
                    if(procedureAssessment.isPresent()){
                        ProcedureAssessment procedureAssessment1=procedureAssessment.get();
                        Procedures procedures1 = procedures.get();
                        Company company1=procedureAssessment1.getCompany();
                        ProcedureExecution procedureExecution = new ProcedureExecution();
                        // check impacts and likelyhood
                        RiskProfile riskProfile = riskProfileRepository.findByProceduresAndIntrash(procedures1, Constants.intrashNO);
                        //get impacts and likelyhood
                        Impacts impacts = riskProfile.getImpacts();
                        Likelyhood likelyhood = riskProfile.getLikelyhood();
                        //get impact value and likelyhood values
                        int impactValue = impacts.getRating();
                        int lkValue = likelyhood.getRating();
                        int finalExecRating = impactValue * lkValue;
                        System.out.println("++++++++++++++++++++ : IMPACT VAL : " + impactValue);
                        System.out.println("++++++++++++++++++++ : LIKELYHOOD VAL : " + lkValue);
                        System.out.println("++++++++++++++++++++ : IM * LK  VAL : " + finalExecRating);
                        //===================================

                        // execute Rating
                        if (procedureExecutionDao.getIsIssueNoted().equalsIgnoreCase("No")) {
                            //TODO=> PIO, UPPER LIMIT IS NEGATIVE
                            Optional<RiskMatrix> riskMatrix = riskMatrixRepository.findByIntrashAndRiskLevelUpperLimit(Constants.intrashNO, 0);
                            if (riskMatrix.isPresent()) {
                                riskMatrix1 = riskMatrix.get();
                            } else {
                                System.out.println("===========NO PIO RISK MATRIX SET ON DB");
                                //set it null
                            }
                        } else {
                            String riskR = String.valueOf(finalExecRating);
                            List<RiskMatrix> riskMatrices = riskMatrixRepository.findByIntrashAndRiskLevelUpperLimitIsGreaterThanEqual(Constants.intrashNO, finalExecRating);
                            //get len of the list
                            int lenList = riskMatrices.size();
                            System.out.println("============ size : " + lenList);
                            if (lenList > 0) {
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FOUND  RISKS");
                                riskMatrix1 = riskMatrices.get(0);
                                String rDetails = "NAME : " + riskMatrix1.getRiskMatrixName() + "\n" + "LOWER : " + riskMatrix1.getRiskLevelLowLimit() + "\n" + "UPPER : " + riskMatrix1.getRiskLevelUpperLimit() + "\n";
                                System.out.println("+++++++++++++++++++ : RISK DEFINED : " + rDetails);
                            } else {
                                // not found
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  NOT FOUND  ");
                            }
                        }
                        procedureExecution.setRiskMatrix(riskMatrix1);
                        procedureExecution.setCompany(company1);
                        procedureExecution.setProcedureAssessment(procedureAssessment1);
                        procedureExecution.setInputterName(usernames);
                        procedureExecution.setRiskRating(String.valueOf(finalExecRating));
                        procedureExecution.setIsIssueNoted(procedureExecutionDao.getIsIssueNoted());
                        procedureExecution.setObservation(procedureExecutionDao.getObservation());
                        procedureExecution.setProcedures(procedures1);
                        procedureExecution.setIsProcedurePrepared(Constants.intrashNO);
                        procedureExecution.setActionStatus(Constants.riskStatus10);// executed,pending preparation
                        procedureExecution.setIntrash(Constants.intrashNO);
                        procedureExecutionRepository.save(procedureExecution);
                        userServiceTemplate.saveAuditRails(name, company8, "EXECUTE PROCEDURE", "SUCCESS: Successfull");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Procedure Executed Successfully");
                        genericResponseWrapper.setData(procedureExecution);
                    }
                    else{
                        genericResponseWrapper = customerOrderServiceTemplate.notFound();
                        genericResponseWrapper.setMessage("Procedure Assessment with Id : " + procedureAssessmentID + " NOT Found");
                        userServiceTemplate.saveAuditRails(name, company8, "EXECUTE PROCEDURE", "FAILED: Procedure Assessment with Id : " + procedureAssessmentID + " NOT Found");
                        genericResponseWrapper.setData(null);
                    }

                } else {
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("Procedure with Id : " + procedureId + " NOT Found");
                    userServiceTemplate.saveAuditRails(name, company8, "EXECUTE PROCEDURE", "FAILED: Procedure with Id : " + procedureId + " NOT Found");
                    genericResponseWrapper.setData(null);
                }
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with Id : " + loggedInUserId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "EXECUTE PROCEDURE", "FAILED: User with Id : " + loggedInUserId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "EXECUTE PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    //==========================================================
    public GenericResponseWrapper reviewExecutedProcedure(Long loggedInUserId, ReviewerDao reviewerDao, Long procedureExecutionId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedInUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        RiskMatrix riskMatrix1 = null;
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(loggedInUserId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                String usernames = user1.getFirstName() + " " + user1.getOtherName();
                Optional<ProcedureExecution> procedureExecution1 = procedureExecutionRepository.findByIdAndIntrashAndActionStatusAndIsProcedurePrepared(procedureExecutionId, Constants.intrashNO, Constants.riskStatus1,"YES");
                if (procedureExecution1.isPresent()) {
                    ProcedureExecution procedureExecution = procedureExecution1.get();
                    if (reviewerDao.getReviewerStatus() != null) {
                        // reviwer gave comment
                        if (reviewerDao.getReviewerStatus().equalsIgnoreCase("Approved")) {
                            // approved
                            procedureExecution.setActionStatus(Constants.actionApproved);
                        } else {
                            // declined
                            procedureExecution.setActionStatus(Constants.assementStatus2);
                        }
                        procedureExecution.setReviewerName(usernames);
                        procedureExecutionRepository.save(procedureExecution);
                        userServiceTemplate.saveAuditRails(name, company8, "REVIEW EXECUTED PROCEDURE", "SUCCESS: Successfull");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Procedure Prepared Reviewed Successfully");
                        genericResponseWrapper.setData(procedureExecution);
                    } else {
                        // no review done
                        userServiceTemplate.saveAuditRails(name, company8, "REVIEW EXECUTED PROCEDURE", "SUCCESS: Successfull");
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setMessage("No Review Done on the Prepared Procedure");
                        genericResponseWrapper.setData(procedureExecution);
                    }

                } else {
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("Prepared Procedure with Id : " + procedureExecutionId + " NOT Found");
                    userServiceTemplate.saveAuditRails(name, company8, "REVIEW EXECUTED PROCEDURE", "FAILED: Prepared Procedure with Id : " + procedureExecutionId + " NOT Found");
                    genericResponseWrapper.setData(null);
                }
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with Id : " + loggedInUserId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "REVIEW EXECUTED PROCEDURE", "FAILED: User with Id : " + loggedInUserId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "REVIEW EXECUTED PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper viewAll(Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();

        try {
            List<ProcedureExecution> list = procedureExecutionRepository.findByIntrash(Constants.intrashNO);
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL EXECUTED PROCEDURE", "SUCCESS: Successfully");
            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            genericResponseWrapper.setData(list);
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW ALL EXECUTED PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewIndividual(Long lkId, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<ProcedureExecution> c = procedureExecutionRepository.findByIdAndIntrash(lkId, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                userServiceTemplate.saveAuditRails(name, company8, "VIEW EXECUTED PROCEDURE", "SUCCESS: Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setData(c.get());
            } else {
                genericResponseWrapper.setMessage("Risk Matrix with Id : " + lkId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "VIEW EXECUTED PROCEDURE", "FAILED: Risk Matrix with ID : " + lkId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "VIEW EXECUTED PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper delete(Long Id, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        Optional<ProcedureExecution> c = procedureExecutionRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                ProcedureExecution procedureExecution = c.get();
                procedureExecution.setIntrash(Constants.intrashYES);
                procedureExecutionRepository.save(procedureExecution);
                userServiceTemplate.saveAuditRails(name, company8, "DELETE EXECUTED PROCEDURE", "SUCCESS: Deleted Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Deleted Succesfully");
                genericResponseWrapper.setData(procedureExecution);
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure Executed with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "DELETE EXECUTED PROCEDURE", "FAILED: Procedure Executed with ID : " + Id + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "DELETE EXECUTED PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper update(Long Id, ProcedureExecutionDao procedureExecutionDao, Long loggedUserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggedUserId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        RiskMatrix riskMatrix1 = null;
        Optional<ProcedureExecution> c = procedureExecutionRepository.findByIdAndIntrash(Id, Constants.intrashNO);
        try {
            if (c.isPresent()) {
                ProcedureExecution procedureExecution = c.get();
                Procedures procedures1 = procedureExecution.getProcedures();
                // check impacts and likelyhood
                OveralRiskDescription overalRiskDescription = overalRiskDescripRepository.findByProceduresAndIntrash(procedures1, Constants.intrashNO);
                RiskProfile riskProfile = riskProfileRepository.findByOveralRiskDescriptionAndIntrash(overalRiskDescription, Constants.intrashNO);
                Impacts impacts = riskProfile.getImpacts();
                Likelyhood likelyhood = riskProfile.getLikelyhood();
                int impactValue = impacts.getRating();
                int lkValue = likelyhood.getRating();
                int finalExecRating = impactValue * lkValue;
                System.out.println("++++++++++++++++++++ : IMPACT VAL : " + impactValue);
                System.out.println("++++++++++++++++++++ : LIKELYHOOD VAL : " + lkValue);
                System.out.println("++++++++++++++++++++ : IM * LK  VAL : " + finalExecRating);

                // execute Rating
                if (procedureExecutionDao.getIsIssueNoted().equalsIgnoreCase("No")) {
                    Optional<RiskMatrix> riskMatrix = riskMatrixRepository.findByIntrashAndRiskLevelUpperLimit(Constants.intrashNO, 0);
                    if (riskMatrix.isPresent()) {
                        riskMatrix1 = riskMatrix.get();
                    }
                } else {
                    String riskR = String.valueOf(finalExecRating);
                    List<RiskMatrix> riskMatrices = riskMatrixRepository.findByIntrashAndRiskLevelUpperLimitIsGreaterThanEqual(Constants.intrashNO,finalExecRating);
                    //get len of the list
                    int lenList = riskMatrices.size();
                    System.out.println("============ size : " + lenList);
                    if (lenList > 0) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FOUND  RISKS");
                        riskMatrix1 = riskMatrices.get(0);
                        String rDetails = "NAME : " + riskMatrix1.getRiskMatrixName() + "\n" + "LOWER : " + riskMatrix1.getRiskLevelLowLimit() + "\n" + "UPPER : " + riskMatrix1.getRiskLevelUpperLimit() + "\n";
                        System.out.println("+++++++++++++++++++ : RISK DEFINED : " + rDetails);
                    } else {
                        // not found
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  NOT FOUND  ");
                    }
                }
                procedureExecution.setRiskMatrix(riskMatrix1);
                procedureExecution.setIsIssueNoted(procedureExecutionDao.getIsIssueNoted());
                procedureExecution.setObservation(procedureExecutionDao.getObservation());
                procedureExecution.setIsProcedurePrepared(Constants.intrashNO);
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE EXECUTED PROCEDURE", "SUCCESS: Updated Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                genericResponseWrapper.setMessage("Updated Successfully");
                genericResponseWrapper.setData(procedureExecution);
            } else {
                genericResponseWrapper.setMessage("Risk Matrix with Id : " + Id + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE EXECUTED PROCEDURE", "FAILED: Procedure Executed with ID : " + Id + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE EXECUTED PROCEDURE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper attributeChecking(Long inputterId, Long procedureExecutionId, ProcedureExecutionAttributesWrapper wrapper) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(inputterId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        ProcedureExecutionAttributesCheckResponseWrapper p11 = new ProcedureExecutionAttributesCheckResponseWrapper();
        Optional<ProcedureExecution> proceduresExecution = procedureExecutionRepository.findByIdAndIntrash(procedureExecutionId, Constants.intrashNO);
        try {
            if (proceduresExecution.isPresent()) {
                ProcedureExecution procedureExecution = proceduresExecution.get();
                //get number of attributes from db
                List<ProcedureExecutionAttributes> lt = procedureExecutionAttributesRepo.findByIntrash(Constants.intrashNO);
                int len = lt.size();
                if (len > 0) {
                    //do execution
                    String[] attributesChecks = wrapper.getProcedureExecutionAttributesChecks();
                    int totalMarked = 0;
                    int score = 0;
                    for (String val : attributesChecks) {
                        if (val.equalsIgnoreCase("Yes")) {
                            totalMarked = totalMarked + 1;
                        }
                    }
                    score = (totalMarked * 10) / len;
                    String score1 = String.valueOf(score);
                    RiskMatrix riskMatrix = procedureExecution.getRiskMatrix();
                    String riskratingName = riskMatrix.getRiskMatrixName();
                    p11.setIsIssueNoted(procedureExecution.getIsIssueNoted());
                    p11.setRiskMatrixName(riskratingName);
                    p11.setRiskRating(procedureExecution.getRiskRating());
                    p11.setScore(score1);
                    if (riskratingName.equalsIgnoreCase("Critical") && score <= 4) {
                        procedureExecution.setScore(score1);
                        procedureExecution.setActionStatus(Constants.riskStatus1);// prepared ,waiting review
                        procedureExecutionRepository.save(procedureExecution);
                        userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "SUCCESS: Successfull");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Score Successfully Saved");
                        p11.setIsTheScoreSaved("YES");
                        p11.setIsProcedurePrepared(procedureExecution.getIsProcedurePrepared());
                        genericResponseWrapper.setData(p11);
                    } else if (riskratingName.equalsIgnoreCase("High") && score <= 6) {
                        procedureExecution.setScore(score1);
                        procedureExecution.setActionStatus(Constants.riskStatus1);// prepared ,waiting review
                        procedureExecutionRepository.save(procedureExecution);
                        userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "SUCCESS: Successfull");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Score Successfully Saved");
                        p11.setIsTheScoreSaved("YES");
                        p11.setIsProcedurePrepared(procedureExecution.getIsProcedurePrepared());
                        genericResponseWrapper.setData(p11);
                    } else if (riskratingName.equalsIgnoreCase("Medium") && score >= 6) {
                        procedureExecution.setScore(score1);
                        procedureExecution.setActionStatus(Constants.riskStatus1);
                        procedureExecutionRepository.save(procedureExecution);
                        userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "SUCCESS: Successfull");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Score Successfully Saved");
                        p11.setIsTheScoreSaved("YES");
                        p11.setIsProcedurePrepared(procedureExecution.getIsProcedurePrepared());
                        genericResponseWrapper.setData(p11);
                    } else if (riskratingName.equalsIgnoreCase("Low") && score >= 8) {
                        procedureExecution.setScore(score1);
                        procedureExecution.setActionStatus(Constants.riskStatus1);// prepared ,waiting review
                        procedureExecutionRepository.save(procedureExecution);
                        userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "SUCCESS: Successfull");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Score Successfully Saved");
                        p11.setIsTheScoreSaved("YES");
                        p11.setIsProcedurePrepared(procedureExecution.getIsProcedurePrepared());
                        genericResponseWrapper.setData(p11);
                    } else if (riskratingName.equalsIgnoreCase("PIO") && score >= 8) {
                        procedureExecution.setScore(score1);
                        procedureExecution.setActionStatus(Constants.riskStatus1);// prepared ,waiting review
                        procedureExecutionRepository.save(procedureExecution);
                        userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "SUCCESS: Successfull");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Score Successfully Saved");
                        p11.setIsTheScoreSaved("YES");
                        p11.setIsProcedurePrepared(procedureExecution.getIsProcedurePrepared());
                        genericResponseWrapper.setData(p11);
                    } else {
                        //do not save the score but return message to client to tick the attributes correctly
                        userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "FAILED: Kindly Tick the ATTRIBUTES Correctly");
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setMessage("Kindly Tick the Procedure ATTRIBUTES Correctly");
                        procedureExecution.setScore(score1);
                        p11.setIsTheScoreSaved("NO");
                        p11.setIsProcedurePrepared(procedureExecution.getIsProcedurePrepared());
                        genericResponseWrapper.setData(p11);
                    }

                } else {
                    //no attributes found
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("NO Procedure Execution Attributes Found");
                    userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "FAILED: NO Procedure Execution Attributes Found");
                    genericResponseWrapper.setData(null);
                }
            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure Execution with Id : " + procedureExecutionId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "FAILED: Procedure Execution with Id : " + procedureExecutionId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION ATTRIBUTES CHECK", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper uploadEvidenceFile(Long inputterId, Long procedureExecutionId, MultipartFile file) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(inputterId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        ProcedureExecutionAttributesCheckResponseWrapper p11 = new ProcedureExecutionAttributesCheckResponseWrapper();
        Optional<ProcedureExecution> proceduresExecution = procedureExecutionRepository.findByIdAndIntrash(procedureExecutionId, Constants.intrashNO);
        try {
            if (proceduresExecution.isPresent()) {
                ProcedureExecution procedureExecution = proceduresExecution.get();
                // Normalize file name
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());

                try {
                    // Check if the file's name contains invalid characters
                    if (fileName.contains("..")) {
                        userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION UPLOAD EVIDENCE", "FAILED: Kindly Tick the ATTRIBUTES Correctly");
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setMessage("Sorry! Filename contains invalid path sequence " + fileName);
                        genericResponseWrapper.setData(fileName);
                    } else {
                        // Copy file to the target location (Replacing existing file with the same name)
                        Path targetLocation = this.fileStorageLocation.resolve(fileName);
                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/downloadFile/")
                                .path(fileName)
                                .toUriString();

                        UploadFileResponse u = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
                        EvidenceFileUpload evidenceFileUpload = new EvidenceFileUpload();
                        evidenceFileUpload.setFileName(u.getFileName());
                        evidenceFileUpload.setIntrash(Constants.intrashNO);
                        evidenceFileUpload.setActionStatus(Constants.actionApproved);
                        evidenceFileUpload.setFileDownloadUri(u.getFileDownloadUri());
                        evidenceFileUpload.setFileType(u.getFileType());
                        evidenceFileUpload.setFileSize(u.getSize());
                        evidenceFileRepository.save(evidenceFileUpload);
                        procedureExecution.setEvidenceFileUpload(evidenceFileUpload);

                        userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION UPLOAD EVIDENCE", "SUCCESS: Evidence File :" + fileName + " is Successfully Uploaded and Saved");
                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                        genericResponseWrapper.setMessage("Evidence File :" + fileName + " is Successfully Uploaded and Saved");
                        genericResponseWrapper.setData(fileName);
                    }
                } catch (IOException ex) {
                    userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION UPLOAD EVIDENCE", "FAILED: Could not store file " + fileName + ". Please try again!");
                    genericResponseWrapper.setCode(300);
                    genericResponseWrapper.setMessage("Could not store file " + fileName + ". Please try again!");
                    genericResponseWrapper.setData(fileName);

                }

            } else {
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Procedure Execution with Id : " + procedureExecutionId + " NOT Found");
                userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION UPLOAD EVIDENCE", "FAILED: Procedure Execution with Id : " + procedureExecutionId + " NOT Found");
                genericResponseWrapper.setData(null);
            }

        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "PROCEDURE EXECUTION UPLOAD EVIDENCE", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }



}
