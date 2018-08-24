/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.business.logic;

import com.pocco.pl.ussdtest.interfaces.CustomerRepo;
import com.pocco.pl.ussdtest.model.CustomerModel;
import com.pocco.pl.ussdtest.model.USSDRequestModel;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author sure
 */
public class Register {

    private final CustomerRepo customerRepo;
    private final static Logger LOGGER = Logger.getLogger(Register.class.getName());

    public Register(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Map init(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        JSONObject responseObject = new JSONObject();
        String statusLevel = sessionMap.get("statusLevel");

        switch (statusLevel) {
            case "1":
                sessionMap = registerStepOne(sessionMap, mnoSession);
                break;
            case "2":
                
                sessionMap = registerStepTwo(sessionMap, mnoSession);
                break;
            case "3":
                sessionMap = registerStepThree(sessionMap, mnoSession);
                break;
            case "4":
                sessionMap = registerStepFour(sessionMap, mnoSession);
                break;
            case "5":
                sessionMap = registerStepFive(sessionMap, mnoSession);
                break;
            case "6":
                sessionMap = registerStepSix(sessionMap, mnoSession);
                break;
            default:
                String ussdType = "continue";
                String responseString = "";
                responseString = "An error occured please try again later";
                ussdType = "end";
                sessionMap.put("responseString", responseString);
                sessionMap.put("ussdType", ussdType);
                break;

        }

        return sessionMap;
    }

    public Map registerStepOne(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";
        try {
            ussdType = "continue";
            responseString = "Welcome to Pocco Credit. To register, please enter your first name";

        } catch (NumberFormatException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.info(sw.toString());
            ussdType = "end";
            responseString = "An error occured. Please try again later";
        }

        sessionMap.put("responseString", responseString);
        sessionMap.put("ussdType", ussdType);
        return sessionMap;

    }

    public Map registerStepTwo(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";
        try {
            String firstName = mnoSession.getUssdBody().toUpperCase().replaceAll("[^A-Z]", "");
            if (firstName.length() > 1) {
                responseString = "Please enter your last name";
            } else {
                ussdType = "repeat";
                responseString = "Invalid Name Length";
            }
            sessionMap.put("firstName", firstName);
        } catch (NumberFormatException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.info(sw.toString());
            ussdType = "end";
            responseString = "An error occured. Please try again later";
        }

        sessionMap.put("responseString", responseString);
        sessionMap.put("ussdType", ussdType);
        return sessionMap;
    }

    public Map registerStepThree(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";
        try {
            String lastName = mnoSession.getUssdBody().toUpperCase().replaceAll("[^A-Z]", "");
            if (lastName.length() > 0) {
                responseString = "Please enter your date of birth format yyyymmdd eg 19840402";
            } else {
                ussdType = "repeat";
                responseString = "Invalid Name Length";
            }
            sessionMap.put("lastName", lastName);
        } catch (NumberFormatException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.info(sw.toString());
            ussdType = "end";
            responseString = "An error occured. Please try again later";
        }

        sessionMap.put("responseString", responseString);
        sessionMap.put("ussdType", ussdType);
        return sessionMap;

    }

    public Map registerStepFour(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";
        String dateOfBirth = mnoSession.getUssdBody().replaceAll("[^\\d.]", "");

        try {
            LocalDate now = LocalDate.now();
            LocalDate latestDOB = now.minusMonths(216);
            LocalDate customerDob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyyMMdd"));
            if (dateOfBirth.length() == 8 && latestDOB.isAfter(customerDob)) {
                responseString = "Please enter your ID number:";
            } else {
                ussdType = "repeat";
                responseString = "Invalid Date of Birth";
            }
            sessionMap.put("dateOfBirth", dateOfBirth);
        } catch (NumberFormatException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.info(sw.toString());
            ussdType = "end";
            responseString = "An error occured. Please try again later";
        }

        sessionMap.put("responseString", responseString);
        sessionMap.put("ussdType", ussdType);
        return sessionMap;

    }

    public Map registerStepFive(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";
        try {
            String idNumber = mnoSession.getUssdBody().replaceAll("\\W", "");
            if (idNumber.length() >= 8) {
                responseString = "Accept terms and conditions\n1. Accept\n2. Decline";
            } else {
                ussdType = "repeat";
                responseString = "Invalid Name Length";
            }

            sessionMap.put("idNumber", idNumber);

        } catch (NumberFormatException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.info(sw.toString());
            ussdType = "end";
            responseString = "An error occured. Please try again later";
        }

        sessionMap.put("responseString", responseString);
        sessionMap.put("ussdType", ussdType);
        return sessionMap;

    }

    public Map registerStepSix(Map<String, String> sessionMap, USSDRequestModel mnoSession) {

        String ussdType = "continue";
        String responseString = "";
        try {

            Integer tnc = Integer.parseInt(mnoSession.getUssdBody().replaceAll("\\W", ""));
            if (tnc == 1) {
                customerRepo.save(
                        new CustomerModel(
                                new Long(mnoSession.getMsisdn()),
                                sessionMap.get("firstName"),
                                sessionMap.get("lastName"),
                                sessionMap.get("dateOfBirth"),
                                sessionMap.get("idNumber"),
                                tnc + ""
                        )
                );
                responseString = "Congratulations " + sessionMap.get("firstName") + ", You are successfully registered for pocco credits";
                ussdType = "end";
            } else {
                if (tnc == 2) {
                    responseString = "To register for Pocco credit, you must accept terms and conditions. Please dial " + mnoSession.getServiceCode() + " to try again";
                    ussdType = "end";
                } else {
                    ussdType = "repeat";
                    responseString = "Invalid TnC response\nAccept terms and conditions\n1. Accept\n2. Decline";
                }
            }
            sessionMap.put("tnc", tnc + "");

        } catch (NumberFormatException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.info(sw.toString());
            ussdType = "end";
            responseString = "An error occured. Please try again later";
        }

        sessionMap.put("responseString", responseString);
        sessionMap.put("ussdType", ussdType);
        return sessionMap;

    }
}
