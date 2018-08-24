/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.interfaces;

import com.pocco.pl.ussdtest.model.LoanRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sure
 */
@Repository
public interface LoanRequestRepo extends JpaRepository<LoanRequestModel, Long> {


}
