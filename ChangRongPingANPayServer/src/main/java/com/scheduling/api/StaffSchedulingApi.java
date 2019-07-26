package com.scheduling.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.scheduling.DTO.MyException;
import com.scheduling.DTO.WorkCompany;
import com.scheduling.common.TimeUtil;
import com.scheduling.common.util.Result;
import com.scheduling.common.util.StringUtil;
import com.scheduling.service.ifac.NewScheduServer;
import com.scheduling.service.ifac.NoticeServer;
import com.scheduling.service.ifac.UserServer;
import com.scheduling.service.ifac.WorkemployeeServer;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javafx.scene.control.cell.MapValueFactory;
import jnr.ffi.Struct;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.awt.geom.FlatteningPathIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@EnableAutoConfiguration
@RequestMapping({"/staffSchedu"})
@Api(
        tags = {"员工排班"}
)
@Slf4j
public class StaffSchedulingApi {

    public static final Logger logger = Logger.getLogger(StaffSchedulingApi.class);
    @Resource
    private NewScheduServer newScheduServer;

    @Resource
    private UserServer userServer;

    @Resource
    private WorkemployeeServer workemployeeServer;

    @Resource
    private NoticeServer noticeServer;

    @Resource
    private LoginApi loginApi;

    /**
     * 企业获取保存 或者 发布的排班表
     * 因为只能发布下周排班
     * 先获取下周排班  若无保存或发布
     * 再获取本周排班  若无保存或发布
     * 不显示
     *
     * @return
     */
    @ApiOperation(
            value = "企业端 发布新排班模块 查看排班记录",
            notes = "企业端 发布新排班模块 查看排班记录"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
//            @ApiImplicitParam(paramType = "query", name = "sametable", dataType = "String", required = true, value = "sametable"),

            @ApiImplicitParam(paramType = "query", name = "startdate", dataType = "String", required = false, value =
                    "开始日期"),
            @ApiImplicitParam(paramType = "query", name = "enddate", dataType = "String", required = false, value =
                    "结束日期")
    })
    @RequestMapping(
            value = {"getScheduForCompany"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getScheduForCompany(
            String skey,

            String startdate,
            String enddate
            /*, String sametable*/) throws MyException {
        Map<String, Object> workemployeeConditionMap = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String comid = userInfo.get("user_id").toString();
        workemployeeConditionMap.put("userid", comid);
        if (startdate == null && enddate == null ) {

            List<Map> schedfactlist = workemployeeServer.listAll_sql("ScheduMapper.listAllAgain", workemployeeConditionMap);
            return new Result().setData(schedfactlist);
        }
        workemployeeConditionMap.put("startdate", startdate);
        workemployeeConditionMap.put("enddate", enddate);
        List<Map> schedfactlist = workemployeeServer.listAll_sql("ScheduMapper.listAll", workemployeeConditionMap);

        return new Result().setData(schedfactlist);
    }


    /*
 * 保存新排班
 *
 * @return
 */
    @ApiOperation(
            value = "企业端 企业保存新排班",
            notes = "企业端 企业保存新排班"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "workList", dataType = "List", required = true, value = "workList"),
            @ApiImplicitParam(paramType = "query", name = "formids", dataType = "String", required = false, value = "formids")
    })
    @RequestMapping(
            value = {"svaeNewSchedu"},
            method = {RequestMethod.POST}
    )
    @Transactional
    public Result svaeNewSchedu(
            String skey, @RequestBody List<WorkCompany> workList) throws MyException {

        //      Map<String, Object> map3 = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String userid = userInfo.get("user_id").toString();
        //       map3.put("userid", userid);

        map.put("sametable", UUID.randomUUID().toString());
        map.put("createtime", new Date().toString());
        map.put("userid", userid);
//        map.put("final", "1");
        map.put("flag", "0");
        System.out.println(workList);

        for (int i = 0; i < workList.size(); i++) {
            map.put("workforce_date", workList.get(i).getWorkforce_date());
            map.put("starttime", workList.get(i).getStarttime());
            map.put("endtime", workList.get(i).getEndtime());
            map.put("positionid", workList.get(i).getPositionid());
            if (workList.get(i).getNum().equals("0")) {
                map.put("num", "9999");
            } else {
                map.put("num", workList.get(i).getNum());
            }
            String workforce_company_id = UUID.randomUUID().toString();
            map.put("workforce_company_id", workforce_company_id);
            //将排班保存至sd_workforce_company
            newScheduServer.insert(map);
            //将排班保存至sd_workforce_company_copy
            newScheduServer.listAll_sql("ScheduMapper.saveAgain", map);
        }
        return new Result();
    }

    /**
     * 企业端发布新排班
     *
     * @return
     */
    @ApiOperation(
            value = "企业端 企业发布新排班",
            notes = "企业端 企业发布新排班"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "data", dataType = "Map", required = true, value =
                    "data"),

//            @ApiImplicitParam(paramType = "query", name = "formids", dataType = "String", required = true, value =
//                    "formids"),
//            @ApiImplicitParam(paramType = "query", name = "workList", dataType = "List", required = true, value = "workList")

    })
    @RequestMapping(
            value = {"addNewSchedu"},
            method = {RequestMethod.POST}
    )
    @Transactional
    public Result addNewSchedu(
//          String skey,@RequestBody String formids,@RequestBody List<WorkCompany> workList) throws MyException {
            String skey, @RequestBody Map data) throws MyException {
        Map userInfo = userServer.loginType(skey);
        String comid = userInfo.get("user_id").toString();
        Map<String, Object> companyMap = new HashMap<String, Object>();
        Map dataValue = (Map) JSONObject.fromObject(data.get("data"));
        List<WorkCompany> workList = (List<WorkCompany>) JSONArray.parseArray(dataValue.get("workList") + "", WorkCompany.class);
        String startdate = dataValue.get("startDate").toString();
        String enddate = dataValue.get("endDate").toString();
        companyMap.put("userid", comid);
        companyMap.put("jstime",enddate );
        companyMap.put("kstime",startdate );
        companyMap.put("flag", "0");
        //查询保存的排班
        List<Map> schedfactlist = workemployeeServer.all(companyMap);
        if (schedfactlist.size() != 0 && !schedfactlist.isEmpty() && schedfactlist != null) {
            //删除sd_workforce_company中保存排班
            newScheduServer.delete_sql("ScheduMapper.deleteCompanyJL", companyMap);
            //删除d_workforce_company_copy中保存排班
            newScheduServer.delete_sql("ScheduMapper.deleteCompanyCopyJL", companyMap);
        }
        Map<String, Object> iscompanyMap = new HashMap<String, Object>();
        iscompanyMap.put("userid", comid);
        iscompanyMap.put("jstime",enddate );
        iscompanyMap.put("kstime",startdate );
        iscompanyMap.put("flag", "1");
        //查询发布的排班
        List<Map> schedfactlistAagin = workemployeeServer.all(companyMap);
        if (schedfactlist.size() != 0 && !schedfactlist.isEmpty() && schedfactlist != null){
            return new Result().setCode(0);
        }
        //       String formids = dataValue.get("formids")+"";
        //       String[] split =formids.split(",");
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("userid", comid);

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map.put("sametable", UUID.randomUUID().toString());
        map.put("createtime", new Date().toString());
        map.put("userid", comid);
        //    map.put("final", "1");
        map.put("flag", "1");
        //    Map<String, Object> workemployeeConditionMap = new HashMap<String, Object>();
        for (int i = 0; i < workList.size(); i++) {

            map.put("workforce_date", workList.get(i).getWorkforce_date());
            map.put("starttime", workList.get(i).getStarttime());
            map.put("endtime", workList.get(i).getEndtime());
            map.put("positionid", workList.get(i).getPositionid());
            if (workList.get(i).getNum().equals("0")) {
                map.put("num", "9999");
            } else {
                map.put("num", workList.get(i).getNum());
            }
            String workforce_company_id = UUID.randomUUID().toString();
            map.put("workforce_company_id", workforce_company_id);
            //添加新发布的排班到sd_workforce_company
            newScheduServer.insert(map);
            //添加新发布的排班到sd_workforce_company_copy
            newScheduServer.save_sql("ScheduMapper.saveAgain", map);

        }
        //增加通知
        // 查找公司所有员工
        List<Map> list1 = userServer.listAll_sql("UserMapper.queryUserId", map3);
       String openIdyg="";
        String formid="";
        List<Map> list2 =userServer.listAll_sql("UserMapper.findNikename",map3);
        //       String nikename = userInfo.get("nikename").toString();
//        LoginApi loginApi = new LoginApi();
        Map map2 = new HashMap();
        for (int i = 0; i < list1.size(); i++) {
            Map map4 = (Map) list1.get(i);
            map2.put("title", "系统通知");
            map2.put("createtime", new Date().toString());
            map2.put("content", "有新的排班信息！");
            map2.put("status", "0");
            map2.put("noticeid", UUID.randomUUID().toString());
            //获取员工id
            map2.put("userid", map4.get("user_id").toString());
            noticeServer.insert(map2);
         //  openIdyg=map4.get("openid").toString();
        //   formid=split[i];
        //  formid=map4.get("formid").toString();
         //  System.out.println("formid:"+formid);
        //   loginApi.sendMsg(openIdyg,formid,nikename);
        }
        map1.put("sametable", map.get("sametable"));
        return new Result().setData(map1);
        //返回一个多sametable的map
    }


    /**
     * 员工获取新发布的排班
     *
     * @return
     */
    @ApiOperation(
            value = "员工端 员工获取新发布的排班",
            notes = "员工端 员工获取新发布的排班"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
           /* @ApiImplicitParam(paramType = "query", name = "sametable", dataType = "String", required = true, value = "sametable"),*/
            @ApiImplicitParam(paramType = "query", name = "comid", dataType = "String", required = true, value =
                    "comid"),
            @ApiImplicitParam(paramType = "query", name = "startdate", dataType = "String", required = false, value =
                    "开始日期"),
            @ApiImplicitParam(paramType = "query", name = "enddate", dataType = "String", required = false, value =
                    "结束日期")
    })
    @RequestMapping(
            value = {"getNewSchedu"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getNewSchedu(
            String skey,
            String comid,
            String startdate,
            String enddate/*, String sametable*/) throws MyException {
        Map<String, Object> workemployeeConditionMap = new HashMap<String, Object>();
        Map<String, Object> useConditionMap = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();

        Map userInfo = userServer.loginType(skey);
        String userid = userInfo.get("user_id").toString();
        useConditionMap.put("userid", userid);
        useConditionMap.put("comid", comid);
        Map useMap = userServer.findById_sql("UserMapper.findUseridPid", useConditionMap);
        //不传入时间默认显示最新的排班
        if (startdate == null && enddate == null) {
            workemployeeConditionMap.put("userid", comid);
            //Map sgsf=userServer.findById_sql("ScheduMapper.listAll",map);
            //map.put("sametable", sametable);
            //map.put("sametable", sgsf.get("sametable"));

            workemployeeConditionMap.put("flag", "1");
            if (useMap == null) {
                return new Result().setCode(0);
            } else {
                //只获取本职位的排班
                workemployeeConditionMap.put("positionid", useMap.get("positionid").toString());
            }


//        List schednullist = newScheduServer.all(map);
//        map.put("confirm_boolean", "1");
            List<Map> schedfactlist = workemployeeServer.listAll_sql("ScheduMapper.listAllAgain", workemployeeConditionMap);

//        map.put("schednullist", schednullist);

            map.put("schedfactlist", schedfactlist);
        }else{
            workemployeeConditionMap.put("userid", comid);
            workemployeeConditionMap.put("flag", "1");
            workemployeeConditionMap.put("kstime", startdate);
            workemployeeConditionMap.put("jstime", enddate);
            if (useMap == null) {
                return new Result().setCode(0);
            } else {
                //只获取本职位的排班
                workemployeeConditionMap.put("positionid", useMap.get("positionid").toString());
            }
            List<Map> schedfactlist = workemployeeServer.listAll_sql("ScheduMapper.listAll", workemployeeConditionMap);
            map.put("schedfactlist", schedfactlist);
        }



        return new Result().setData(map);
    }


    /**
     * 员工选排班
     * 分别添加实际排班  和 系统排班
     *
     * @return
     */
    @ApiOperation(
            value = "员工端 员工选排班",
            notes = "员工端 员工选排班"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "comuserid", dataType = "String", required = true, value =
                    "comuserid"),
            @ApiImplicitParam(paramType = "query", name = "workforce_company_ids", dataType = "String", required = true, value = "workforce_company_ids")
    })
    @RequestMapping(
            value = {"addWorkemploy"},
            method = {RequestMethod.POST}
    )
    @Transactional
    public Result addWorkemploy(
            String skey,
            String comuserid,
            String workforce_company_ids) throws MyException {

        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        String[] split = workforce_company_ids.split(",");

        for (int i = 0; i < split.length; i++) {
            String workforce_company_id = split[i];
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("workforce_company_id", workforce_company_id);
            map.put("flag", "1");

            //查出公司发布的当周排班记录
            List all = newScheduServer.all(map);
            Map newtable = all.size() > 0 ? (Map) all.get(0) : null;
            if (newtable == null)
                return new Result().failed("不可选！");
            String starttime = newtable.get("starttime").toString();
            String endtime = newtable.get("endtime").toString();
            String sametable = newtable.get("sametable").toString();
            String workforce_date =newtable.get("workforce_date").toString();
            String workforce_employee_id = UUID.randomUUID().toString();
            String createtime = new Date().toString();
            String confirm_boolean = "1";
            map.put("starttime", starttime);
            map.put("sametable", sametable);
            map.put("endtime", endtime);
            map.put("workforce_employee_id", workforce_employee_id);
            map.put("userid", user_id);
            map.put("createtime", createtime);
            map.put("confirm_boolean", confirm_boolean);
            map.put("workforce_date", workforce_date);
            //将员工选班存到表sd_workforce_employee
            workemployeeServer.inserttwo(map);
            //将员工选班存到表sd_workforce_employee_system
            workemployeeServer.save_sql("WorkemployeeMapper.saveSystem", map);
            String num = newtable.get("num").toString();
            Map param = new HashMap<>();
            param.put("workforce_company_id", workforce_company_id);
            param.put("comuserid", comuserid);
            //查出当前排班员工 按分数排序后修改表sd_workforce_employee_system中confirm_boolean
            List all1 = workemployeeServer.listAll_sql("WorkemployeeMapper.findAvgScore", param);
            while ((all1.size() > Integer.parseInt(num))) {
                //比较评分 分低者退出
                //有问题
                Map map1 = (Map) all1.get(all1.size() - 1);
                map1.put("confirm_boolean", "2");
                map1.put("workforce_company_id", map1.get(workforce_company_id));
                workemployeeServer.edit_sql("WorkemployeeMapper.editSystem", map1);

            }
        }
        return new Result();
    }



    /**
     * 实际排班
     *
     * @return
     */
    @ApiOperation(
            value = "企业端  实际排班查询",
            notes = "企业端  实际排班查询"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "startdate", dataType = "startdate", required = true, value
                    = "开始日期"),
            @ApiImplicitParam(paramType = "query", name = "enddate", dataType = "enddate", required = true, value = "结束日期")
    })
    @RequestMapping(
            value = {"findWorkforceByTime"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result findWorkforceByTime(String skey, String startdate, String enddate) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id", user_id);
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        //查表sd_workforce_employee
        List<Map> list = userServer.listAll_sql("ScheduMapper.findWorkforceByTime", map);
        return new Result().setData(list);
    }

    /**
     * 系统排班
     *
     * @return
     */
    @ApiOperation(
            value = "企业端  系统排班查询",
            notes = "企业端  系统排班查询"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "startdate", dataType = "startdate", required = true, value
                    = "开始日期"),
            @ApiImplicitParam(paramType = "query", name = "enddate", dataType = "enddate", required = true, value = "结束日期")
    })
    @RequestMapping(
            value = {"findWorkforceByTimeSystem"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result findWorkforceByTimeSystem(String skey, String startdate, String enddate) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map3 = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id", user_id);
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        //查表sd_workforce_employee_system
        List<Map> list = userServer.listAll_sql("ScheduMapper.findWorkforceByTimeSystem", map);
        map.put("userid", user_id);
        map.put("kstime", startdate);
        map.put("jstime", enddate);
        List<Map> list2 =userServer.listAll_sql("ScheduMapper.listAllZhou", map);
        //查表sd_workforce_employee_system_zhou
        if (list2 == null) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            for (int i = 0; i <list.size() ; i++) {
                map = list.get(i);
                map1.put("companyuserid", user_id);
                map1.put("workforce_date", map.get("workforce_date").toString());
                //将系统排班存至周排班中 将员工状态设置为未发布
                map1.put("confirm_boolean", "0");
                map1.put("createtime",new Date().toString());
                map1.put("endtime",map.get("endtime").toString());
                map1.put("sametable", map.get("sametable").toString());
                map1.put("starttime", map.get("starttime").toString());
                map1.put("userid", map.get("userid").toString());
                map1.put("workforce_company_id", map.get("workforce_company_id").toString());
                map1.put("workforce_employee_id", map.get("workforce_employee_id").toString());
                //将系统排班表添加至周排班表
                workemployeeServer.save_sql("WorkemployeeMapper.saveZhou", map1);
        }

        }

        return new Result().setData(list);
    }


    /**
     * 查询企业员工当天所有排班信息
     *
     * @return
     */
    @ApiOperation(
            value = "查询企业员工当天所有排班信息",
            notes = "查询企业员工当天所有排班信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "workforce_date", dataType = "String", required = true, value = "workforce_date（日期）")
            ,
            @ApiImplicitParam(paramType = "query", name = "confirm_boolean", dataType = "String", required = true,
                    value = "confirm_boolean（是否发布）")
    })
    @RequestMapping(
            value = {"findWorkforceByDate"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result findWorkforceByDate(String skey, String workforce_date,String confirm_boolean) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id", user_id);
        map.put("workforce_date", workforce_date);
        map.put("confirm_boolean", confirm_boolean);
        List<Map> dataList = userServer.listAll_sql("ScheduMapper.findWorkforceByDate", map);
        //huyang mark

        List<List<Map>> resultArray = new ArrayList<>();
        List<Map> firstArray = new ArrayList<>();
        resultArray.add(firstArray);

        for (Map dic : dataList
                ) {
            boolean isThisDicAdd = false;
            List<Map> newNowArray = new ArrayList<>();
            ;
            int i = 0;
            for (List nowArray : resultArray) {
                int nowDicRights = 0;
                newNowArray.clear();
                newNowArray.addAll(nowArray);
                for (int j = 0; j < nowArray.size(); j++) {
                    Object star1 = dic.get("starttime");
                    Object endt1 = dic.get("endtime");
                    String starttime = ((Map) nowArray.get(j)).get("starttime").toString();
                    String endtime = ((Map) nowArray.get(j)).get("endtime").toString();
                    Date date1 = simpleDateFormat.parse(star1.toString());
                    Date date2 = simpleDateFormat.parse(endt1.toString());
                    Date date3 = simpleDateFormat.parse(starttime);
                    Date date4 = simpleDateFormat.parse(endtime);
                    if (date1.getTime() <= date3.getTime() && date2.getTime() <= date3.getTime()
                            || date1.getTime() >= date4.getTime() && date2.getTime() >= date4.getTime()) {
                        nowDicRights++;
                    }
                }
                if (nowDicRights == nowArray.size()) {//没有交集的情况和现有数组内元素数相同，表明此元素可以直接加入到该数组中
                    newNowArray.add(dic);
                    isThisDicAdd = true;
                    i++;
                    break;
                }
                i++;
            }
            resultArray.set(i - 1, newNowArray);
            if (isThisDicAdd == false) {
                List newArray = new ArrayList();
                newArray.add(dic);
                resultArray.add(newArray);
            }

        }


        return new Result().setData(resultArray);

    }

    /**
     * 企业编辑排班查询功能
     *
     * @return
     */
    @ApiOperation(
            value = "企业端 企业编辑排班查询功能(编辑弹窗的内容)",
            notes = "企业端 企业编辑排班查询功能(编辑弹窗的内容)"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "workforce_date", dataType = "String", required = true, value = "workforce_date")
    })
    @RequestMapping(
            value = {"findUserByComuserid"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result findUserByComuserid(String skey, String workforce_date) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id", user_id);
        map.put("workforce_date", workforce_date);
        Map map2 = new HashMap();
        List<Map> dataList = userServer.listAll_sql("ScheduMapper.findUserByComuserid", map);
        for (int i = 0; i < dataList.size(); i++) {
            Map maps = dataList.get(i);
            List<String> list1 = new ArrayList<String>();
            List<String> list2 = new ArrayList<String>();
            List<String> list3 = new ArrayList<String>();
            List<String> list4 = new ArrayList<String>();
            List<String> list5 = new ArrayList<String>();
            if (null != maps && null != maps.get("allusers") && !"".equals(maps.get("allusers").toString().replaceAll(" ", ""))) {
                String[] alls = (maps.get("allusers") + "").split(",");
                List<String> lists = Arrays.asList(alls);
                for (int j = 0; j < lists.size(); j++) {
                    String[] allchs = alls[j].split("#");
                    list1.add(allchs[0]);
                    list3.add(allchs[1]);
                }
            }
            if (null != maps && null != maps.get("yusers") && !"".equals(maps.get("yusers").toString().replaceAll(" ", ""))) {
                String[] alls = maps.get("yusers").toString().split(",");
                List<String> lists = Arrays.asList(alls);
                for (int j = 0; j < lists.size(); j++) {
                    String[] allchs = lists.get(j).split("#");
                    list2.add(allchs[0]);
                    list4.add(allchs[1]);
                    list5.add(allchs[2]);
                }
            }
            // 差集
            list1.removeAll(list2);
            list3.removeAll(list4);
            System.out.println("无排班员工id-->" + JSON.toJSON(list1));
            System.out.println("无排班员工name-->" + JSON.toJSON(list3));
            System.out.println("有排班员工id-->" + JSON.toJSON(list2));
            System.out.println("有排班员工name-->" + JSON.toJSON(list4));
            System.out.println("有排班员工排班id-->" + JSON.toJSON(list5));
            map2.put("Nuser_id", list1);
            map2.put("Yuser_id", list2);
            map2.put("Nnikename", list3);
            map2.put("Ynikename", list4);
            map2.put("Yworkforce_employee_id", list5);
        }
        List<Map> dataList1 = userServer.listAll_sql("ScheduMapper.findUserByAvgCount", map);
        map2.put("Avgscore", dataList1);
        return new Result().setData(map2);
    }

    /**
     * 编辑排班信息
     *
     * @return
     */
    @ApiOperation(
            value = "编辑排班信息 任意编辑",
            notes = "编辑排班信息 任意编辑"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),

            @ApiImplicitParam(paramType = "query", name = "workList", dataType = "List", required = true, value = "workList")
            //编辑后的排班信息
    })
    @RequestMapping(
            value = {"updateWorkforce"},
            method = {RequestMethod.POST}
    )
    @Transactional
    public Result updateWorkforce(String skey, @RequestBody List<Map> workList) throws MyException {


        Map<String, Object> map1 = new HashMap<String, Object>();

        Map userInfo = userServer.loginType(skey);
        String comid = userInfo.get("user_id").toString();
        String workforce_company_id = UUID.randomUUID().toString();
        String sametable = UUID.randomUUID().toString();
        String createtime = new Date().toString();
        String workforce_date = workList.get(0).get("workforce_date").toString();
        map1.put("workforce_date",workforce_date);
        map1.put("companyuserid",comid);
        //多个员工(可能含有多个同一个员工 不同时间段)
        List<Map> zhouList = workemployeeServer.listAll_sql("ScheduMapper.listAllZhou",map1);
        //企业修改后的员工排班
        for (int i = 0; i <workList.size() ; i++) {
            Map<String, Object> companyMap = new HashMap<String, Object>();
            companyMap =workList.get(i);

            //周排班表中已存在员工排班
            for (int j = 0; j < zhouList.size(); j++){
                Map zhouMap = zhouList.get(j);

                //判断员工的上班时间是否被修改
                if (companyMap.get("user_id").toString().equals(zhouMap.get("userid").toString())){
                    //修改员工上班时间 需判断是否重复
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("workforce_date",workforce_date);
                    map2.put("companyuserid",comid);
                    map2.put("userid",companyMap.get("user_id").toString());
                    map2.put("starttime",companyMap.get("starttime").toString());
                    map2.put("endtime",companyMap.get("endtime").toString());
                    //通过传入的开始时间 与结束时间  与数据库中员工开始 结束时间做交集 查出不同状态下的员工
                    List<Map> maybesameListStart = workemployeeServer.listAll_sql("ScheduMapper.findSameFormZhouKS",
                            map2);
                    List<Map> maybesameListEnd = workemployeeServer.listAll_sql("ScheduMapper.findSameFormZhouJS",
                            map2);
                    //判断userid是否相等  两种情况  被包含(修改)  或  开始结束时间分别在两个时间段(中间可能还有小的时间段) (删除后添加)
                    if (maybesameListStart.size() == 1 && maybesameListEnd.size() == 1) {
                        if (maybesameListStart.get(0).get("userid").toString().equals(maybesameListEnd.get(0).get("userid").toString())) {
                            Map<String, Object> map3 = new HashMap<String, Object>();
                            map3.put("workforce_date",workforce_date);
                            map3.put("companyuserid",comid);
                            map3.put("userid",companyMap.get("user_id").toString());
                            map3.put("starttime",companyMap.get("starttime").toString());
                            map3.put("endtime",companyMap.get("endtime").toString());
                            map3.put("createtime",createtime);
                            workemployeeServer.edit_sql("ScheduMapper.editEmployeeZhouSame",map3);
                        }else {
                            String time1 = maybesameListStart.get(0).get("starttime").toString();
                            String time2 = maybesameListEnd.get(0).get("endtime").toString();
                            Map<String, Object> map4 = new HashMap<String, Object>();
                            map4.put("workforce_date",workforce_date);
                            map4.put("companyuserid",comid);
                                //以数据库时间段删除
                            map4.put("starttime",time1);
                            map4.put("endtime",time2);

                            workemployeeServer.edit_sql("ScheduMapper.deleteEmployeeZhouSame",map4);
                            //添加
                            Map<String, Object> map5 = new HashMap<String, Object>();
                            map5.put("workforce_date",workforce_date);
                            map5.put("companyuserid",comid);
                            map5.put("userid",companyMap.get("user_id").toString());
                            map5.put("starttime",companyMap.get("starttime").toString());
                            map5.put("endtime",companyMap.get("endtime").toString());
                            map5.put("createtime",createtime);
                            map5.put("workforce_company_id", workforce_company_id);
                            map5.put("confirm_boolean", "0");
                            map5.put("workforce_employee_id", UUID.randomUUID().toString());
                            map5.put("sametable", sametable);
                            workemployeeServer.save_sql("WorkemployeeMapper.saveZhou", map5);
                        }
                    }
                    //判断在时间段内的是开始还是结束   开始(取时间段开始 修改后结束)  结束(取修改后的开始 时间段的结束)  进行修改
                    else if (maybesameListStart.size() == 1 || maybesameListEnd.size() == 1) {
                        if (maybesameListStart.size() == 1){
                            Map<String, Object> map6 = new HashMap<String, Object>();
                            map6.put("workforce_date",workforce_date);
                            map6.put("companyuserid",comid);

                            //以数据库时间段的开始 传入的结束时间
                            map6.put("starttime",maybesameListStart.get(0).get("starttime").toString());
                            map6.put("endtime",companyMap.get("endtime").toString());
                            workemployeeServer.delete_sql("ScheduMapper.deleteEmployeeZhouSame",map6);
                            Map<String, Object> map7 = new HashMap<String, Object>();
                            map7.put("workforce_date",workforce_date);
                            map7.put("companyuserid",comid);
                            map7.put("userid",companyMap.get("user_id").toString());
                            map7.put("starttime",companyMap.get("starttime").toString());
                            map7.put("endtime",companyMap.get("endtime").toString());
                            map7.put("createtime",createtime);
                            map7.put("workforce_company_id", workforce_company_id);
                            map7.put("confirm_boolean", "0");
                            map7.put("workforce_employee_id", UUID.randomUUID().toString());
                            map7.put("sametable", sametable);
                            workemployeeServer.save_sql("WorkemployeeMapper.saveZhou", map7);
                        }else if (maybesameListEnd.size() == 1){
                            Map<String, Object> map6 = new HashMap<String, Object>();
                            map6.put("workforce_date",workforce_date);
                            map6.put("companyuserid",comid);

                            //以传入的开始时间 数据库时间段的结束
                            map6.put("starttime",companyMap.get("starttime").toString());
                            map6.put("endtime",maybesameListEnd.get(0).get("endtime").toString());
                            workemployeeServer.delete_sql("ScheduMapper.deleteEmployeeZhouSame",map6);
                            Map<String, Object> map7 = new HashMap<String, Object>();
                            map7.put("workforce_date",workforce_date);
                            map7.put("companyuserid",comid);
                            map7.put("userid",companyMap.get("user_id").toString());
                            map7.put("starttime",companyMap.get("starttime").toString());
                            map7.put("endtime",companyMap.get("endtime").toString());
                            map7.put("createtime",createtime);
                            map7.put("workforce_company_id", workforce_company_id);
                            map7.put("confirm_boolean", "0");
                            map7.put("workforce_employee_id", UUID.randomUUID().toString());
                            map7.put("sametable", sametable);
                            workemployeeServer.save_sql("WorkemployeeMapper.saveZhou", map7);
                        }

                    }
                    //判断是覆盖(删除后添加) 还是插入(插入)  判断方法:即该时间段是否含有该员工的工作时间端
                    else if (maybesameListStart.size() == 0 && maybesameListEnd.size() == 0){
                        //查找该时间段是否有员工上班
                        Map ismap =workemployeeServer.findById_sql("ScheduMapper.findSameFormZhouTwo",map2);
                        if (ismap.size()!=0) {
                            workemployeeServer.edit_sql("ScheduMapper.deleteEmployeeZhouSame",map2);
                            Map<String, Object> map5 = new HashMap<String, Object>();
                            map5.put("workforce_date",workforce_date);
                            map5.put("companyuserid",comid);
                            map5.put("userid",companyMap.get("user_id").toString());
                            map5.put("starttime",companyMap.get("starttime").toString());
                            map5.put("endtime",companyMap.get("endtime").toString());
                            map5.put("createtime",createtime);
                            map5.put("workforce_company_id", workforce_company_id);
                            map5.put("confirm_boolean", "0");
                            map5.put("workforce_employee_id", UUID.randomUUID().toString());
                            map5.put("sametable", sametable);
                            workemployeeServer.save_sql("WorkemployeeMapper.saveZhou", map5);


                        }else {
                            Map<String, Object> map5 = new HashMap<String, Object>();
                            map5.put("workforce_date",workforce_date);
                            map5.put("companyuserid",comid);
                            map5.put("userid",companyMap.get("user_id").toString());
                            map5.put("starttime",companyMap.get("starttime").toString());
                            map5.put("endtime",companyMap.get("endtime").toString());
                            map5.put("createtime",createtime);
                            map5.put("workforce_company_id", workforce_company_id);
                            map5.put("confirm_boolean", "0");
                            map5.put("workforce_employee_id", UUID.randomUUID().toString());
                            map5.put("sametable", sametable);
                            workemployeeServer.save_sql("WorkemployeeMapper.saveZhou", map5);

                        }

                    }
                }else if(!companyMap.get("user_id").toString().equals(zhouMap.get("userid").toString())) {
                    //添加新增员工
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("workforce_company_id", workforce_company_id);
                    map.put("sametable", sametable);
                    map.put("companguserid",comid );
                    map.put("confirm_boolean", "0");
                    map.put("workforce_employee_id", UUID.randomUUID().toString());
                    map.put("createtime", createtime);
                    map.put("workforce_date", workforce_date);
                    map.put("userid", workList.get(i).get("user_id").toString());
                    map.put("starttime", workList.get(i).get("starttime").toString());
                    map.put("endtime", workList.get(i).get("endtime").toString());
                    //将新增员工添加至  周排班表
                    workemployeeServer.save_sql("WorkemployeeMapper.saveZhou", map);
                }

            }

        }
        return new Result().setData("修改成功");
    }


    /**
     * 发布排班
     *
     * @return
     */
    @ApiOperation(
            value = "企业端 确认发布员工排班 ",
            notes = "企业端 确认发布员工排班 "
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "startdate", dataType = "String", required = true, value = "开始日期"),
            @ApiImplicitParam(paramType = "query", name = "enddate", dataType = "String", required = true, value = "结束日期")
    })
    @RequestMapping(
            value = {"pushWorkemploy"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    @Transactional
    public Result pushWorkemploy(
            String skey,
            String startdate,String enddate) throws MyException {

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map3 = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        String comid = userInfo.get("user_id").toString();
        map3.put("companyuserid",comid);
        map3.put("kstime",startdate);
        map3.put("jstime",enddate);
        List<Map> all = workemployeeServer.listAll_sql("ScheduMapper.listAllZhou",map3);

        if (all != null) {
            for (int i = 0; i < all.size(); i++) {
                Map map1 = all.get(i);
                map.put("userid",map1.get("userid").toString());
                map.put("companyuserid",map1.get("companyuserid").toString());
                map.put("workforce_date",map1.get("workforce_date").toString());
                map.put("createtime",new Date().toString());
                map.put("confirm_boolean","1");
                workemployeeServer.edit_sql("WorkemployeeMapper.editZhouZT",map);

            }
        }
        List<Map> allQc = workemployeeServer.listAll_sql("ScheduMapper.listAllZhou",map3);
        Map map2 = new HashMap();
        map2.put("title", "系统通知");

        map2.put("content", "您有一个新排班已发布！");
        map2.put("status", "0");
        for (int i = 0; i < allQc.size(); i++) {
                Map map1 =  allQc.get(i);
                map2.put("noticeid", UUID.randomUUID().toString());
                map2.put("userid", map1.get("userid").toString());
                map2.put("createtime", new Date().toString());
                noticeServer.insert(map2);

            }


        return new Result();
    }






    /**
     * 员工选排版区域以选人数量
     *
     * @return
     */
    @ApiOperation(
            value = "getWorkemploySum",
            notes = "getWorkemploySum"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "workforce_company_id", dataType = "String", required = true, value = "workforce_company_id")
    })
    @RequestMapping(
            value = {"getWorkemploySum"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getWorkemploySum(
            String skey,
            String workforce_company_id) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();

        Map userInfo = userServer.loginType(skey);

        map.put("workforce_company_id", workforce_company_id);
        List all = workemployeeServer.all(map);
        map.put("sum", all.size());
        return new Result().setData(map);
    }




   /* *
     * 当员工不存在时找寻一个
     * @return*/

    @ApiOperation(
            value = "查找公司Sametable",
            notes = "查找公司Sametable"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "startdate", dataType = "String", required = true, value = "开始日期"),
            @ApiImplicitParam(paramType = "query", name = "enddate", dataType = "String", required = true, value = "结束日期"),
            @ApiImplicitParam(paramType = "query", name = "workforce_company_id", dataType = "String", required = true, value = "企业排班id")
    })
    @RequestMapping(
            value = {"findSametable"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result findSametable(String skey, String startdate, String enddate, String workforce_company_id) throws MyException {
        Map<String, Object> map = new HashMap<String, Object>();
        Map userInfo = userServer.loginType(skey);
        map.put("workforce_company_id", workforce_company_id);
        userServer.listAll_sql("ScheduMapper.findSametableById", map);
        String user_id = userInfo.get("user_id").toString();
        map.put("user_id", user_id);
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        List<Map> list = userServer.listAll_sql("ScheduMapper.findWorkforceByTime", map);
        return new Result().setData(list);
    }

    /**
<<<<<<< Updated upstream
     * 企业获取保存 或者发布的排班表
     *
     * @return
     */
    @ApiOperation(
            value = "getCompanySchedu",
            notes = "getCompanySchedu"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey"),
            @ApiImplicitParam(paramType = "query", name = "sametable", dataType = "String", required = true, value = "sametable"),
            @ApiImplicitParam(paramType = "query", name = "comid", dataType = "String", required = true, value = "comid")
    })
    @RequestMapping(
            value = {"getCompanySchedu"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result getCompanySchedu(
            String skey,
            String comid, String sametable) throws MyException {
        Map<String, Object> workemployeeConditionMap = new HashMap<String, Object>();
        workemployeeConditionMap.put("userid", comid);
        workemployeeConditionMap.put("jstime", TimeUtil.getSundayOfThisWeek());
        workemployeeConditionMap.put("kstime", TimeUtil.getMondayOfThisWeek());

        List<Map> schedfactlist = workemployeeServer.all(workemployeeConditionMap);
        return new Result().setData(schedfactlist);
    }


    @ApiOperation(
            value = "查找公司员工数量",
            notes = "查找公司员工数量"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "skey", dataType = "String", required = true, value = "skey")
    })
    @RequestMapping(
            value = {"findCount"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result findCount(String skey) throws MyException {
        Map<String,Object> map = new HashMap<String, Object>(  );
        Map userInfo = userServer.loginType(skey);

        String userid = userInfo.get("user_id").toString();
        map.put("userid",userid);
        List<Map>list=userServer.listAll_sql("UserMapper.findCount",map);
        return new Result().setData(list);
    }

}
