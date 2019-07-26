package com.scheduling.config;

import com.alibaba.fastjson.JSONObject;
import com.scheduling.DTO.MyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class TokenCacheUtil {
    @Resource
    RedisUtil  cache;

    private boolean debug=true;

    private static final String CACHE_KEY_PREFIX = "user:token:app:";

    private static final Integer[] BOSS_UID = new Integer[]{
            271498,
            427928,
            41458,
            246527,
            427190,
            393068,
            309,
            427837,
            427076,
            425074,
            710274,
            1066,
            832086,
            832082,
            832079,
            832081,
            832084,
            832078,
            832083,
            832080,
            832087,
            832092,
            832091,
            832094,
            832085,
            832265,
            832089,
            708432,
            708910
    };

    private String getCacheKeyByUserId(String userId) {
        return CACHE_KEY_PREFIX + userId.toString();
    }

    public void saveToken(String token, String userId) {
        cache.set(getCacheKeyByUserId(userId), token);
        //去掉app登陆token失效问题
        cache.expire(getCacheKeyByUserId(userId), Integer.MAX_VALUE);
    }

    public boolean checkTokenExists(String token, String userId) {
        // 老板uid直接跳过验证,这里跳过验证会有安全风险

        for (Integer uid : BOSS_UID) {
            if (uid.equals(userId)) {
                return true;
            }
        }

        // 测试环境跳过
        if (debug) {
            return true;
        }

        String redisToken = (String)cache.get(getCacheKeyByUserId(userId));
        return redisToken != null && redisToken.equals(token);
    }


    public  String getOpenid(String skey) throws MyException {

        String sessionObjStr = (String) cache.get(skey);
        if(!StringUtils.isNotBlank( sessionObjStr )){//存在 删除 skey 重新生成skey 将skey返回
            //不存在
            throw new MyException("您还没有登录,请先登录");
        }        //  缓存一份新的
        JSONObject jsonObject = JSONObject.parseObject(sessionObjStr);
        String openId = jsonObject.getString("openId");
        return openId;
    }
    public  String getOpenidstr(String skey){
        String openId="";
        String sessionObjStr = (String) cache.get(skey);
        if(!StringUtils.isNotBlank( sessionObjStr )){//存在 删除 skey 重新生成skey 将skey返回
            //不存在
            openId = "-1";
        }else{
            //  缓存一份新的
            JSONObject jsonObject = JSONObject.parseObject(sessionObjStr);
            openId = jsonObject.getString("openId");
        }
        return openId;
    }
}
