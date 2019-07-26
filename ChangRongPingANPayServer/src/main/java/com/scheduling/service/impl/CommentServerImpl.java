package com.scheduling.service.impl;

import com.scheduling.service.ifac.CommentServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("commentServer")
@SuppressWarnings("rawtypes")
public class CommentServerImpl extends BaseServiceImpl implements CommentServer {

    String mapper = "SoftcommentMapper.";

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
    public List getAll(Map map) {
        List<Map> maps = listAll_sql(mapper + "listAll", null);
        return maps;
    }

    @Override
    public int update(Map map) {
        int i = edit_sql(mapper + "edit", map);
        return i;
    }
   public int insert1(List list){
       return save_sql(mapper+"save", list);
   }
}
