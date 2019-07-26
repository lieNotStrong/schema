package com.scheduling.service.impl;

import com.scheduling.service.ifac.NewScheduServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("newScheduServer")
@SuppressWarnings("rawtypes")
public class NewScheduServerImpl extends BaseServiceImpl implements NewScheduServer {

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
    public List all(Map map) {
        List<Map> maps = listAll_sql(mapper + "listAll", map);
        return maps;
    }

    @Override
    public int update(Map map) {
        return edit_sql(mapper + "edit", map);
    }

    @Override
    public int saveList(List list) {
        return save_sql(mapper + "save", list);
    }
}
