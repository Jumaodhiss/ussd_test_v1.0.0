/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pocco.pl.ussdtest.interfaces;

import com.pocco.pl.ussdtest.model.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sure
 */
public interface SessionsRepo extends JpaRepository<SessionModel, Long> {
//{
//
//    "msisdn": "254700327596",
//    "sessionId": "2348767819",
//    "recId": "1",
//    "serviceCode": "*168*5#",
//    "ussdBody": "*168*5#"
//}http://localhost:8080/USSDGateway1.0
    
//    try (Reader reader = new FileReader("D:\\staff.json")) {
//
//			// Convert JSON to Java Object
//            Staff staff = gson.fromJson(reader, Staff.class);
//            System.out.println(staff);
//
//			// Convert JSON to JsonElement, and later to String
//            /*JsonElement json = gson.fromJson(reader, JsonElement.class);
//            String jsonInString = gson.toJson(json);
//            System.out.println(jsonInString);*/
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
}
