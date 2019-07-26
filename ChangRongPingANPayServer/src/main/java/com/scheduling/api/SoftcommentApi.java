package com.scheduling.api;

import com.scheduling.DTO.MyException;
import com.scheduling.common.util.Result;
import com.scheduling.service.ifac.SoftcommentServer;
import com.scheduling.service.ifac.SoftcommentUserServer;
import com.scheduling.service.ifac.UserServer;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jnr.ffi.Struct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@EnableAutoConfiguration
@RequestMapping({"/softcomment"})
@Api(
        tags = {"评价"}
)
@Slf4j
public class SoftcommentApi {

    public static final Logger logger = Logger.getLogger(SoftcommentApi.class);
    @Resource
    private SoftcommentServer softcommentServer;

    @Resource
    private UserServer userServer;

    @Autowired
    private SoftcommentUserServer softcommentUserServer;


    /**
     * 添加评价 添加成功返回数据并显示
     * @return
     */
    @ApiOperation(
            value = "addSoftcomment",
            notes = "addSoftcomment"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "content", dataType = "String", required = true, value = "content")
    })
    @RequestMapping(
            value = {"addSoftcomment"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result addNewSchedu(
                                                String skey,
                                                String content) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );

        Map userInfo = userServer.loginType(skey);
        String userid = userInfo.get("user_id").toString();
        map.put("userid",userid);
        map.put("softcommentid",UUID.randomUUID().toString());
        map.put("content",content);
        map.put("support_num","0");
        map.put("createtime",new Date());
      //添加评价
        softcommentServer.insert(map);
        List<Map> list =softcommentServer.queryUser(map);
        return new Result().setData(list);
    }




    /**
     * 获取所有用户评价列表(按点赞数排序)
     * @return
     */
    @ApiOperation(
            value = "获取所有用户评价列表(按点赞数排序)",
            notes = "获取所有用户评价列表(按点赞数排序)"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey")

    })
    @RequestMapping(
            value = {"getSoftcomment"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getSoftcomment(
                                                String skey) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String userid = userInfo.get("user_id").toString();
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        List<Map> list1= softcommentServer.listAll_sql("SoftcommentMapper.queryUserByNum",new HashMap());


        for (int i=0; i< list1.size();i++
             ) {
            Map map2 = list1.get(i);
            Map mapsr = new HashedMap();
            mapsr.put("userid",userid);
            mapsr.put("createtime",fmt.format(date));
            mapsr.put("softcommentid",map2.get("softcommentid").toString());
            List<Map> list= softcommentServer.listAll_sql("SoftcommentMapper.queryDZJLno",mapsr);
            if (null!=list&&list.size()>0){
                map2.put("id","1");
            }else{
                map2.put("id","0");
            }
            /*for (int j=0; j< list2.size();j++
                 ) {
               Map map3 = list2.get(j);
                if (map2.get("softcommentid").toString().equals(map3.get("softcommentid").toString())) {

                    list1.get(i).put("id","1");
                }else{

                    list1.get(i).put("id","0");
                }

            }
*/
        }

       /* //List all1 = softcommentUserServer.getAll(map);
        //查所有评价
        List allTwo = softcommentServer.getAllTwo();
        //查此用户评价 按照点赞数
        List all = softcommentServer.getAll(map1);
        allTwo.removeAll(all);

        List allone = softcommentServer.getAllone(map1);
        allTwo.addAll(allone);
        map.put("list",allTwo);*/
        return new Result().setData(list1);
    }


    /**
     * 评价点赞
     * @return
     */
    @ApiOperation(
            value = "upSoftcomment",
            notes = "upSoftcomment"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "softcommentid", dataType = "String", required = true, value = "softcommentid")
    })
    @RequestMapping(
            value = {"upSoftcomment"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result upSoftcomment(
                                                String skey,
                                                String softcommentid) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );

        Map userInfo = userServer.loginType(skey);
       String userid = userInfo.get("user_id").toString();
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
        map.put("userid",userid);
        map.put("softcommentid",softcommentid);
        Date date = new Date();
        map.put("createtime",fmt.format(date));
        //点赞记录
        List<Map> list = softcommentServer.listAll_sql("SoftcommentMapper.queryDZJL",map);
        //指定评论的点赞数
        Map map3 = softcommentServer.findById_sql("SoftcommentMapper.queryDZS",map);
        //为空  添加点赞记录 点赞数+1
        // 不为空   再次点击点赞数-1  删除点赞记录
        if (list.isEmpty()) {
            Map<String,Object> map2 = new HashMap<String, Object>(  );
            map2.put("id",UUID.randomUUID().toString());
            map2.put("createtime",fmt.format(date));
            map2.put("userid",userid);
            map2.put("softcommentid",softcommentid);
            softcommentServer.insertTwo(map2);
            map3.put("support_num",String.valueOf(Integer.parseInt(map3.get("support_num").toString())+1));
            map3.put("softcommentid",softcommentid);
            map3.put("flag","1");
            softcommentServer.edit_sql("SoftcommentMapper.editNum",map3);
        }else{
            for (Map map4:list
                 ) {
                softcommentServer.delete_sql("SoftcommentMapper.deleteDZJL",map4);
                map3.put("support_num",String.valueOf(Integer.parseInt(map3.get("support_num").toString())-1));
                map3.put("softcommentid",softcommentid);
                map3.put("flag","0");
                softcommentServer.edit_sql("SoftcommentMapper.editNum",map3);

            }
        }
        return new Result().setData(map3);
    }





}
