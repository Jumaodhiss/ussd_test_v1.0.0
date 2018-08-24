/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.business.logic;

import com.pocco.pl.ussdtest.model.USSDRequestModel;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author sure
 */
public class Pin {

    private final static Logger LOGGER = Logger.getLogger(Pin.class.getName());

    public Pin() {

    }

    public Map init(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String statusLevel = sessionMap.get("statusLevel");
        String ussdType = "continue";
        String responseString = "";

        switch (statusLevel) {

            case "1":
                sessionMap = pinStepOne(sessionMap, mnoSession);
                break;
            case "2":
                sessionMap = pinStepTwo(sessionMap, mnoSession);
                break;
            case "3":
                sessionMap = pinStepThree(sessionMap, mnoSession);
                break;

            default:
                responseString = "An error occured please try again later";
                ussdType = "end";
                sessionMap.put("responseString", responseString);
                sessionMap.put("ussdType", ussdType);
                break;

        }

        return sessionMap;
    }

    public Map pinStepOne(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";

        try {

            ussdType = "continue";
            responseString = "Welcome " + sessionMap.get("firstName") + " to Pocco Credit.  please enter your pin";

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

    public Map pinStepTwo(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";

        try {

            String pin = mnoSession.getUssdBody().replaceAll("[^\\d.]", "");
            if (pin.length() != 4) {
                ussdType = "repeat";
                responseString = "Invalid Pin. Please try again";
            } else if ("1234".equals(pin)) {
                responseString = "Please select service:\n1. Loan Request\n2. Loan Repayment";
            } else {
                ussdType = "repeat";
                responseString = "Invalid Pin. Please try again";
            }

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

    public Map pinStepThree(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";

        try {

            Integer serviceRequest = Integer.parseInt(mnoSession.getUssdBody().replaceAll("[^\\d.]", ""));
            switch (serviceRequest) {
                case 1:
                    //
                    sessionMap.put("statusLevel", "1");
                    sessionMap.put("status", "LoanRequest");
                    return new LoanRequest().init(sessionMap, mnoSession);
                case 2:
                    sessionMap.put("statusLevel", "1");
                    sessionMap.put("status", "PayLoan");
                    return new PayLoan().init(sessionMap, mnoSession);
                default:
                    ussdType = "repeat";
                    responseString = serviceRequest + "Invalid Menu. Please try again";
                    break;
            }
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
