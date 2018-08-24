/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.business.logic;

import com.pocco.pl.ussdtest.model.LoanRequestModel;
import com.pocco.pl.ussdtest.model.USSDRequestModel;
import com.pocco.pl.ussdtest.params.LoanParams;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author sure
 */
public class PayLoan {

    private final LoanParams loanParams = new LoanParams();
    private String ussdType = "continue";
    private String responseString = "";
    private final static Logger LOGGER = Logger.getLogger(PayLoan.class.getName());

    public Map init(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        String statusLevel = sessionMap.get("statusLevel");

        try {

            switch (statusLevel) {
                case "1":
                    sessionMap = payLoanStepOne(sessionMap, mnoSession);
                    break;
                case "2":
                    sessionMap = payLoanStepTwo(sessionMap, mnoSession);
                    break;
                case "3":
                    sessionMap = payLoanStepThree(sessionMap, mnoSession);
                    break;
                case "4":
                    sessionMap = payLoanStepFour(sessionMap, mnoSession);
                    break;

                default:
                    responseString = "An error occured please try again later";
                    ussdType = "end";
                    sessionMap.put("responseString", responseString);
                    sessionMap.put("ussdType", ussdType);
                    break;

            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.info(sw.toString());
            ussdType = "end";
            responseString = "An error occured. Please try again later";
            sessionMap.put("responseString", responseString);
            sessionMap.put("ussdType", ussdType);
        }

        return sessionMap;
    }

    public Map payLoanStepOne(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        try {
            HashMap<String, LoanRequestModel> outStandingLoans = loanParams.getAllLoanRequest(sessionMap.get("idNumber"));
            responseString = "Please select which loan to Repay\n";

            String loanTypeIds = "";
            String loanOutstanding = "";
            Integer x = 1;
            Integer count = 0;
            if (outStandingLoans.size() > 0) {
                for (String loanId : outStandingLoans.keySet()) {
                    LoanRequestModel loan = outStandingLoans.get(loanId);
                    if (new BigDecimal(loan.getLoanOutstanding()).compareTo(new BigDecimal(0)) != 0) {
                        count++;
                        loanTypeIds = loanTypeIds + loan.getLoanType();
                        loanOutstanding = loanOutstanding + loan.getLoanOutstanding();
                        responseString = responseString + x + ". " + loan.getLoanName();
                        if (x < outStandingLoans.size()) {
                            responseString = responseString + "\n";
                            loanTypeIds = loanTypeIds + "|";
                            loanOutstanding = loanOutstanding + "|";
                        }
                    }

                    x++;

                }
                if (count > 0) {
                    sessionMap.put("loanTypeIds", loanTypeIds);
                    sessionMap.put("loanOutstanding", loanOutstanding);
                } else {
                    ussdType = "end";
                    responseString = "You do not have any outstanding loan";
                }
            } else {
                ussdType = "end";
                responseString = "You do not have any outstanding loan";
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

    public Map payLoanStepTwo(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        try {
            HashMap<String, LoanRequestModel> outStandingLoans = loanParams.getAllLoanRequest(sessionMap.get("idNumber"));
            Integer ussdBody = Integer.parseInt(mnoSession.getUssdBody().replaceAll("[^\\d.]", "")) - 1;

            String LoanIds[] = sessionMap.get("loanTypeIds").split("\\|");
            outStandingLoans = loanParams.getAllLoanRequest(sessionMap.get("idNumber"));

            System.err.println(outStandingLoans);
            LoanRequestModel selectedLoan = outStandingLoans.get(LoanIds[ussdBody]);

            if (selectedLoan == null) {
                responseString = "Invalid Menu. Please try again";
                ussdType = "repeat";
            } else {
                BigDecimal outStandingLoan = new BigDecimal(selectedLoan.getLoanOutstanding());

                responseString = "Your outstanding balance is UGX " + outStandingLoan + "\nPlease enter amount to pay";
                sessionMap.put("outStandingLoan", outStandingLoan.toString());
                sessionMap.put("loanTypeId", LoanIds[ussdBody]);

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

    public Map payLoanStepThree(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        try {
            String ussdBodyAmount = mnoSession.getUssdBody().replaceAll("[^\\d.]", "");
            BigDecimal paymentAmount = new BigDecimal(ussdBodyAmount);
            BigDecimal outStandingLoan = new BigDecimal(sessionMap.get("outStandingLoan"));
            BigDecimal newOutstandingLoan = outStandingLoan.subtract(paymentAmount);
            if (newOutstandingLoan.compareTo(new BigDecimal(0)) == 0) {
                sessionMap.put("paymentstatus", "full");
            } else {
                sessionMap.put("paymentstatus", "partial");
            }
            if (paymentAmount.compareTo(outStandingLoan) == 1) {
                ussdType = "repeat";
                responseString = "You can only pay up to UGX " + outStandingLoan + "\nPlease try again";
            } else {
                responseString = "Please confirm payment of UGX " + paymentAmount + " for your loan ID " + sessionMap.get("loanTypeId") + "\n1. Confirm\n2. Decline";
            }
            sessionMap.put("loanAmount", paymentAmount.toString());
            sessionMap.put("loanOutstandingAmount", outStandingLoan.toString());
            sessionMap.put("newOutstandingLoan", newOutstandingLoan.toString());
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

    public Map payLoanStepFour(Map<String, String> sessionMap, USSDRequestModel mnoSession) {
        try {
            HashMap<String, LoanRequestModel> outStandingLoans = loanParams.getAllLoanRequest(sessionMap.get("idNumber"));
            Integer confirm = Integer.parseInt(mnoSession.getUssdBody().replaceAll("\\W", ""));

            if (confirm == 1) {
                outStandingLoans = loanParams.getAllLoanRequest(sessionMap.get("idNumber"));
                LoanRequestModel currentLoan = outStandingLoans.get(sessionMap.get("loanTypeId"));
                currentLoan.setLoanOutstanding(sessionMap.get("newOutstandingLoan"));

                if ("full".equalsIgnoreCase(sessionMap.get("paymentstatus"))) {
                    loanParams.updateLoanRequest(sessionMap.get("idNumber"), sessionMap.get("loanTypeId"), currentLoan);
                    responseString = "Congratulations " + sessionMap.get("firstName") + ", You have fully paid your loan. thank you";
                } else {
                    responseString = "Dear " + sessionMap.get("firstName") + ", You have paid UGX" + sessionMap.get("loanAmount") + ". Your outstanding loan is UGX " + sessionMap.get("newOutstandingLoan") + ". Thank you for choosing pocco credits";
                }

                ussdType = "end";
            } else {
                ussdType = "repeat";
                responseString = "Invalid Name Length";
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
