package com.scheduling.service.ifac;

import com.scheduling.DTO.MyException;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface UserServer extends BaseService {

     Map findByOpenid(String openid);

     int insert(Map map);

     Map loginType(String skey) throws MyException;
     }
