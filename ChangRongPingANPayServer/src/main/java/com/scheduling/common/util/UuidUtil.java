package com.scheduling.common.util;

import java.util.*;

public class UuidUtil {
public static int i=0;
	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
	public static String get16(){
		String str=System.currentTimeMillis()+"";
		String i_str="";
		if(i>=100000){
			i=0;
		}
		i++;
		i_str=i+"";
		while(true){
			if(i_str.length()>5){
				break;
			}
			i_str="0"+i_str;
		}
		return System.currentTimeMillis()+i_str;
	}

}

