package com.scheduling.common;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class LoginMap {


    public static Map<String,String> loginMap = new HashMap();



    public static void put(String Key,String value){
        loginMap.put(Key,value);
    }

    public static void remove(String Key){
        loginMap.remove(Key);
    }

    public static String get(String Key){
        return loginMap.get(Key);
    }


}
