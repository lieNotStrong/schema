package com.scheduling.service.impl;

import com.scheduling.service.ifac.NoticeServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("noticeServer")
@SuppressWarnings("rawtypes")
public class NoticeServerImpl extends BaseServiceImpl implements NoticeServer {

    String mapper = "NoticeMapper.";


    @Override
    public Map findByOpenid(String openid) {
        Map map = new HashMap();
        map.put("openid",openid);
        map = findById_sql(mapper+"findById", map);
        return map;
    }

    @Override
    public int insert(Map map) {
        return save_sql(mapper+"save", map);
    }

    @Override
    public int saveList(List list) {
        return save_sql(mapper + "save", list);
    }
}
