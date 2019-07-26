package com.scheduling.api;

import com.scheduling.DTO.MyException;
import com.scheduling.common.util.Result;
import com.scheduling.service.ifac.CommentServer;
import com.scheduling.service.ifac.UserServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
@RequestMapping({"/index"})
@Api(
        tags = {"用户首页"}
)
@Slf4j
/**
 * 王留洋
 */
public class IndexApi {

    public static final Logger logger = Logger.getLogger(IndexApi.class);
    @Resource
    private CommentServer commentServer;

    @Resource
    private UserServer userServer;

    @ApiOperation(
            value = "获取用户信息",
            notes = "获取用户信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey(openid)"),
            @ApiImplicitParam(paramType = "query", name = "comid", dataType = "String", required = true, value =
                    "公司id")
    })
    @RequestMapping(
            value = {"getUserInfo"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getUserInfo(String skey,String comid)  throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );

        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id",user_id);
        map.put("comid",comid);
        List<Map> list=userServer.listAll_sql("UserMapper.findUserINFO",map);
        return new Result().setData(list);
    }

    @ApiOperation(
            value = "用户首页排班信息",
            notes = "用户首页排班信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "startdate", dataType = "String", required = true, value = "开始日期"),
            @ApiImplicitParam(paramType = "query", name = "comid", dataType = "String", required = true, value = "公司id"),
            @ApiImplicitParam(paramType = "query", name = "enddate", dataType = "String", required = true, value = "结束日期")
    })
    @RequestMapping(
            value = {"getUserInformation"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getUserInformation(String skey,String enddate,String startdate,String comid)  throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id",user_id);
        map.put("startdate",startdate);
        map.put("enddate",enddate);
        map.put("comid",comid);
        List<Map> list=userServer.listAll_sql("UserMapper.getUserInfo",map);
        //if (null!=list&&list.size()>0){
            return new Result().setData(list);
        /*}else {
            return new Result().setData(list);
        }*/

    }

    @ApiOperation(
            value = "获取用户通知信息",
            notes = "获取用户通知信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey")
    })
    @RequestMapping(
            value = {"getNoticeById"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getNoticeById(String skey)  throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id",user_id);
        List<Map>list =userServer.listAll_sql("UserMapper.getAllNotice",map);
        return new Result().setData(list);
    }

    @ApiOperation(
            value = "删除用户通知信息",
            notes = "删除用户通知信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "noticeid", dataType = "String", required = true, value = "通知ID")
    })
    @RequestMapping(
            value = {"deleteNoticeById"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result deleteNoticeById(String skey,String noticeid)  throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("noticeid",noticeid);
        map.put("userid",user_id);
        map.put("status","2");
        userServer.edit_sql("UserMapper.clickNotice",map);
        return new Result();
    }

    @ApiOperation(
            value = "点击用户通知信息",
            notes = "点击用户通知信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "noticeid", dataType = "String", required = true, value = "通知ID"),
            @ApiImplicitParam(paramType = "query", name = "workforce_employee_id", dataType = "String", required = true, value = "员工排班ID")
    })
    @RequestMapping(
            value = {"clickNotice"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getAllNotic(String skey,String noticeid,String workforce_employee_id)  throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("noticeid",noticeid);
        map.put("status","1");
        userServer.edit_sql("UserMapper.clickNotice",map);
        Map<String,Object> map1 = new HashMap<String, Object>(  );
        map1.put("workforce_employee_id",workforce_employee_id);
        map1.put("confirm_boolean","1");
        userServer.edit_sql("WorkemployeeMapper.modifyState",map);
        return new Result();
    }

}
