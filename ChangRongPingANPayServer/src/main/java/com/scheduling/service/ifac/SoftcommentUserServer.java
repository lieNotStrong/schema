package com.scheduling.service.ifac;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface SoftcommentUserServer extends BaseService {

     int insert(Map map);

     int del(Map map);

     List getAll(Map map);

     Map fanById(Map map);

}
