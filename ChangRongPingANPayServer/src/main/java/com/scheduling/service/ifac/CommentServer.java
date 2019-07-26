package com.scheduling.service.ifac;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface CommentServer extends BaseService {

     Map findByOpenid(String openid);

     int insert(Map map);

     List getAll(Map map);

     int update(Map map);

     int insert1(List list);
}
