package com.scheduling.service.ifac;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface WorkemployeeServer extends BaseService {

     Map findByOpenid(String openid);

     int insert(Map map);
     int inserttwo(Map map);

     List all(Map map);

     int update(Map map);
}
