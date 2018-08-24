/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author sure
 */
@Entity
@Table(name = "LoanRequests")
public class LoanRequestModel implements Serializable {

    public LoanRequestModel() {
    }

    public LoanRequestModel(String custName, String loanType, String loanName, String loanAmount, String loanTenure, String idNumber, String interest, String loanOutstanding, String loanDate, String dueDate, String tnc) {

        this.custName = custName;
        this.loanType = loanType;
        this.loanName = loanName;
        this.loanAmount = loanAmount;
        this.loanTenure = loanTenure;
        this.idNumber = idNumber;
        this.interest = interest;
        this.loanOutstanding = loanOutstanding;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.tnc = tnc;

    }

    /**
     * @return the loanOutstanding
     */
    public String getLoanOutstanding() {
        return loanOutstanding;
    }

    /**
     * @param loanOutstanding the loanOutstanding to set
     */
    public void setLoanOutstanding(String loanOutstanding) {
        this.loanOutstanding = loanOutstanding;
    }

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
     * @return the custName
     */
    public String getCustName() {
        return custName;
    }

    /**
     * @param custName the custName to set
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * @return the loanType
     */
    public String getLoanType() {
        return loanType;
    }

    /**
     * @param loanType the loanType to set
     */
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    /**
     * @return the loanAmount
     */
    public String getLoanAmount() {
        return loanAmount;
    }

    /**
     * @param loanAmount the loanAmount to set
     */
    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    /**
     * @return the loanTenure
     */
    public String getLoanTenure() {
        return loanTenure;
    }

    /**
     * @param loanTenure the loanTenure to set
     */
    public void setLoanTenure(String loanTenure) {
        this.loanTenure = loanTenure;
    }

    /**
     * @return the idNumber
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber the idNumber to set
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return the interest
     */
    public String getInterest() {
        return interest;
    }

    /**
     * @param interest the interest to set
     */
    public void setInterest(String interest) {
        this.interest = interest;
    }

    /**
     * @return the loanDate
     */
    public String getLoanDate() {
        return loanDate;
    }

    /**
     * @param loanDate the loanDate to set
     */
    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    /**
     * @return the dueDate
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the tnc
     */
    public String getTnc() {
        return tnc;
    }

    /**
     * @param tnc the tnc to set
     */
    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    /**
     * @return the tnc
     */
    public String getLoanName() {
        return loanName;
    }

    /**
     * @param loanName the tnc to set
     */
    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CUSTOMERNAME")
    private String custName;
    @Column(name = "LOANTYPE")
    private String loanType;
    @Column(name = "LOANNAME")
    private String loanName;
    @Column(name = "LOANAMOUNT")
    private String loanAmount;
    @Column(name = "LOANTENURE")
    private String loanTenure;
    @Column(name = "IDNUMBER")
    private String idNumber;
    @Column(name = "INTEREST")
    private String interest;
    @Column(name = "LOANOUTSTANDING")
    private String loanOutstanding;
    @Column(name = "LOANDATE")
    private String loanDate;
    @Column(name = "DUEDATE")
    private String dueDate;
    @Column(name = "TNC")
    private String tnc;

}
