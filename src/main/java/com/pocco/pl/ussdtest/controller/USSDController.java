/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pocco.pl.ussdtest.business.logic.LoanRequest;
import com.pocco.pl.ussdtest.business.logic.PayLoan;
import com.pocco.pl.ussdtest.business.logic.Pin;
import com.pocco.pl.ussdtest.business.logic.Register;
import com.pocco.pl.ussdtest.interfaces.CustomerRepo;
import com.pocco.pl.ussdtest.interfaces.LoanRequestRepo;
import com.pocco.pl.ussdtest.interfaces.SessionsRepo;
import com.pocco.pl.ussdtest.model.CustomerModel;
import com.pocco.pl.ussdtest.model.SessionModel;
import com.pocco.pl.ussdtest.model.USSDRequestModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sure
 */
@RestController("/USSDGateway1.0")
public class USSDController {

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private final SessionsRepo sessionsRepo;
    @Autowired
    private final CustomerRepo customerRepo;
    @Autowired
    private final LoanRequestRepo loanRequestRepo;

    public USSDController(SessionsRepo sessionsRepo, CustomerRepo customerRepo, LoanRequestRepo loanRequestRepo) {
        this.sessionsRepo = sessionsRepo;
        this.customerRepo = customerRepo;
        this.loanRequestRepo = loanRequestRepo;
    }

    @PostMapping
    public String postRequest(@RequestBody USSDRequestModel mnoSession) {
        try {
            Map<String, String> bLogic = new HashMap<>();
            String msisdn = mnoSession.getMsisdn();
            String sessionId = mnoSession.getSessionId();
            String recId = mnoSession.getRecId();
            String serviceCode = mnoSession.getServiceCode();
            String ussdBody = mnoSession.getUssdBody();

            Optional<SessionModel> sessionFromH2 = sessionsRepo.findById(new Long(msisdn));
            JSONObject sessionData = new JSONObject();
            if (!sessionFromH2.isPresent()) {
                //Most probably the 1st time here

                sessionData = new JSONObject();

                sessionData.put("sessionId", sessionId);
                sessionData.put("msisdn", msisdn);
                sessionData.put("recId", recId);
                sessionData.put("serviceCode", serviceCode);
                sessionData.put("ussdBody", ussdBody);

                Optional<CustomerModel> CustomerFromH2 = customerRepo.findById(new Long(msisdn));
                String responseString;
                if (!CustomerFromH2.isPresent()) {
                    sessionData.put("status", "Register");
                    responseString = "Welcome to Pocco Credit. To register, please enter your first name";
                } else {
                    CustomerModel cust = CustomerFromH2.get();

                    sessionData.put("status", "Pin");
                    responseString = "Welcome " + cust.getFirstName() + " to Pocco Credit.  please enter your pin";
                    sessionData.put("firstName", cust.getFirstName());
                    sessionData.put("idNumber", cust.getIdNumber());

                }
                String ussdType = "continue";

                sessionData.put("responseString", responseString);
                sessionData.put("ussdType", ussdType);
//                sessionData.put("responseObject", responseObject.toString());

                sessionData.put("statusLevel", "2");//Overwrite to go to the next step
                sessionsRepo.save(new SessionModel(new Long(msisdn), msisdn, sessionData.toString()));

                return ussdType + " " + responseString;
//                return responseObject.toString();
            } else {

//                System.out.println(sessionFromH2);
                // SessionModel status = ;//session.get("status").toString();
                Map<String, String> sessionMap = mapper.readValue(sessionFromH2.get().getSessionData(), Map.class);

//                System.out.println("serviceCode=>" + link.get("serviceCode"));
                String status = sessionMap.get("status");
                switch (status) {
                    case "Pin":
                        bLogic = new Pin(loanRequestRepo).init(sessionMap, mnoSession);
                        break;
                    case "Register":
                        bLogic = new Register(customerRepo).init(sessionMap, mnoSession);
                        break;
                    case "LoanRequest":
                        bLogic = new LoanRequest().init(sessionMap, mnoSession);
                        break;
                    case "PayLoan":
                        bLogic = new PayLoan().init(sessionMap, mnoSession);
                        break;
                }

//                Integer newStatusLevel = (Integer.parseInt(sessionMap.get("statusLevel"))) + 1;
//                sessionMap.put("statusLevel", newStatusLevel + "");//Overwrite to go to the next step
//                mapper.
                //sessionMap.put("responseObject", new Gson().toJson(bLogic));
                sessionsRepo.deleteById(new Long(msisdn));
                if (!"end".equalsIgnoreCase(bLogic.get("ussdType"))) {
                    System.out.println(bLogic.get("ussdType"));
                    if ("continue".equalsIgnoreCase(bLogic.get("ussdType"))) {
                        Integer newStatusLevel = (Integer.parseInt(sessionMap.get("statusLevel"))) + 1;
                        sessionMap.put("statusLevel", newStatusLevel + "");//Overwrite to go to the next step
                    }
                    sessionsRepo.save(new SessionModel(new Long(msisdn), msisdn, new Gson().toJson(sessionMap)));
                }
                return bLogic.get("ussdType") + " " + bLogic.get("responseString");
            }
//        return LoanParams.getLoanType("LN001").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
