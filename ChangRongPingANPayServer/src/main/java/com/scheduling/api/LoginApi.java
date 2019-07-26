package com.scheduling.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scheduling.DTO.MyException;
import com.scheduling.common.HttpClientUtils;
import com.scheduling.common.util.Result;
import com.scheduling.common.util.wechat.WechatApi;
import com.scheduling.common.util.wechat.WechatConf;
import com.scheduling.common.util.wechat.WechatTemplate;
import com.scheduling.common.util.wechat.WechatTemplateItem;
import com.scheduling.config.RedisUtil;
import com.scheduling.config.TokenCacheUtil;
import com.scheduling.service.ifac.UserServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;

@RestController
@EnableAutoConfiguration
@RequestMapping({"/login"})
@Api(
        tags = {"登录"}
)
@Slf4j
public class LoginApi {

    public static final Logger logger = Logger.getLogger(LoginApi.class);
    @Resource
    private UserServer loginServer;

    //public static Map<String,String> loginMap = new LoginMap();

    @Autowired
    TokenCacheUtil tokenCacheUtil;
    @Autowired
    RedisUtil cache;
    @Resource
    private UserServer userServer;
    /**
     * 获取公司
     * @return
     */
    @ApiOperation(
            value = "获取公司列表",
            notes = "获取公司列表"
    )
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey")
    })
    @RequestMapping(
            value = {"getUsergslist"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getUsergslist(){
        try {
            Map<String,Object> map = new HashMap<String, Object>(  );
            map.put("user_type","1");
            List<Map> list= userServer.listAll("UserMapper",map);
            return new Result().setData(list).setCode(1).setMsg_cn("成功！");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new Result().setData(e.getMessage()).setCode(0).setMsg_cn("失败！");
        }
    }
    public static JSONObject getSessionKeyOrOpenId(String code){    //微信端登录code
        String wxCode = code;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String,String> requestUrlParam = new HashMap<String, String>(  );
        requestUrlParam.put( "appid","wx7e3e5749f96ef499" );//小程序appId
        requestUrlParam.put( "secret","c24922a0fbc89096269b6cd1756ce258" );//小程序appSecret
        requestUrlParam.put( "js_code",wxCode );//小程序端返回的code
        requestUrlParam.put( "grant_type","authorization_code" );//默认参数     //发送post请求读取调用微信接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject( HttpClientUtils.post( requestUrl,requestUrlParam ));
        return jsonObject;
    }

    public static JSONObject getUserInfo(String encryptedData,String sessionKey,String iv){
        // 被加密的数据
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] dataByte = decoder.decode(encryptedData);    // 加密秘钥
        byte[] keyByte = decoder.decode(sessionKey);    // 偏移量
        byte[] ivByte = decoder.decode(iv);
        try {        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init( Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSON.parseObject(result);
            }

        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     *测试登录
     * @return
     */
    @ApiOperation(
            value = "loginC",
            notes = "loginC"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", dataType = "String", required = true, value = "phone"),
            @ApiImplicitParam(paramType = "query", name = "firmName", dataType = "String", required = false, value = "firmName"),
            @ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code"),
            @ApiImplicitParam(paramType = "query", name = "rawData", dataType = "String", required = true, value = "rawData"),
            @ApiImplicitParam(paramType = "query", name = "signature", dataType = "String", required = true, value = "signature"),
            @ApiImplicitParam(paramType = "query", name = "encrypteData", dataType = "String", required = true, value = "encrypteData"),
            @ApiImplicitParam(paramType = "query", name = "iv", dataType = "String", required = true, value = "iv"),
    })
    @RequestMapping(
            value = {"loginC"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result doLoginC(
            String phone,
            String firmName,
            String code,
            String rawData,
            String signature,
            String encrypteData,
            String iv){
        Map<String,Object> map = new HashMap<String, Object>(  );
        if (firmName!=null){
            map.put("user_type","1");
            map.put("nikename",firmName);
        }else {
            return new Result().failed("登录失败！");
        }
        if (map.get("user_type")==null){
            return new Result().failed("登录失败！");
        }
        if (phone==null){
            return new Result().failed("登录失败！");
        }
        log.info("用户非敏感信息"+rawData);
        //JSONObject rawDataJson = JSON.parseObject( rawData );
        log.info("签名"+signature);
        //JSONObject SessionKeyOpenId = getSessionKeyOrOpenId( code );
        //log.info("post请求获取的SessionAndopenId="+SessionKeyOpenId);
        String openid ;//= SessionKeyOpenId.getString("openid" );
        openid="123";
        String sessionKey ;//= SessionKeyOpenId.getString( "session_key" );
        sessionKey="456";
        log.info("openid="+openid+",session_key="+sessionKey);
        Map user = loginServer.findByOpenid( openid );
        //uuid生成唯一key
        String skey = UUID.randomUUID().toString();
        if(user==null){        //入库
            //String nickName = rawDataJson.getString( "nickName" );
            String avatarUrl ="1234";// rawDataJson.getString( "avatarUrl" );
            String gender  = "1234";// rawDataJson.getString( "gender" );
            String city = "1234";//rawDataJson.getString( "city" );
            String country = "1234";//rawDataJson.getString( "country" );
            String province = "1234";//rawDataJson.getString( "province" );

            map.put("user_id",UUID.randomUUID().toString());
            map.put("idcard_front","");
            map.put("idcard_reverse","");
            map.put("score","");
            map.put("wxnum","");
            map.put("phone",phone);
            map.put("password","123");
            map.put("openid",openid);
            map.put("createtime", new Date(  ));
            map.put("headimg", avatarUrl);
            map.put("del_boolean", "1");
//              user.setSessionkey( sessionKey );
//              user.setSkey( skey );
            loginServer.insert( map );
        }else {        //已存在
            log.info( "用户openid已存在,不需要插入" );
        }    //根据openid查询skey是否存在
        //String skey_redis = (String) redisTemplate.opsForValue().get( openid );
        String skey_redis = null;
        if(null!=cache.get(openid)){
            cache.remove(skey_redis);
            skey_redis = (String) cache.get(openid);
        }
        if(StringUtils.isNotBlank( skey_redis )){//存在 删除 skey 重新生成skey 将skey返回
            cache.remove(skey_redis);
        }        //  缓存一份新的
        JSONObject sessionObj = new JSONObject(  );
        sessionObj.put( "openId",openid );
        sessionObj.put( "sessionKey",sessionKey );
        //redisTemplate.opsForValue().set( skey,sessionObj.toJSONString() );
        //redisTemplate.opsForValue().set( openid,skey );//把新的sessionKey和oppenid返回给小程序

        cache.set(skey,sessionObj.toJSONString());
        cache.set(openid,skey);

        map.put( "skey",skey );
        map.put( "result","0" );
        JSONObject userInfo = new JSONObject();//getUserInfo( encrypteData, sessionKey, iv );
        userInfo.put("openid",openid);
        log.info("根据解密算法获取的userInfo="+userInfo);
        map.put( "userInfo",userInfo );
        return new Result().setData(map);
    }
    /*public static void main(String[] args) {
        sendMsg("oSIGH5KVEpqjwAXl4cWOqCnRtEFY","9a2bf1e55d8940e99f6a2adcd4407acb","ceshi");
    }*/
    /**
     * 发送消息
     * @return
     */
    public  String sendMsg(String openIdyg, String formId, String nikename) {
        String accessToken = WechatApi.getAccessToken();
        // 填充模板数据 （测试代码，写死）
        Map map= MapUtil.newHashMap();
        map.put("keyword1",new WechatTemplateItem(nikename));
        map.put("keyword2", new WechatTemplateItem(DateUtil.now()));
        map.put("keyword3", new WechatTemplateItem("您的排班已经调整请及时查看!"));//
        //map.put("keyword4", new WechatTemplateItem(RandomUtil.randomNumbers(10)));
        WechatTemplate wechatTemplate = new WechatTemplate()
                .setTouser(openIdyg)
                .setTemplate_id(WechatConf.templateId)
                // 表单提交场景下为formid，支付场景下为prepay_id
                .setForm_id(formId)
                // 跳转页面
                //.setPage("pages/index")
                /**
                 * 模板内容填充：随机字符
                 * 调整人{{keyword1.DATA}}
                 * 调整时间{{keyword2.DATA}}
                 * 调整内容{{keyword3.DATA}}
                 * -> {"keyword1": {"value":"xxx"}, "keyword2": ...}
                 */
                .setData(map);
        String qq=WechatApi.templateSend(accessToken, wechatTemplate);
        map.put("body",qq);
        return qq;
    }

    /**
     *企业登录
     * @return
     */
    @ApiOperation(
            value = "loginQ",
            notes = "loginQ"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", dataType = "String", required = true, value = "phone"),
            @ApiImplicitParam(paramType = "query", name = "firmName", dataType = "String", required = false, value = "firmName"),
            @ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code"),
            @ApiImplicitParam(paramType = "query", name = "formid", dataType = "String", required = true, value = "表单id"),
            @ApiImplicitParam(paramType = "query", name = "rawData", dataType = "String", required = true, value = "rawData"),
            @ApiImplicitParam(paramType = "query", name = "signature", dataType = "String", required = true, value = "signature"),
            @ApiImplicitParam(paramType = "query", name = "encrypteData", dataType = "String", required = true, value = "encrypteData"),
            @ApiImplicitParam(paramType = "query", name = "iv", dataType = "String", required = true, value = "iv"),
    })
    @RequestMapping(
            value = {"loginQ"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result doLoginQ(
            String phone,
            String firmName,
            String code,
            String rawData,
            String signature,
            String encrypteData,
            String formid,
            String iv){
        Map<String,Object> map = new HashMap<String, Object>(  );
        if (firmName!=null){
            map.put("user_type","1");
            map.put("nikename",firmName);
        }else {
            return new Result().failed("登录失败！");
        }
        if (map.get("user_type")==null){
            return new Result().failed("登录失败！");
        }
        if (phone==null){
            return new Result().failed("登录失败！");
        }
        log.info("用户非敏感信息"+rawData);
        JSONObject rawDataJson = JSON.parseObject( rawData );
        log.info("签名"+signature);
        JSONObject SessionKeyOpenId = getSessionKeyOrOpenId( code );
        log.info("post请求获取的SessionAndopenId="+SessionKeyOpenId);
        String openid = SessionKeyOpenId.getString("openid" );
        String sessionKey = SessionKeyOpenId.getString( "session_key" );
        log.info("openid="+openid+",session_key="+sessionKey);
        Map user = loginServer.findByOpenid( openid );
        //uuid生成唯一key
        String skey = UUID.randomUUID().toString();
        if(user==null){        //入库
            //String nickName = rawDataJson.getString( "nickName" );
            String avatarUrl = rawDataJson.getString( "avatarUrl" );
            String gender  = rawDataJson.getString( "gender" );
            String city = rawDataJson.getString( "city" );
            String country = rawDataJson.getString( "country" );
            String province = rawDataJson.getString( "province" );
            map.put("user_id",UUID.randomUUID().toString());
            map.put("idcard_front","");
            map.put("idcard_reverse","");
            map.put("score","");
            map.put("wxnum","");
            map.put("phone",phone);
            map.put("password","123");
            map.put("openid",openid);
            map.put("createtime", new Date(  ) );
            map.put("headimg", avatarUrl);
            map.put("del_boolean", "1");
            map.put("formid",formid);
//              user.setSessionkey( sessionKey );
//              user.setSkey( skey );
            loginServer.insert( map );
        }else {        //已存在
            user.put("formid",formid);
            loginServer.edit("UserMapper",user);
            log.info( "用户openid已存在,不需要插入" );
        }    //根据openid查询skey是否存在
        //String skey_redis = (String) redisTemplate.opsForValue().get( openid );
        String skey_redis = (String) cache.get(openid);
        if(StringUtils.isNotBlank( skey_redis )){//存在 删除 skey 重新生成skey 将skey返回
            //redisTemplate.delete( skey_redis );
            cache.remove(skey_redis);
        }        //  缓存一份新的
        JSONObject sessionObj = new JSONObject(  );
        sessionObj.put( "openId",openid );
        sessionObj.put( "sessionKey",sessionKey );
        //redisTemplate.opsForValue().set( skey,sessionObj.toJSONString() );
        //redisTemplate.opsForValue().set( openid,skey );//把新的sessionKey和oppenid返回给小程序
        cache.set(skey,sessionObj.toJSONString());
        cache.set(openid,skey);

        map.put( "skey",skey );
        map.put( "result","0" );
        //JSONObject userInfo = getUserInfo( encrypteData, sessionKey, iv );
        log.info("根据解密算法获取的userInfo="+rawDataJson);
        map.put( "userInfo",rawDataJson );
        return new Result().setData(map);
    }


    /**
     *  员工登录
     *  @return
     */
    @ApiOperation(
            value = "loginY",
            notes = "loginY"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", dataType = "String", required = true, value = "phone"),
            @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = false, value = "name"),
            @ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code"),
            @ApiImplicitParam(paramType = "query", name = "rawData", dataType = "String", required = true, value = "rawData"),
            @ApiImplicitParam(paramType = "query", name = "formid", dataType = "String", required = true, value = "表单id"),
            @ApiImplicitParam(paramType = "query", name = "signature", dataType = "String", required = true, value = "signature"),
            @ApiImplicitParam(paramType = "query", name = "encrypteData", dataType = "String", required = true, value = "encrypteData"),
            @ApiImplicitParam(paramType = "query", name = "iv", dataType = "String", required = true, value = "iv"),
    })
    @RequestMapping(
            value = {"loginY"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result doLoginY(
            String phone,
            String name,
            String code,
            String rawData,
            String signature,
            String encrypteData,
            String formid,
            String iv){
        Map<String,Object> map = new HashMap<String, Object>(  );
        if (name!=null){
            map.put("user_type","0");
            map.put("nikename",name);
        }else {
            return new Result().failed("登录失败！");
        }
        if (map.get("user_type")==null){
            return new Result().failed("登录失败！");
        }
        if (phone==null){
            return new Result().failed("登录失败！");
        }
        log.info("用户非敏感信息"+rawData);
        JSONObject rawDataJson = JSON.parseObject( rawData );
        log.info("签名"+signature);
        JSONObject SessionKeyOpenId = getSessionKeyOrOpenId( code );
        log.info("post请求获取的SessionAndopenId="+SessionKeyOpenId);
        String openid = SessionKeyOpenId.getString("openid" );
        String sessionKey = SessionKeyOpenId.getString( "session_key" );
        log.info("openid="+openid+",session_key="+sessionKey);
        Map user = loginServer.findByOpenid( openid );
        //uuid生成唯一key
        String skey = UUID.randomUUID().toString();
        if(user==null){        //入库
            //String nickName = rawDataJson.getString( "nickName" );
            String avatarUrl = rawDataJson.getString( "avatarUrl" );
            String gender  = rawDataJson.getString( "gender" );
            String city = rawDataJson.getString( "city" );
            String country = rawDataJson.getString( "country" );
            String province = rawDataJson.getString( "province" );
            map.put("user_id",UUID.randomUUID().toString());
            map.put("idcard_front","");
            map.put("idcard_reverse","");
            map.put("score","");
            map.put("wxnum","");
            map.put("phone",phone);
            map.put("password","123");
            map.put("openid",openid);
            map.put("createtime", new Date(  ) );
            map.put("headimg", avatarUrl);
            map.put("del_boolean", "1");
            map.put("formid",formid);
//              user.setSessionkey( sessionKey );
//              user.setSkey( skey );
            loginServer.insert( map );
        }else {        //已存在
            user.put("formid",formid);
            loginServer.edit("UserMapper",user);
            log.info( "用户openid已存在,不需要插入" );
        }    //根据openid查询skey是否存在
        //String skey_redis = (String) redisTemplate.opsForValue().get( openid );
        String skey_redis = (String) cache.get(openid);
        if(StringUtils.isNotBlank( skey_redis )){//存在 删除 skey 重新生成skey 将skey返回
            //redisTemplate.delete( skey_redis );
            cache.remove(skey_redis);
        }        //  缓存一份新的
        JSONObject sessionObj = new JSONObject(  );
        sessionObj.put( "openId",openid );
        sessionObj.put( "sessionKey",sessionKey );
        //redisTemplate.opsForValue().set( skey,sessionObj.toJSONString() );
        //redisTemplate.opsForValue().set( openid,skey );//把新的sessionKey和oppenid返回给小程序
        cache.set(skey,sessionObj.toJSONString());
        cache.set(openid,skey);

        map.put( "skey",skey );
        map.put( "result","0" );
       // JSONObject userInfo = getUserInfo( encrypteData, sessionKey, iv );
        //String accessToken = WechatApi.getAccessToken();
        //User userd=WeiXinUserUtil.getUserInfo(accessToken,openid);
        //log.info("根据解密算法获取的userInfo="+JSON.toJSONString(userd));
        map.put( "userInfo",JSON.toJSONString(rawDataJson));
        return new Result().setData(map);
    }


    /**
     * 获取信息
     * @return
     */
    @ApiOperation(
            value = "getUserInfo",
            notes = "getUserInfo"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey")
    })
    @RequestMapping(
            value = {"getUserInfo"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getUserInfo(
            String skey) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        map = loginServer.loginType(skey);
        return new Result().setData(map);
    }

    @ApiOperation(
            value = "getUserOpenid//登录前确认是否有注册过是否是用户,企业",
            notes = "getUserOpenid//登录前确认是否有注册过是否是用户,企业"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey")
    })
    @RequestMapping(
            value = {"getUserOpenid"},
            method = {RequestMethod.POST}
    )
    public Result getUserOpenid(
            String skey) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        return new Result().setData(map);
    }
    /**
     *查询用户信息
     * @return
     */
    @ApiOperation(
            value = "查询用户信息",
            notes = "查询用户信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code"),
            @ApiImplicitParam(paramType = "query", name = "formid", dataType = "String", required = true, value = "表单id"),
            @ApiImplicitParam(paramType = "query", name = "rawData", dataType = "String", required = true, value = "rawData"),
            @ApiImplicitParam(paramType = "query", name = "signature", dataType = "String", required = true, value = "signature"),
            @ApiImplicitParam(paramType = "query", name = "encrypteData", dataType = "String", required = true, value = "encrypteData"),
            @ApiImplicitParam(paramType = "query", name = "iv", dataType = "String", required = true, value = "iv"),
    })
    @RequestMapping(
            value = {"getuser"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getuser(
            String code,
            String rawData,
            String signature,
            String formid) throws MyException {
        //对传参进行判断,如果为空
        Map<String,Object> map = new HashMap<String, Object>(  );
        log.info("用户非敏感信息"+rawData);
        JSONObject rawDataJson = JSON.parseObject( rawData );
        log.info("签名"+signature);
        JSONObject SessionKeyOpenId = getSessionKeyOrOpenId( code );
        log.info("post请求获取的SessionAndopenId="+SessionKeyOpenId);
        String openid = SessionKeyOpenId.getString("openid" );
        String sessionKey = SessionKeyOpenId.getString( "session_key" );
        log.info("openid="+openid+",session_key="+sessionKey);
        Map user = loginServer.findByOpenid( openid );
        //uuid生成唯一key
        String skey = UUID.randomUUID().toString();
        if(user==null){
            //对象为空,不能
            user = new HashedMap();
            //为空转到企业 员工端登陆
            user.put("nikename","");
        }else {
            String user_type = (String) user.get("user_type");
            //企业登陆
            if (user_type.toString().equals("1")) {
                String skey_redis = (String) cache.get(openid);
                if(StringUtils.isNotBlank( skey_redis )){//存在 删除 skey 重新生成skey 将skey返回
                    cache.remove(skey_redis);
                }        //  缓存一份新的
                JSONObject sessionObj = new JSONObject(  );
                sessionObj.put( "openId",openid );
                sessionObj.put( "sessionKey",sessionKey );
                cache.set(skey,sessionObj.toJSONString());
                cache.set(openid,skey);
                Map userInfo =userServer.loginType(skey);
                String user_id= userInfo.get("user_id").toString();
                user.put("skey",skey );
                user.put("comid",user_id );
            }//员工登陆
            else if(user_type.toString().equals("0")){
                String skey_redis = (String) cache.get(openid);
                if(StringUtils.isNotBlank( skey_redis )){//存在 删除 skey 重新生成skey 将skey返回
                    cache.remove(skey_redis);
                }        //  缓存一份新的
                JSONObject sessionObj = new JSONObject(  );
                sessionObj.put( "openId",openid );
                sessionObj.put( "sessionKey",sessionKey );
                cache.set(skey,sessionObj.toJSONString());
                cache.set(openid,skey);
                user.put("skey",skey );
                String user_id= (String) user.get("user_id");
                map.put("userid",user_id);
                List<Map> map2= userServer.listAll_sql("UserMapper.queryCompany",map);
                user.put("companyList",map2);
            }

        }
        return new Result().setData(user);
    }

}
