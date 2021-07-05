package com.cma.cmaproject.service;

import com.cma.cmaproject.constants.Constants;
import com.cma.cmaproject.dao.RiskAssessmentDao;
import com.cma.cmaproject.dao.RiskReviewerDao;
import com.cma.cmaproject.model.*;
import com.cma.cmaproject.repository.*;
import com.cma.cmaproject.servicesImpl.CustomerOrderServiceTemplate;
import com.cma.cmaproject.servicesImpl.UserServiceTemplate;
import com.cma.cmaproject.wrappers.AuditUsernameCompanyWrapper;
import com.cma.cmaproject.wrappers.GenericResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RiskAssessmentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LincenceRepository lincenceRepository;

    @Autowired
    private OveralRiskDescripRepository overalRiskDescripRepository;

    @Autowired
    private RiskProfileRepository riskProfileRepository;

    @Autowired
    private ImpactsRepository impactsRepository;

    @Autowired
    private LikelyHoodRepository likelyHoodRepository;

    @Autowired
    private CustomerOrderServiceTemplate customerOrderServiceTemplate;

    @Autowired
    private UserServiceTemplate userServiceTemplate;

    public GenericResponseWrapper doAssessment(RiskAssessmentDao riskAssessmentDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Long uId = riskAssessmentDao.getUserId();
        Long rId = riskAssessmentDao.getRiskId();
        Long lkId = riskAssessmentDao.getLikelyhoodId();
        Long imId = riskAssessmentDao.getImpactId();
        String inputterComments = riskAssessmentDao.getInputterComments();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(uId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();

        try {
            Optional<User> user = userRepository.findByIdAndIntrash(uId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                Company company = user1.getCompany();
                Optional<Licence> licence = lincenceRepository.findLicenceByCompany(company);
                if (licence.isPresent()) {
                    Licence licence1 = licence.get();
                    if (licence1.getActionStatus().equalsIgnoreCase(Constants.licenceStatus2)) {
                        //check user status
                        if (user1.getActionStatus().equalsIgnoreCase(Constants.userActive)) {
                            // get the risk
                            Optional<OveralRiskDescription> riskDescription = overalRiskDescripRepository.findByIdAndIntrashAndActionStatus(rId, Constants.intrashNO, Constants.actionApproved);
                            if (riskDescription.isPresent()) {
                                OveralRiskDescription ov = riskDescription.get();
                                //check on impact
                                Optional<Impacts> impacts = impactsRepository.findByIdAndIntrashAndActionStatus(imId, Constants.intrashNO, Constants.actionApproved);
                                if (impacts.isPresent()) {
                                    Impacts impacts1 = impacts.get();
                                    //check likelyhood
                                    Optional<Likelyhood> likelyhood = likelyHoodRepository.findByIdAndIntrashAndActionStatus(lkId, Constants.intrashNO, Constants.actionApproved);
                                    if (likelyhood.isPresent()) {
                                        Likelyhood likelyhood1 = likelyhood.get();
                                        RiskProfile riskProfile = new RiskProfile();
                                        riskProfile.setCompany(company);
                                        riskProfile.setImpacts(impacts1);
                                        riskProfile.setLikelyhood(likelyhood1);
                                        riskProfile.setOveralRiskDescription(ov);
                                        riskProfile.setAccesserName(user1.getFirstName() + " " + user1.getOtherName());
                                        riskProfile.setActionStatus(Constants.riskStatus1);
                                        riskProfile.setIntrash(Constants.intrashNO);
                                        riskProfile.setAssesersComments(inputterComments);
                                        riskProfile.setAssessmentDate(new Date());
                                        riskProfileRepository.save(riskProfile);
                                        userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "SUCCESS: Risk Assessed Successfully");
                                        genericResponseWrapper.setData(riskProfile);
                                        genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                                        genericResponseWrapper.setMessage("Risk Profile Updated Successfully");
                                    } else {
                                        userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "FAILED: Likelyhood with ID :" + lkId + " is NOT Found");
                                        genericResponseWrapper = customerOrderServiceTemplate.notFound();
                                        genericResponseWrapper.setMessage("Likelyhood with ID :" + lkId + " is NOT Found");
                                    }
                                } else {
                                    userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "FAILED: Impact with ID :" + imId + " is NOT Found");
                                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                                    genericResponseWrapper.setMessage("Impact with ID :" + imId + " is NOT Found");
                                }
                            } else {
                                userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "FAILED: Risk with ID :" + rId + " is NOT Found");
                                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                                genericResponseWrapper.setMessage("Risk with ID :" + rId + " is NOT Found");
                            }
                        } else {
                            userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "FAILED: User Status is : " + user1.getActionStatus());
                            genericResponseWrapper.setCode(300);
                            genericResponseWrapper.setStatus("Failed");
                            genericResponseWrapper.setMessage("User Status is : " + user1.getActionStatus());
                            genericResponseWrapper.setData(licence1);
                        }
                    } else {
                        userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "FAILED: Client Licence Status is: " + licence1.getActionStatus());
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setStatus("Failed");
                        genericResponseWrapper.setMessage("Licence Status : " + licence1.getActionStatus());
                        genericResponseWrapper.setData(licence1);
                    }
                } else {
                    userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "FAILED: Client :" + company.getCompanyName() + " Has No Licence");
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("Client :" + company.getCompanyName() + " Has No Licence");
                }

            } else {
                userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "FAILED: User with id : " + uId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with id : " + uId + " NOT Found");

            }
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "RISK ASSESSMENT", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper reviewers(RiskReviewerDao riskReviewerDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Long reviewerId = riskReviewerDao.getUserId();
        Long riskId = riskReviewerDao.getRiskDetailsId();
        String rStatus = riskReviewerDao.getReviewStatus();
        String rComment = riskReviewerDao.getReviewerComments();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(reviewerId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(reviewerId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                Company company = user1.getCompany();
                Optional<Licence> licence = lincenceRepository.findLicenceByCompany(company);
                if (licence.isPresent()) {
                    Licence licence1 = licence.get();
                    if (licence1.getActionStatus().equalsIgnoreCase(Constants.licenceStatus2)) {
                        //check user status
                        if (user1.getActionStatus().equalsIgnoreCase(Constants.userActive)) {
                            //check if that risk exist
                            Optional<RiskProfile> riskProfile = riskProfileRepository.findByIdAndIntrash(riskId, Constants.intrashNO);
                            if (riskProfile.isPresent()) {
                                RiskProfile riskProfile1 = riskProfile.get();
                                riskProfile1.setAssessmentStatus(rStatus);
                                riskProfile1.setReviewingDate(new Date());
                                riskProfile1.setReviewerName(user1.getFirstName() + " " + user1.getOtherName());
                                riskProfile1.setReviewersComments(rComment);
                                riskProfile1.setActionStatus(Constants.riskStatus2);

                                riskProfileRepository.save(riskProfile1);
                                userServiceTemplate.saveAuditRails(name, company8, "REVIEW RISK ASSESSMENT", "SUCCESS: Risk Assessment Reviewed Successfully");
                                genericResponseWrapper.setData(riskProfile1);
                                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                                genericResponseWrapper.setMessage("Risk Assessment Reviewed Successfully");
                            } else {
                                userServiceTemplate.saveAuditRails(name, company8, "REVIEW RISK ASSESSMENT", "FAILED: Risk Assessment with ID : " + riskId + " NOT Found");
                                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                                genericResponseWrapper.setMessage("Risk Assessment with ID : " + riskId + " NOT Found");
                            }
                        } else {
                            userServiceTemplate.saveAuditRails(name, company8, "REVIEW RISK ASSESSMENT", "FAILED: User Status is : " + user1.getActionStatus());
                            genericResponseWrapper.setCode(300);
                            genericResponseWrapper.setStatus("Failed");
                            genericResponseWrapper.setMessage("User Status is : " + user1.getActionStatus());
                            genericResponseWrapper.setData(licence1);
                        }
                    } else {
                        userServiceTemplate.saveAuditRails(name, company8, "REVIEW RISK ASSESSMENT", "FAILED: Client Licence Status : " + licence1.getActionStatus());
                        genericResponseWrapper.setCode(300);
                        genericResponseWrapper.setStatus("Failed");
                        genericResponseWrapper.setMessage("Licence Status : " + licence1.getActionStatus());
                        genericResponseWrapper.setData(licence1);
                    }
                } else {
                    userServiceTemplate.saveAuditRails(name, company8, "REVIEW RISK ASSESSMENT", "FAILED: Client :" + company.getCompanyName() + " Has No Licence");
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("Client :" + company.getCompanyName() + " Has No Licence");
                }

            } else {
                userServiceTemplate.saveAuditRails(name, company8, "REVIEW RISK ASSESSMENT", "FAILED: User with id : " + reviewerId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with id : " + reviewerId + " NOT Found");

            }
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company8, "REVIEW RISK ASSESSMENT", "FAILED: System Malfunctioned");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;

    }

    public GenericResponseWrapper viewCompanyRiskAssessment(Long userId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(userId);
        String name = null;
        String company = null;
        name = auditUsernameCompanyWrapper.getUser();
        company = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<User> user = userRepository.findByIdAndIntrash(userId, Constants.intrashNO);
            if (user.isPresent()) {
                User user1 = user.get();
                Company company1 = user1.getCompany();
                List<RiskProfile> riskProfiles = riskProfileRepository.findByCompanyAndIntrash(company1, Constants.intrashNO);
                userServiceTemplate.saveAuditRails(name, company, "VIEW ALL RISK ASSESSMENT", "SUCCESS: Viewed Successfully");
                genericResponseWrapper.setData(riskProfiles);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            } else {
                userServiceTemplate.saveAuditRails(name, company, "VIEW ALL RISK ASSESSMENT", "FAILED: User with id : " + userId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("User with id : " + userId + " NOT Found");

            }
        } catch (Exception ex) {
            userServiceTemplate.saveAuditRails(name, company, "VIEW ALL RISK ASSESSMENT", "FAILED: System Error Occurred");
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper viewOneRiskAssessmentDetails(Long riskAssementId, Long loggeduserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggeduserId);
        String name = null;
        String company = null;
        name = auditUsernameCompanyWrapper.getUser();
        company = auditUsernameCompanyWrapper.getCompany();

        try {
            Optional<RiskProfile> riskProfile = riskProfileRepository.findByIdAndIntrash(riskAssementId, Constants.intrashNO);
            if (riskProfile.isPresent()) {
                RiskProfile riskProfile1 = riskProfile.get();
                userServiceTemplate.saveAuditRails(name, company, "VIEW INDIVIDUAL RISK ASSESSMENT", "SUCCESS: Viewed Successfully");
                genericResponseWrapper.setData(riskProfile1);
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();

            } else {
                userServiceTemplate.saveAuditRails(name, company, "VIEW INDIVIDUAL RISK ASSESSMENT", "FAILED: Risk Assessment : " + riskAssementId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Risk Assessment with id : " + riskAssementId + " NOT Found");

            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            userServiceTemplate.saveAuditRails(name, company, "VIEW INDIVIDUAL RISK ASSESSMENT", "FAILED: System Malfunctioned");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper deleteRiskAssessmentDetails(Long riskAssementId, Long loggeduserId) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(loggeduserId);
        String name = null;
        String company = null;
        name = auditUsernameCompanyWrapper.getUser();
        company = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<RiskProfile> riskProfile = riskProfileRepository.findByIdAndIntrash(riskAssementId, Constants.intrashNO);
            if (riskProfile.isPresent()) {
                RiskProfile riskProfile1 = riskProfile.get();
                riskProfile1.setIntrash(Constants.intrashYES);
                riskProfileRepository.save(riskProfile1);
                userServiceTemplate.saveAuditRails(name, company, "DELETE RISK ASSESSMENT", "SUCCESS: Deleted Successfully");
                genericResponseWrapper.setData(riskProfile1);
                genericResponseWrapper.setMessage("Deleted Successfully");
                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
            } else {
                userServiceTemplate.saveAuditRails(name, company, "DELETE RISK ASSESSMENT", "FAILED: Risk Assessment : " + riskAssementId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Risk Assessment with id : " + riskAssementId + " NOT Found");
            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            userServiceTemplate.saveAuditRails(name, company, "DELETE RISK ASSESSMENT", "FAILED: System Malfunctioned");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper updateRiskAssessmentByInputter(Long riskAssementId, RiskAssessmentDao riskAssessmentDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Long uId = riskAssessmentDao.getUserId();
        Long rId = riskAssessmentDao.getRiskId();
        Long lkId = riskAssessmentDao.getLikelyhoodId();
        Long imId = riskAssessmentDao.getImpactId();
        String asserComm = riskAssessmentDao.getInputterComments();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(uId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<RiskProfile> riskProfile = riskProfileRepository.findByIdAndIntrash(riskAssementId, Constants.intrashNO);
            if (riskProfile.isPresent()) {
                RiskProfile riskProfile1 = riskProfile.get();
                Optional<User> user = userRepository.findByIdAndIntrash(uId, Constants.intrashNO);
                if (user.isPresent()) {
                    User user1 = user.get();
                    Company company = user1.getCompany();
                    Optional<Licence> licence = lincenceRepository.findLicenceByCompany(company);
                    if (licence.isPresent()) {
                        Licence licence1 = licence.get();
                        if (licence1.getActionStatus().equalsIgnoreCase(Constants.licenceStatus2)) {
                            if (user1.getActionStatus().equalsIgnoreCase(Constants.userActive)) {
                                Optional<OveralRiskDescription> riskDescription = overalRiskDescripRepository.findByIdAndIntrashAndActionStatus(rId, Constants.intrashNO, Constants.actionApproved);
                                if (riskDescription.isPresent()) {
                                    OveralRiskDescription ov = riskDescription.get();
                                    Optional<Impacts> impacts = impactsRepository.findByIdAndIntrashAndActionStatus(imId, Constants.intrashNO, Constants.actionApproved);
                                    if (impacts.isPresent()) {
                                        Impacts impacts1 = impacts.get();
                                        Optional<Likelyhood> likelyhood = likelyHoodRepository.findByIdAndIntrashAndActionStatus(imId, Constants.intrashNO, Constants.actionApproved);
                                        if (likelyhood.isPresent()) {
                                            Likelyhood likelyhood1 = likelyhood.get();
                                            riskProfile1.setCompany(company);
                                            riskProfile1.setImpacts(impacts1);
                                            riskProfile1.setLikelyhood(likelyhood1);
                                            riskProfile1.setOveralRiskDescription(ov);
                                            riskProfile1.setAccesserName(user1.getFirstName() + " " + user1.getOtherName());
                                            riskProfile1.setActionStatus(Constants.riskStatus1);
                                            riskProfile1.setIntrash(Constants.intrashNO);
                                            riskProfile1.setAssesersComments(asserComm);
                                            riskProfile1.setAssessmentDate(new Date());
                                            riskProfileRepository.save(riskProfile1);
                                            userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "SUCCESS: Risk Assessment Updated Successfully");
                                            genericResponseWrapper.setData(riskProfile1);
                                            genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                                            genericResponseWrapper.setMessage("Risk Assessment Updated Successfully");
                                        } else {
                                            userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: Likelyhood with ID :" + lkId + " is NOT Found");
                                            genericResponseWrapper = customerOrderServiceTemplate.notFound();
                                            genericResponseWrapper.setMessage("Likelyhood with ID :" + lkId + " is NOT Found");
                                        }
                                    } else {
                                        userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: Impact with ID :" + imId + " is NOT Found");
                                        genericResponseWrapper = customerOrderServiceTemplate.notFound();
                                        genericResponseWrapper.setMessage("Impact with ID :" + imId + " is NOT Found");
                                    }
                                } else {
                                    userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: Risk with ID :" + rId + " is NOT Found");
                                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                                    genericResponseWrapper.setMessage("Risk with ID :" + rId + " is NOT Found");
                                }
                            } else {
                                userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: User Status is : " + user1.getActionStatus());
                                genericResponseWrapper.setCode(300);
                                genericResponseWrapper.setStatus("Failed");
                                genericResponseWrapper.setMessage("User Status is : " + user1.getActionStatus());
                                genericResponseWrapper.setData(licence1);
                            }
                        } else {
                            userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: Client Licence Status is: " + licence1.getActionStatus());
                            genericResponseWrapper.setCode(300);
                            genericResponseWrapper.setStatus("Failed");
                            genericResponseWrapper.setMessage("Licence Status : " + licence1.getActionStatus());
                            genericResponseWrapper.setData(licence1);
                        }
                    } else {
                        userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: Client :" + company.getCompanyName() + " Has No Licence");
                        genericResponseWrapper = customerOrderServiceTemplate.notFound();
                        genericResponseWrapper.setMessage("Client :" + company.getCompanyName() + " Has No Licence");
                    }

                } else {
                    userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: User with id : " + uId + " NOT Found");
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("User with id : " + uId + " NOT Found");

                }
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: Risk Assessment : " + riskAssementId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Risk Assessment with id : " + riskAssementId + " NOT Found");
            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT", "FAILED: System Malfunctioned");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }

    public GenericResponseWrapper updateRiskAssessmentByReviewer(RiskReviewerDao riskReviewerDao) {
        GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
        Long reviewerId = riskReviewerDao.getUserId();
        Long riskId = riskReviewerDao.getRiskDetailsId();
        String rStatus = riskReviewerDao.getReviewStatus();
        String rComment = riskReviewerDao.getReviewerComments();
        AuditUsernameCompanyWrapper auditUsernameCompanyWrapper = userServiceTemplate.createAudit(reviewerId);
        String name = null;
        String company8 = null;
        name = auditUsernameCompanyWrapper.getUser();
        company8 = auditUsernameCompanyWrapper.getCompany();
        try {
            Optional<RiskProfile> riskProfile = riskProfileRepository.findByIdAndIntrash(riskId, Constants.intrashNO);
            if (riskProfile.isPresent()) {
                RiskProfile riskProfile1 = riskProfile.get();
                Optional<User> user = userRepository.findByIdAndIntrash(reviewerId, Constants.intrashNO);
                if (user.isPresent()) {
                    User user1 = user.get();
                    Company company = user1.getCompany();
                    Optional<Licence> licence = lincenceRepository.findLicenceByCompany(company);
                    if (licence.isPresent()) {
                        Licence licence1 = licence.get();
                        if (licence1.getActionStatus().equalsIgnoreCase(Constants.licenceStatus2)) {
                            if (user1.getActionStatus().equalsIgnoreCase(Constants.userActive)) {
                                riskProfile1.setAssessmentStatus(rStatus);
                                riskProfile1.setReviewingDate(new Date());
                                riskProfile1.setReviewerName(user1.getFirstName() + " " + user1.getOtherName());
                                riskProfile1.setReviewersComments(rComment);
                                riskProfile1.setActionStatus(Constants.riskStatus2);
                                riskProfileRepository.save(riskProfile1);
                                userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT REVIEW", "SUCCESS: Risk Assessment Review Updated Successfully");
                                genericResponseWrapper.setData(riskProfile1);
                                genericResponseWrapper = customerOrderServiceTemplate.succesSection();
                                genericResponseWrapper.setMessage("Risk Assessment Review Updated Successfully");
                            } else {
                                userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT REVIEW", "FAILED: User Status is : " + user1.getActionStatus());
                                genericResponseWrapper.setCode(300);
                                genericResponseWrapper.setStatus("Failed");
                                genericResponseWrapper.setMessage("User Status is : " + user1.getActionStatus());
                                genericResponseWrapper.setData(licence1);
                            }
                        } else {
                            userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT REVIEW", "FAILED: Client Licence Status : " + licence1.getActionStatus());
                            genericResponseWrapper.setCode(300);
                            genericResponseWrapper.setStatus("Failed");
                            genericResponseWrapper.setMessage("Licence Status : " + licence1.getActionStatus());
                            genericResponseWrapper.setData(licence1);
                        }
                    } else {
                        userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT REVIEW", "FAILED: Client :" + company.getCompanyName() + " Has No Licence");
                        genericResponseWrapper = customerOrderServiceTemplate.notFound();
                        genericResponseWrapper.setMessage("Client :" + company.getCompanyName() + " Has No Licence");
                    }

                } else {
                    userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT REVIEW", "FAILED: User with id : " + reviewerId + " NOT Found");
                    genericResponseWrapper = customerOrderServiceTemplate.notFound();
                    genericResponseWrapper.setMessage("User with id : " + reviewerId + " NOT Found");

                }
            } else {
                userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT REVIEW", "FAILED: Risk Assessment : " + riskId + " NOT Found");
                genericResponseWrapper = customerOrderServiceTemplate.notFound();
                genericResponseWrapper.setMessage("Risk Assessment with id : " + riskId + " NOT Found");
            }
        } catch (Exception ex) {
            genericResponseWrapper = customerOrderServiceTemplate.errorSection(ex);
            ex.printStackTrace();
            userServiceTemplate.saveAuditRails(name, company8, "UPDATE RISK ASSESSMENT REVIEW", "FAILED: System Malfunctioned");
            genericResponseWrapper.setMessage("System Error Occurred");
        }
        return genericResponseWrapper;
    }


}
