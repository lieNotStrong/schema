package com.scheduling.service.ifac;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface NewScheduServer extends BaseService {

     Map findByOpenid(String openid);

     int insert(Map map);

     List all(Map map);

     int update(Map map);

     int saveList(List list);
}
