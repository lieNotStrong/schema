package com.scheduling.api;

import com.scheduling.DTO.MyException;


import com.scheduling.common.util.Result;
import com.scheduling.service.ifac.CommentServer;
import com.scheduling.service.ifac.UserServer;


import com.sun.xml.internal.xsom.impl.scd.Iterators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.*;

@RestController
@EnableAutoConfiguration
@RequestMapping({"/comment"})
@Api(
        tags = {"评分"}
)
@Slf4j
public class CommentApi {

    public static final Logger logger = Logger.getLogger(CommentApi.class);
    @Resource
    private CommentServer commentServer;

    @Resource
    private UserServer userServer;


    @ApiOperation(
            value = "getComment",
            notes = "getComment"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey")
    })
    @RequestMapping(
            value = {"getComment"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getComment(
            String skey) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();

        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("comuserid", user_id);
        List all = commentServer.getAll(map);
        map.put("list", map);
        return new Result().setData(map);
    }


    @ApiOperation(
            value = "查询用户历史评分 员工端",
            notes = "查询用户历史评分 员工端"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey")
    })
    @RequestMapping(
            value = {"getListAll"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getListAll(
            String skey) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();

        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id", user_id);
        List<Map> list = commentServer.listAll_sql("CommentMapper.getListAll", map);
        return new Result().setData(list);

    }


    @ApiOperation(
            value = "获取上班员工评分",
            notes = "获取上班员工评分"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "workforce_date", dataType = "String", required = false, value
                    = "workforce_date"),
    })
    @RequestMapping(
            value = {"getYesterdayComment"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getYesterdayComment(
            String skey,String workforce_date) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map3 = new HashMap<String, Object>();
        Map<String, Object> map4 = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        if (workforce_date == null || workforce_date.length()<= 0) {
            //查找前一天
            map.put("companyuserid", companyuserid);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String systemTime = simpleDateFormat.format(new Date());
            String workforce_date1T = simpleDateFormat.format(new Date().getTime() - 24 * 60 * 60 * 1000);
            map.put("workforce_date", workforce_date1T);

            List<Map> list = commentServer.listAll_sql("CommentMapper.queryNeedComment", map);
            List<Map> list2 = commentServer.listAll_sql("CommentMapper.queryCommentneByTime", map);
            if (list2.size() == 0) {
            if (list.size() != 0) {
                for (Map map2 : list
                        ) {
                    map4.put("workforce_employee_id", map2.get("workforce_employee_id").toString());
                    map4.put("userid", map2.get("userid").toString());
                    map4.put("comuserid", companyuserid);
                    map4.put("createtime", systemTime);
                    map4.put("comment_id", UUID.randomUUID().toString());
                    map4.put("score","0.0");
                    map4.put("workforce_date",workforce_date1T);
                    map4.put("content","");
                    commentServer.save_sql("CommentMapper.saveNeedComment",map4);
                }
                List<Map> list3 = commentServer.listAll_sql("CommentMapper.queryCommentneByTime", map);
                map3.put("list",list3 );
            }else {
                map3.put("working",null);
            }
            }else {
                map3.put("list",list2 );
            }

        }else {
            //查找某一天
            map1.put("companyuserid", companyuserid);
            map1.put("workforce_date", workforce_date);
            List<Map> list = commentServer.listAll_sql("CommentMapper.queryCommentneByTime", map1);
            map3.put("list",list);
        }
        return new Result().setData(map3);
    }




    @ApiOperation(
            value = "修改员工评分",
            notes = "修改员工评分"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "list", dataType = "List", required = true, value = "list"),
    })
    @RequestMapping(
            value = {"insertYesterdayComment"},
            method = {RequestMethod.POST}
    )
    @Transactional
    public Result insertYesterdayComment(
            String skey, @RequestBody List<Map> list) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("comuserid", user_id);
        for (int i = 0; i <list.size(); i++) {
            for (Map map2 : list
                    ) {
                map1.put("score", map2.get("score"));
                map1.put("comment_id", map2.get("comment_id"));
                int edit = commentServer.edit_sql("CommentMapper.editComment", map1);
            }
        }
        return new Result();
    }


    @ApiOperation(
            value = "修改员工平均分",
            notes = "修改员工平均分"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            //list 里面有userid
            @ApiImplicitParam(paramType = "query", name = "list", dataType = "List", required = true, value = "list"),
    })
    @RequestMapping(
            value = {"updateUserAvg"},
            method = {RequestMethod.POST}
    )
    @Transactional
    public Result updateUserAvg(
            String skey, @RequestBody List<Map> list) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();

        Map userInfo = userServer.loginType(skey);
        int a = 0 ;
        String user_id = userInfo.get("user_id").toString();
        map.put("comuserid", user_id);
        for (int i = 0; i <list.size(); i++) {
            String userid = list.get(i).get("userid").toString();
            map.put("userid", userid);
            map1 =commentServer.findById_sql("CommentMapper.queryUserSorce",map);
            for (int j = 0 ; j < map1.size();j++) {
                a = a + Integer.parseInt(map.get(i).toString());
            }
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String num = df.format((float)a/list.size());//返回的是String类型
            map.put("score",num);
            commentServer.save_sql("CommentMapper.saveComuser",map);
        }


        return new Result();
    }




    @ApiOperation(
            value = "查询用户评分 可通过时间",
            notes = "查询用户评分 可通过时间"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "workforce_date", dataType = "String", required = false, value
                    = "workforce_date")
    })
    @RequestMapping(
            value = {"getCommentByTime"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getCommentByTime(
            String skey, String workforce_date) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();

        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("comuserid", user_id);
        map.put("workforce_date", workforce_date);
        List<Map> list = commentServer.listAll_sql("CommentMapper.queryCommentneByTime", map);
        return new Result().setData(list);

    }

    @ApiOperation(
            value = "修改员工评分",
            notes = "修改员工评分"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "list", dataType = "List", required = true, value =
                    "list")

    })
    @RequestMapping(
            value = {"updateComment"},
            method = {RequestMethod.POST}
    )
    @Transactional
    public Result updateComment(
            String skey,
            @RequestBody List<Map> list

    ) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("comuserid", user_id);

        for (int i = 0; i <list.size(); i++) {
            for (Map map2 : list
                    ) {
                map1.put("score", map2.get("score"));
                map1.put("comment_id", map2.get("comment_id"));
                int edit = commentServer.edit_sql("CommentMapper.editComment", map1);
            }
        }
        return new Result();
    }


}
