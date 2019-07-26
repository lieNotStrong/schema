package com.scheduling.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scheduling.DTO.MyException;
import com.scheduling.config.TokenCacheUtil;
import com.scheduling.service.ifac.UserServer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.scheduling.common.LoginMap.loginMap;

@Transactional
@Service("loginServer")
@SuppressWarnings("rawtypes")
public class UserServerImpl extends BaseServiceImpl implements UserServer {

    String mapper = "UserMapper.";

    @Autowired
    TokenCacheUtil tokenCacheUtil;

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

    public  Map loginType(String skey) throws MyException {

        String openid = tokenCacheUtil.getOpenid(skey);
        Map map = new HashMap();
        map.put("openid",openid);
        Map byId_sql = (Map) findById_sql(mapper+"findById", map);
        return byId_sql;
    }
}
