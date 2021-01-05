/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lennydennis.plane_ticketing.utils;

import java.util.HashMap;

/**
 *
 * @author lenny
 */
public class Utils {

    public static String getStringValueFromHashmap(HashMap hashMap, String key) {
        String res = null;
        try {
            if (hashMap.get(key) != null) {
                res = hashMap.get(key).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
    
        public static Integer getIntegerValueFromHashmap(HashMap hashMap, String key) {
        Integer res = null;
        try {
            if (hashMap.get(key) != null) {
                res = Integer.valueOf(hashMap.get(key).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

}
