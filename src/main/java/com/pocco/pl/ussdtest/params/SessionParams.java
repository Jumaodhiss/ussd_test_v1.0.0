/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.params;

/**
 *
 * @author sure
 */
import com.pocco.pl.ussdtest.model.SessionModel;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SessionParams {

    // static HashMap<String, JSONObject> USSDSessions = new HashMap<>();
    private static Map<String, SessionModel> USSDSessions = new HashMap<String, SessionModel>();

    static {
    }

   
    public void addSession(String msisdn, String sessionDetails) {
        SessionModel session = USSDSessions.get(msisdn);
        session.setSessionData(sessionDetails);
        //System.err.println(USSDSessions);
    }

    public void updateSession(String msisdn, String sessionDetails) {
        USSDSessions.remove(msisdn);
        addSession(msisdn, sessionDetails);
    }

    
    public SessionModel getSession(String msisdn) {
        //USSDSessions.remove(msisdn);
        SessionModel session = USSDSessions.get(msisdn);
        return session;
    }
}
