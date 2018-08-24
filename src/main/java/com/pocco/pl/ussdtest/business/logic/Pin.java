/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.business.logic;

import com.pocco.pl.ussdtest.interfaces.LoanRequestRepo;
import com.pocco.pl.ussdtest.model.USSDRequestModel;
import java.util.Map;

/**
 *
 * @author sure
 */
public class Pin {

    private final LoanRequestRepo loanRequestRepo;

    public Pin(LoanRequestRepo loanRequestRepo) {
        this.loanRequestRepo = loanRequestRepo;
    }

    public Map init(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String statusLevel = sessionMap.get("statusLevel");
        String ussdType = "continue";
        String responseString = "";

        switch (statusLevel) {

            case "2":
                String pin = mnoSession.getUssdBody().replaceAll("[^\\d.]", "");
                if (pin.length() != 4) {
                    ussdType = "repeat";
                    responseString = "Invalid Pin. Please try again";
                } else {
                    if ("1234".equals(pin)) {
                        responseString = "Please select service:\n1. Loan Request\n2. Loan Repayment";
                    } else {
                        ussdType = "repeat";
                        responseString = "Invalid Pin. Please try again";
                    }
                }
                break;
            case "3":

                String serviceRequest = mnoSession.getUssdBody().replaceAll("[^\\d.]", "");
                if ("1".equals(serviceRequest)) {//
                    sessionMap.put("statusLevel", "1");
                    sessionMap.put("status", "LoanRequest");
                    return new LoanRequest().init(sessionMap, mnoSession);
                } else if ("2".equals(serviceRequest)) {
                    sessionMap.put("statusLevel", "1");
                    sessionMap.put("status", "PayLoan");
                    return new PayLoan().init(sessionMap, mnoSession);
                } else {
                    ussdType = "repeat";
                    responseString = serviceRequest + "Invalid Menu. Please try again";
                }

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
