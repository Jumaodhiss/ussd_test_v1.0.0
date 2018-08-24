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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private final static Logger LOGGER = Logger.getLogger(USSDController.class.getName());

    public USSDController(SessionsRepo sessionsRepo, CustomerRepo customerRepo, LoanRequestRepo loanRequestRepo) {
        this.sessionsRepo = sessionsRepo;
        this.customerRepo = customerRepo;
        this.loanRequestRepo = loanRequestRepo;
    }

    @PostMapping
    public String postRequest(@RequestBody USSDRequestModel mnoSession) {
        String ussdType = "continue";
        String responseString = "";
        Map<String, String> sessionMap = new HashMap<>();
        try {
            Map<String, String> bLogic = new HashMap<>();
            String msisdn = mnoSession.getMsisdn();
            String sessionId = mnoSession.getSessionId();
            String recId = mnoSession.getRecId();
            String serviceCode = mnoSession.getServiceCode();
            String ussdBody = mnoSession.getUssdBody();

            Optional<SessionModel> sessionFromH2 = sessionsRepo.findById(new Long(msisdn));
            sessionMap = new HashMap<>();
            if (!sessionFromH2.isPresent()) {
                //Most probably the 1st time here
                LOGGER.log(Level.INFO, "Class {0} Next Level{1}", new Object[]{this.getClass(), "Imeingia hapa true"});
                sessionMap = new HashMap<>();

                sessionMap.put("sessionId", sessionId);
                sessionMap.put("msisdn", msisdn);
                sessionMap.put("recId", recId);
                sessionMap.put("serviceCode", serviceCode);
                sessionMap.put("ussdBody", ussdBody);

                Optional<CustomerModel> CustomerFromH2 = customerRepo.findById(new Long(msisdn));

                if (!CustomerFromH2.isPresent()) {
                    sessionMap.put("status", "Register");
                    sessionMap.put("statusLevel", "1");//Overwrite to go to the next step
                    bLogic = new Register(customerRepo).init(sessionMap, mnoSession);
                } else {
                    sessionMap.put("status", "Pin");
                    sessionMap.put("statusLevel", "1");//Overwrite to go to the next step

                    CustomerModel cust = CustomerFromH2.get();
                    sessionMap.put("firstName", cust.getFirstName());
                    sessionMap.put("idNumber", cust.getIdNumber());

                    bLogic = new Pin().init(sessionMap, mnoSession);

                }
                Integer newStatusLevel = Integer.parseInt(sessionMap.get("statusLevel"));
                if (!"end".equalsIgnoreCase(bLogic.get("ussdType"))) {
                    if ("continue".equalsIgnoreCase(bLogic.get("ussdType"))) {
                        newStatusLevel = newStatusLevel + 1;
                        sessionMap.put("statusLevel", newStatusLevel + "");//Overwrite to go to the next step
                    }

                    //LOGGER.log(Level.INFO, "Class {0} Next Level{1}", new Object[]{this.getClass(), newStatusLevel});
                    sessionsRepo.save(new SessionModel(new Long(msisdn), msisdn, new Gson().toJson(sessionMap)));
                }

                responseString = bLogic.get("responseString");
                ussdType = bLogic.get("ussdType");
            } else {

//                System.out.println(sessionFromH2);
                // SessionModel status = ;//session.get("status").toString();
                sessionMap = mapper.readValue(sessionFromH2.get().getSessionData(), Map.class);

                LOGGER.log(Level.INFO, "Class {0} Next Level{1}", new Object[]{this.getClass(), "Imeingia hapa false  " + sessionMap.get("status")+" "+sessionMap.get("statusLevel")});

//                System.out.println("serviceCode=>" + link.get("serviceCode"));
                String status = sessionMap.get("status");
                switch (status) {
                    case "Pin":
                        bLogic = new Pin().init(sessionMap, mnoSession);
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

                sessionsRepo.deleteById(new Long(msisdn));
                if (!"end".equalsIgnoreCase(bLogic.get("ussdType"))) {
                    System.out.println(bLogic.get("ussdType"));
                    if ("continue".equalsIgnoreCase(bLogic.get("ussdType"))) {
                        Integer newStatusLevel = (Integer.parseInt(sessionMap.get("statusLevel"))) + 1;
                        sessionMap.put("statusLevel", newStatusLevel + "");//Overwrite to go to the next step
                    }
                    sessionsRepo.save(new SessionModel(new Long(msisdn), msisdn, new Gson().toJson(sessionMap)));
                }

                responseString = bLogic.get("responseString");
                ussdType = bLogic.get("ussdType");
            }
//        return LoanParams.getLoanType("LN001").toString();

        } catch (IOException | NumberFormatException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.info(sw.toString());
            ussdType = "end";
            responseString = "An error occured. Please try again later";
        }
        return ussdType + " " + responseString;
    }

}
