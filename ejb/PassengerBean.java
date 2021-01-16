/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lennydennis.plane_ticketing.ejb;

import com.google.common.base.Strings;
import com.lennydennis.plane_ticketing.ejb_db.DatabaseBean;
import com.lennydennis.plane_ticketing.entities.Booking;
import com.lennydennis.plane_ticketing.entities.Flight;
import com.lennydennis.plane_ticketing.entities.Passenger;
import com.lennydennis.plane_ticketing.entities.User;
import com.lennydennis.plane_ticketing.jpa.TransactionProvider;
import com.lennydennis.plane_ticketing.utils.JsonResponse;
import com.lennydennis.plane_ticketing.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author lenny
 */
@Stateless
public class PassengerBean {

    @EJB
    DatabaseBean dbEntities;

    @EJB
    TransactionProvider transactionProvider;

    public JsonResponse updatePassenger(List<HashMap> hashMaps) {
        JsonResponse res = new JsonResponse();
        res.setCode(500);
        res.setMessage("An error occured");

        List<Passenger> passengers = new ArrayList();
        Boolean passengerExists = false;
        try {

            for(HashMap hashMap:hashMaps) {
                String passengerId = Utils.getStringValueFromHashmap(hashMap, "id");
                String passengerName = Utils.getStringValueFromHashmap(hashMap, "name");
                String passengerIdNo = Utils.getStringValueFromHashmap(hashMap, "idNo");
                String passengerEmail = Utils.getStringValueFromHashmap(hashMap, "email");
                String passengerCountry = Utils.getStringValueFromHashmap(hashMap, "country");
                String passengerNumber = Utils.getStringValueFromHashmap(hashMap, "number");
                String passengerGender = Utils.getStringValueFromHashmap(hashMap, "gender");

                if (Strings.isNullOrEmpty(passengerName) || Strings.isNullOrEmpty(passengerIdNo)
                        || Strings.isNullOrEmpty(passengerEmail) || Strings.isNullOrEmpty(passengerCountry)
                        || Strings.isNullOrEmpty(passengerNumber) || Strings.isNullOrEmpty(passengerGender)) {
                    res.setMessage("A passenger field/fields is empty");
                } else {
                    int id = Integer.parseInt(passengerId);
                    int intpassengerIdNo = Integer.parseInt(passengerIdNo);
                    int intpassengerGender = Integer.parseInt(passengerGender);

                    Passenger passengerRetrieved = dbEntities.getPassenger_ById(id);
                    passengerExists = passengerRetrieved != null;
                    
                    if (passengerExists) {
                        
                        Passenger passenger = new Passenger();
                        passenger.setId(id);
                        passenger.setName(passengerName);
                        passenger.setIdNo(intpassengerIdNo);
                        passenger.setEmail(passengerEmail);
                        passenger.setGender(intpassengerGender);
                        passenger.setPhone(passengerNumber);
                        passenger.setCountry(passengerCountry);
                        passenger.setBooking(passengerRetrieved.getBooking());
                        passengers.add(passenger);
                        
                    } else {
                        res.setMessage("Passenger does not exist");
                    }
                }
            }

            if (passengers.size() > 0 && passengerExists) {
                if (transactionProvider.updateMultipleEntities(passengers)) {
                    res.setCode(200);
                    res.setMessage("Passenger/s has/have been update");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public JsonResponse deletePassenger_ById(Integer passengerId) {
        JsonResponse res = new JsonResponse();
        res.setCode(500);
        res.setMessage("An error occured");
        try {
            if (passengerId != null) {
                Passenger passenger = dbEntities.getPassenger_ById(passengerId);
                    
                if (passenger != null) {
                    if (transactionProvider.deleteEntity(passenger)) {
                        res.setCode(200);
                        res.setMessage("Passenger has been deleted");
                    }
                } else {
                    res.setMessage("Passenger does not exist");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

}
