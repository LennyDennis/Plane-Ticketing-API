/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lennydennis.plane_ticketing.ejb;

import com.google.common.base.Strings;
import com.lennydennis.plane_ticketing.ejb_db.DatabaseBean;
import com.lennydennis.plane_ticketing.entities.User;
import com.lennydennis.plane_ticketing.jpa.TransactionProvider;
import com.lennydennis.plane_ticketing.utils.JsonResponse;
import com.lennydennis.plane_ticketing.utils.Utils;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author lenny
 */
@Stateless
public class AuthenticationBean {

    @EJB
    DatabaseBean dbEntities;

    @EJB
    TransactionProvider transactionProvider;

    public JsonResponse addAUser(HashMap hashMap) {
        JsonResponse jr = new JsonResponse();
        jr.setCode(500);
        jr.setMessage("An error occurred");
        try {
            String userName = Utils.getStringValueFromHashmap(hashMap, "name");
            String userIdNo = Utils.getStringValueFromHashmap(hashMap, "idno");
            String userEmail = Utils.getStringValueFromHashmap(hashMap, "email");
            String userCountry = Utils.getStringValueFromHashmap(hashMap, "country");
            String userNumber = Utils.getStringValueFromHashmap(hashMap, "number");
            String userPassword = Utils.getStringValueFromHashmap(hashMap, "password");
            String userGender = Utils.getStringValueFromHashmap(hashMap, "gender");
            String userType = Utils.getStringValueFromHashmap(hashMap, "type");

            User existingUser = dbEntities.getUser_ByUserEmail(userEmail);
            if (existingUser == null) {

                if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(userIdNo)
                        || Strings.isNullOrEmpty(userEmail) || Strings.isNullOrEmpty(userCountry)
                        || Strings.isNullOrEmpty(userNumber) || Strings.isNullOrEmpty(userPassword)
                        || Strings.isNullOrEmpty(userGender) || Strings.isNullOrEmpty(userType)) {
                    jr.setMessage("A user field/fields is empty");
                } else {
                    int intUserIdNo = Integer.parseInt(userIdNo);
                    int intUserType = Integer.parseInt(userType);
                    int intUserGender = Integer.parseInt(userGender);

                    User user = new User();
                    user.setName(userName);
                    user.setIdNo(intUserIdNo);
                    user.setEmail(userEmail);
                    user.setType(intUserType);
                    user.setPassword(userPassword);
                    user.setGender(intUserGender);
                    user.setPhoneNumber(userNumber);
                    user.setCountry(userCountry);

                    if (transactionProvider.createEntity(user)) {
                        jr.setCode(200);
                        jr.setMessage(userName + " has been regitered");
                    }
                }
            } else {
                jr.setMessage("Email already exists");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jr;
        }
    }

    public JsonResponse loginUser(HashMap hashMap) {
        JsonResponse jr = new JsonResponse();
        jr.setCode(500);
        jr.setMessage("An error occurred");
        try {
            String userEmail = Utils.getStringValueFromHashmap(hashMap, "email");
            String userPassword = Utils.getStringValueFromHashmap(hashMap, "password");

            if (Strings.isNullOrEmpty(userEmail) || Strings.isNullOrEmpty(userPassword)) {
                jr.setMessage("A user field/fields is empty");
            } else {
                User registeredUser = dbEntities.getUser_ByUserEmail(userEmail);
                if (registeredUser != null) {
                    String registeredUserPassword = registeredUser.getPassword();
                    if (userPassword.equals(registeredUserPassword)) {
                        jr.setCode(200);
                        jr.setMessage(registeredUser.getName() + " logged in");
                    } else {
                        jr.setMessage("Passwords do not match");
                    }
                } else {
                    jr.setMessage("Email does not exist");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jr;
        }
    }

}
