package com.scheduling.service.impl;

import com.scheduling.service.ifac.SoftcommentServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("softcommentServer")
@SuppressWarnings("rawtypes")
public class SoftcommentServerImpl extends BaseServiceImpl implements SoftcommentServer {

    String mapper = "SoftcommentMapper.";

    @Override
    public Map findByOpenid(String openid) {
        Map map = new HashMap();
        map.put("openid",openid);
        map = findById_sql(mapper+"findById", map);
        return map;
    }@Override
    public Map findByid(String softcommentid) {
        Map map = new HashMap();
        map.put("softcommentid",softcommentid);
        map = findById_sql(mapper+"findById2", map);
        return map;
    }

    @Override
    public int insert(Map map) {
        return save_sql(mapper+"save", map);
    }

    @Override
    public int insertTwo(Map map) {
        return save_sql(mapper+"saveTwo", map);
    }

    @Override
    public List getAll(Map map) {
        List<Map> maps = listAll_sql(mapper + "listAll", map);
        return maps;
    }

    @Override
    public List getAllone(Map map) {
        List<Map> maps = listAll_sql(mapper + "listAll3", map);
        return maps;
    }

    @Override
    public List getAllTwo( ) {
        List<Map> maps = listAll_sql(mapper + "listAll2", null);
        return maps;
    }



    @Override
    public int update(Map map) {
        int i = edit_sql(mapper + "edit2", map);
        return i;
    }

    @Override
    public List queryUser (Map map) {
        List<Map> maps = listAll_sql(mapper + "queryUser", map);
        return maps;
    }
}
