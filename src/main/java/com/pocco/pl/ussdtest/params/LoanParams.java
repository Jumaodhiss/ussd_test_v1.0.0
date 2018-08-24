/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.params;

import com.pocco.pl.ussdtest.model.LoanRequestModel;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author sure
 */
public class LoanParams {

    static HashMap<String, JSONObject> LoanTypes = new HashMap<>();
    public static HashMap<String, HashMap<String, LoanRequestModel>> LoanRequest = new HashMap<>();
    HashMap<String, LoanRequestModel> currentLoanRequest = new HashMap<>();
    static HashMap<String, JSONObject> USSDSessions = new HashMap<>();
//    static HashMap<String, JSONObject> loanDetails = new HashMap<>();

    public void setLoanTypes() {
        JSONObject loanType = new JSONObject();

        loanType = new JSONObject();
        loanType.put("loanTypeId", "LN001");
        loanType.put("maximumLoanAmount", "10000");
        loanType.put("minimumLoanAmount", "100");
        loanType.put("loanTenure", "1");
        loanType.put("loanInterest", "10");
        loanType.put("loanName", "Personal Micro Loan");
        LoanTypes.put("LN001", loanType);

        loanType = new JSONObject();
        loanType.put("loanTypeId", "LN002");
        loanType.put("maximumLoanAmount", "200000");
        loanType.put("minimumLoanAmount", "10000");
        loanType.put("loanTenure", "3");
        loanType.put("loanInterest", "10");
        loanType.put("loanName", "LPO Financing Loan");
        LoanTypes.put("LN002", loanType);

        System.out.println(LoanTypes);
    }

    public static HashMap<String, JSONObject> getLoanTypes() {
        return LoanTypes;
    }

    public static JSONObject getLoanType(String loanTypeId) {
        return LoanTypes.get(loanTypeId);
    }

    public void addLoanRequest(String idNumber, String loanType, LoanRequestModel loanRequest) {
        currentLoanRequest.put(loanType, loanRequest);
        LoanRequest.put(idNumber, currentLoanRequest);

    }

    public void updateLoanRequest(String idNumber, String loanType, LoanRequestModel loanRequest) {
        LoanRequest.get(idNumber).remove(loanType);
        addLoanRequest(idNumber, loanType, loanRequest);
    }

    public LoanRequestModel getLoanRequest(String idNumber, String loanType) {
        currentLoanRequest = LoanRequest.get(idNumber);
        if (currentLoanRequest == null) {
            return null;
        }
        return currentLoanRequest.get(loanType);
    }

    public HashMap<String, LoanRequestModel> getAllLoanRequest(String idNumber) {
        currentLoanRequest = LoanRequest.get(idNumber);
        return currentLoanRequest;
    }
}
