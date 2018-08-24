/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.business.logic;

import com.pocco.pl.ussdtest.interfaces.CustomerRepo;
import com.pocco.pl.ussdtest.model.CustomerModel;
import com.pocco.pl.ussdtest.model.USSDRequestModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author sure
 */
public class Register {

    private final CustomerRepo customerRepo;

    public Register(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Map init(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        JSONObject responseObject = new JSONObject();
        String statusLevel = sessionMap.get("statusLevel");
        String ussdType = "continue";
        String responseString = "";

        switch (statusLevel) {
            case "1":
                responseString = "Welcome to Pocco Credit. To register, please enter your first name";
                break;
            case "2":
                String firstName = mnoSession.getUssdBody().toUpperCase().replaceAll("[^A-Z]", "");
                if (firstName.length() > 1) {
                    responseString = "Please enter your last name";
                } else {
                    ussdType = "repeat";
                    responseString = "Invalid Name Length";
                }
                sessionMap.put("firstName", firstName);
                break;
            case "3":
                String lastName = mnoSession.getUssdBody().toUpperCase().replaceAll("[^A-Z]", "");
                if (lastName.length() > 0) {
                    responseString = "Please enter your date of birth format yyyymmdd eg 19840402";
                } else {
                    ussdType = "repeat";
                    responseString = "Invalid Name Length";
                }
                sessionMap.put("lastName", lastName);
                break;
            case "4":
                String dateOfBirth = mnoSession.getUssdBody().replaceAll("[^\\d.]", "");

                LocalDate now = LocalDate.now();
                LocalDate latestDOB = now.minusMonths(216);
                System.out.println(latestDOB);
                try {
                    if (dateOfBirth.length() == 8) {

                        LocalDate customerDob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyyMMdd"));
                        if (latestDOB.isBefore(customerDob)) {
                            ussdType = "repeat";
                            responseString = "Invalid Date of Birth";
                        } else {
                            responseString = "Please enter your ID number:";
                        }
                    } else {
                        ussdType = "repeat";
                        responseString = "Invalid Date of Birth";
                    }
                    sessionMap.put("dateOfBirth", dateOfBirth);
                } catch (Exception e) {
                    e.printStackTrace();
                    ussdType = "repeat";
                    responseString = "Invalid Date of Birth";
                }
                //responseString = "";
                break;
            case "5":
                String idNumber = mnoSession.getUssdBody().replaceAll("\\W", "");
                if (idNumber.length() >= 8) {
                    responseString = "Accept terms and conditions\n1. Accept\n2. Decline";
                } else {
                    ussdType = "repeat";
                    responseString = "Invalid Name Length";
                }

                sessionMap.put("idNumber", idNumber);
                break;
            case "6":
                Integer tnc = Integer.parseInt(mnoSession.getUssdBody().replaceAll("\\W", ""));
                if (tnc == 1) {

                    customerRepo.save(new CustomerModel(new Long(mnoSession.getMsisdn()), sessionMap.get("firstName"), sessionMap.get("lastName"), sessionMap.get("dateOfBirth"), sessionMap.get("idNumber"), tnc + ""));
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
                break;
            default:
                responseString = "An error occured please try again later";
                ussdType = "end";
                break;

        }
        sessionMap.put("responseString", responseString);
        sessionMap.put("ussdType", ussdType);
//        sessionMap.put("responseObject", new Gson().toJson(responseObject));

        return sessionMap;
    }
}
