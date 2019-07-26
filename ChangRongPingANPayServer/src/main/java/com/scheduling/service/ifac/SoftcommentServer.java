package com.scheduling.service.ifac;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface SoftcommentServer extends BaseService {

     Map findByOpenid(String openid);
     Map findByid(String softcommentid);

     int insert(Map map);
     int insertTwo(Map map);
     List getAll(Map map );
     List getAllTwo( );
     List getAllone(Map map );
     List queryUser(Map map);
     int update(Map map);

}
