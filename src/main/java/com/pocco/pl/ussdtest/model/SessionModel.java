/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author sure
 */
@Entity
@Table(name = "CustomerSessions")
public class SessionModel implements Serializable {


    public SessionModel() {
    }

    public SessionModel(Long id, String msisdn, String sessionData) {
        this.id = id;
        this.msisdn = msisdn;
        this.sessionData = sessionData;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the sessionData
     */
    public String getSessionData() {
        return sessionData;
    }

    /**
     * @param sessionData the sessionData to set
     */
    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "MSISDN")
    private String msisdn;
    @Column(name = "SESSIONDATA",length = 1000)
    private String sessionData;

}
