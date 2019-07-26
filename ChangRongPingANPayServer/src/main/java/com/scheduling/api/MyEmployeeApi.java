package com.scheduling.api;

import com.scheduling.DTO.MyException;
import com.scheduling.common.util.Result;
import com.scheduling.common.util.UploadFile;
import com.scheduling.service.ifac.UserServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@EnableAutoConfiguration

@RequestMapping({"/myemployee"})
@Api(
        tags = {"我的员工"}
)
@Slf4j
public class MyEmployeeApi {

    public static final Logger logger = Logger.getLogger(CommentApi.class);


    @Resource
    private UserServer userServer;



    /**
     * 查询员工姓名 状态  职位  综合评分
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "获取员工姓名 状态 职位 评分",
            notes = "getEmploye "
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),

    })
    @RequestMapping(
            value = {"getEmploye"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getComuser(
            String skey
               ) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        map.put("companyuserid",companyuserid);

        List<Map> all = userServer.listAll_sql("MyEmployeeMapper.findByCidU",map);
        map.put("list",all);
        return new Result().setData(map);
    }

    /**
     * 查询员工个人信息（入职离职时间 身份证正反面）
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "获取员工个人信息 ",
            notes = "getEmployeInformations"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "userid", dataType = "String", required = true, value = "userid"),

    })
    @RequestMapping(
            value = {"getEmployeInformations"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getComuserInformationst(
            String skey,
            String userid) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        map.put("companyuserid",companyuserid);
        map.put("userid",userid);

        List<Map> all = userServer.listAll_sql("MyEmployeeMapper.findByUid",map);
        map.put("list",all);
        return new Result().setData(map);
    }


    /**
     * 修改员工个人信息（入职离职时间 ）
     * 如果离职日期小于当前时间  则修改为离职状态
     * 若大于等于当前时间则  维持不变
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "修改员工个人信息（若离职时间小于系统时间 会修改员工状态）",
            notes = "updateEmployeInformations"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "userid", dataType = "String", required = true, value = "userid"),
            @ApiImplicitParam(paramType = "query", name = "entrytime", dataType = "String", required = true, value = "entrytime"),
            @ApiImplicitParam(paramType = "query", name = "exittime", dataType = "String", required =false, value =
                    "exittime"),
            @ApiImplicitParam(paramType = "query", name = "positionid", dataType = "String", required = false, value =
                    "positionid")

    })
    @RequestMapping(
            value = {"updateEmployeInformations"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result updateComuserInformationst(
            String skey,
            String userid,
            String entrytime,
            String exittime,
            String positionid
           )
            throws
            MyException, ParseException ,Exception{
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        map.put("companyuserid",companyuserid);
        map.put("userid",userid);
        map.put("entrytime",entrytime);
        map.put("exittime",exittime);
        map.put("positionid",positionid);

        int one = userServer.edit_sql("MyEmployeeMapper.editUser",map);
        System.out.println(one);
        /*如果离职日期小于当前时间  则修改为离职状态
          若大于等于当前时间则  维持不变*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String systemdate = simpleDateFormat.format(new Date());
        try {
            Date systemDate = simpleDateFormat.parse(systemdate);
            System.out.println(systemDate);
            Date comuserExittime = simpleDateFormat.parse(exittime);
            System.out.println(comuserExittime);
            if (systemDate.getTime() > comuserExittime.getTime()) {
                int two = userServer.edit_sql("MyEmployeeMapper.editActiveY",map);
                System.out.println(two);
            }else {
                int there = userServer.edit_sql("MyEmployeeMapper.editActiveN",map);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Result();
    }
    /**
     * 修改员工个人信息（身份证正面）
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "修改员工个人信息（身份证正面）",
            notes = "updateEmployeInformationsF"
    )

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "userid", dataType = "String", required = true, value = "userid"),
            @ApiImplicitParam(paramType = "query", name = "idcard_front", dataType = "MultipartFile", required = true,
                    value = "idcard_front"),


    })
    @RequestMapping(
            value = {"updateEmployeInformationsImgF"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result updateComuserInformationstF(
            String skey, String userid, @RequestParam(value = "idcard_front") MultipartFile idcard_front,
            HttpServletRequest request)
            throws
            MyException, ParseException ,Exception{
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        map.put("companyuserid",companyuserid );
        map.put("userid",userid);
        String  idcard_front1 = new UploadFile().uploadFile(request,idcard_front);
        map.put("idcard_front",idcard_front1);


        int one = userServer.edit_sql("MyEmployeeMapper.editUserImgF",map);
        System.out.println(one);


        return new Result();
    }
    /**
     * 修改员工个人信息（身份证反面）
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "修改员工个人信息（身份证反面）",
            notes = "updateEmployeInformationS"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "userid", dataType = "String", required = true, value = "userid"),

            @ApiImplicitParam(paramType = "query", name = "idcard_reverse", dataType = "MultipartFile", required = true, value = "idcard_reverse"),

    })
    @RequestMapping(
            value = {"updateEmployeInformationsImgS"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result updateComuserInformationstS(
            String skey, String userid, @RequestParam(value = "idcard_reverse")MultipartFile idcard_reverse, HttpServletRequest request)
            throws
            MyException, ParseException ,Exception{
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        map.put("companyuserid",companyuserid );
        map.put("userid",userid);

        String  idcard_reverse1 = new UploadFile().uploadFile(request,idcard_reverse);
        map.put("idcard_reverse",idcard_reverse1);

        int one = userServer.edit_sql("MyEmployeeMapper.editUserImgS",map);
        System.out.println(one);


        return new Result();
    }

    /**
     * 修改员工状态  是否离职
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "修改员工状态 是否离职",
            notes = "updateEmployeActive"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "userid", dataType = "String", required = true, value = "userid"),
            @ApiImplicitParam(paramType = "query", name = "if_active", dataType = "String", required = true, value = "if_active"),

    })
    @RequestMapping(
            value = {"updateEmployeActive"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result updateComuserActive(
            String skey,String userid,String if_active) throws MyException, ParseException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        map.put("userid",userid);
        map.put("if_active",if_active);
        int one = userServer.edit_sql("MyEmployeeMapper.editActiveTwo",map);
        System.out.println(one);
        return new Result();
    }


    /**
     * 查询公司职位
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "获取公司职位",
            notes = "getPositisdon"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),

    })
    @RequestMapping(
            value = {"getPositisdon"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getPositisdon(
            String skey) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        map.put("comuserid",companyuserid);
        List<Map> all = userServer.listAll_sql("MyEmployeeMapper.findByCidP",map);
        map.put("list",all);
        return new Result().setData(map);
    }

    /**
     * 添加公司职位
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "添加公司职位",
            notes = "addPositisdon"
    )
    @ApiImplicitParams({

            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),

            @ApiImplicitParam(paramType = "query", name = "position_name", dataType = "String", required = true, value = "position_name")



    })
    @RequestMapping(
            value = {"addPositisdon"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result addPositisdon(
            String skey,

            String position_name

    ) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();

        Map userInfo =userServer.loginType(skey);

        String companyuserid = userInfo.get("user_id").toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String systemdate = simpleDateFormat.format(new Date());
        map.put("comuserid",companyuserid);
        map.put("position_name",position_name);
        //判断公司职位是否重复
       List<Map> all=userServer.listAll_sql("MyEmployeeMapper.finddim",map);
        for (int i =0;i<all.size();i++) {
            String  name=all.get(i).get("position_name").toString();
            if (name.equals(position_name) ){

                return new Result().setCode(0).setMsg_cn("名称重复,添加失败");

            }
        }

                map.put("comuserid", companyuserid);
                map.put("positionid", UUID.randomUUID().toString());
                map.put("position_name", position_name);
                map.put("createtime", systemdate);
                map.put("del_boolean", "2");
                int one = userServer.save_sql("MyEmployeeMapper.savePositisdon", map);
                System.out.println(one);


        return new Result().setCode(1).setMsg_cn("添加成功");
    }
    /**
     * 删除公司职位
     * HWQ
     * @return
     */
    @ApiOperation(
            value = "删除公司职位",
            notes = "deletePositisdon"
    )
    @ApiImplicitParams({

            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "positionid", dataType = "String", required = true, value = "positionid"),


    })
    @RequestMapping(
            value = {"deletePositisdon"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result deletePositisdon(
            String skey,String positionid

    ) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        Map userInfo =userServer.loginType(skey);
        String companyuserid= userInfo.get("user_id").toString();
        map.put("comuserid",companyuserid);
        map.put("positionid",positionid);
        map.put("del_boolean","1");
        List<Map> all =   userServer.listAll_sql("MyEmployeeMapper.findByCidUT",map);
        System.out.println(all);
        if (all.isEmpty()) {
            map2.put("delete","ture");
            int two = userServer.edit_sql("MyEmployeeMapper.updatePositisdon",map);
        }else {
            for (Map if_actives:all ) {
                for (Object value:if_actives.values()) {
                    System.out.println(value);
                    String one = value.toString();
                    System.out.println(one);
                    System.out.println(one.indexOf("1") < (int)0);
                    //如果包含1 则职位下有员工未离职  返回大于等于0的数
                    if (one.indexOf("1") < (int)0) {
                        System.out.println("可以删除");
                        int two = userServer.edit_sql("MyEmployeeMapper.updatePositisdon",map);
                        map2.put("delete","ture");
                    }else {
                        System.out.println("删除失败");
                        map2.put("delete","false");
                    }
                }
            }
        }

        return new Result().setData(map2);

    }
    @ApiOperation(
            value = "修改公司职位",
            notes = "editPositisdon"
    )
    @ApiImplicitParams({

            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "positionid", dataType = "String", required = true, value =
                    "positionid"),
            @ApiImplicitParam(paramType = "query", name = "position_name", dataType = "String", required = true, value = "position_name"),


    })
    @RequestMapping(
            value = {"editPositisdon"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result editPositisdon(
            String skey,
            String positionid,
            String position_name) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map userInfo =userServer.loginType(skey);
        String companyuserid = userInfo.get("user_id").toString();
        map.put("comuserid",companyuserid);
        map.put("position_name",position_name);
        //判断公司职位是否重复
        List<Map> all=userServer.listAll_sql("MyEmployeeMapper.finddim",map);
        for (int i =0;i<all.size();i++) {
            String  name=all.get(i).get("position_name").toString();
            if (name.equals(position_name) ){

                return new Result().setCode(0).setMsg_cn("职位名称重复,修改失败");

            }
        }
        map.put("positionid",positionid);
        map.put("position_name",position_name);
        int edit=userServer.edit_sql("MyEmployeeMapper.edit",map);

        return new Result().setCode(1).setMsg_cn("修改成功 ");
    }





    /**
     * 邀请员工与公司进行绑定
     * @param skey
     * @param comid
     * @return
     */
    @ApiOperation(
            value = "邀请新员工",
            notes = "邀请新员工"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "code"),
            @ApiImplicitParam(paramType = "query", name = "comid", dataType = "String", required = true, value = "公司ID")
    })
    @RequestMapping(
            value = {"companyInvitation"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result companyInvitation(String comid,String skey )throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map userInfo =userServer.loginType(skey);
  //      Map companyInfo =userServer.loginType(skeyQ);
        String user_id= userInfo.get("user_id").toString();
    //    String comid= companyInfo.get("user_id").toString();
        map.put("comuserid",comid);
        List<Map> list=userServer.listAll_sql("MyEmployeeMapper.findByCidP",map);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String systemdate = simpleDateFormat.format(new Date());
        String positionid="";
        int count=0;
        for(int i=0;i<list.size();i++){
            String  position_name=list.get(i).get("position_name").toString();
            if("无职位".equals(position_name)){
                count++;
                positionid=list.get(i).get("positionid").toString();
            }
        }
        if(count==0){
            map.put("comuserid",comid);
            map.put("position_name","无职位");
            positionid=UUID.randomUUID().toString();
            map.put("positionid",positionid);
            map.put("createtime",systemdate);
            map.put("del_boolean","2");
            userServer.save_sql("MyEmployeeMapper.savePositisdon",map);
        }
        //判断员工是否与本公司已经绑定  防止重复添加
        Map<String, Object> mapComuser = new HashMap<String, Object>();
        mapComuser.put("comuserid",comid);
        mapComuser.put("userid",user_id);
        Map ismap = userServer.findById_sql("MyEmployeeMapper.findUserHaveComuser",mapComuser);
        if (ismap == null) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("com_userid",UUID.randomUUID().toString());
            map1.put("companyuserid",comid);
            map1.put("userid",user_id);
            map1.put("positionid",positionid);
            map1.put("entrytime",systemdate);
            map1.put("exittime","");
            map1.put("if_active","1");
            map1.put("idcard_front","");
            map1.put("idcard_reverse","");
            map1.put("score","0.0");
            userServer.save_sql("MyEmployeeMapper.saveComuser",map1);
        }
        return new Result();
    }

}







