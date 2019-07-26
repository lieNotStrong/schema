package com.scheduling.service.impl;

import com.scheduling.service.ifac.WorkemployeeServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("workemployeeServer")
@SuppressWarnings("rawtypes")
public class WorkemployeeServerImpl extends BaseServiceImpl implements WorkemployeeServer {

    String mapper = "ScheduMapper.";

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
    public int inserttwo(Map map) {
        return save_sql("WorkemployeeMapper.save", map);
    }

    @Override
    public List all(Map map) {
        List<Map> maps = listAll_sql(mapper + "listAll", map);
        return maps;
    }

    @Override
    public int update(Map map) {
        return edit_sql(mapper + "edit", map);
    }
}
