package com.scheduling.service.ifac;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface NoticeServer extends BaseService {

     Map findByOpenid(String openid);

     int insert(Map map);

     int saveList(List list);


}
