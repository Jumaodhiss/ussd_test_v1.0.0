/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.model;

/**
 *
 * @author sure
 */
public class USSDRequestModel {

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
     * @return the recId
     */
    public String getRecId() {
        return recId;
    }

    /**
     * @param recId the recId to set
     */
    public void setRecId(String recId) {
        this.recId = recId;
    }

    /**
     * @return the ussdBody
     */
    public String getUssdBody() {
        return ussdBody;
    }

    /**
     * @param ussdBody the ussdBody to set
     */
    public void setUssdBody(String ussdBody) {
        this.ussdBody = ussdBody;
    }

    /**
     * @return the serviceCode
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * @param serviceCode the serviceCode to set
     */
    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
    private Long id;
    private String sessionId;
    private String msisdn;
    private String recId;
    private String ussdBody;
    private String serviceCode;
}
