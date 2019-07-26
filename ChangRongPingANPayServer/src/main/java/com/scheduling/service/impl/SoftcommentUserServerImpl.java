package com.scheduling.service.impl;

import com.scheduling.service.ifac.SoftcommentServer;
import com.scheduling.service.ifac.SoftcommentUserServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("softcommentUserServer")
@SuppressWarnings("rawtypes")
public class SoftcommentUserServerImpl extends BaseServiceImpl implements SoftcommentUserServer {

    String mapper = "SoftcommentuserMapper.";


    @Override
    public int insert(Map map) {
        return save_sql(mapper+"save", map);
    }



    @Override
    public int del(Map map) {
        int i = edit_sql(mapper + "delete", map);
        return i;
    }

    @Override
    public List getAll(Map map) {
        List<Map> maps = listAll_sql(mapper + "listAll", map);
        return maps;
    }

    @Override
    public Map fanById(Map map) {
        Map byId_sql = findById_sql(mapper + "findById", map);
        return byId_sql;
    }
}
