package com.scheduling.api;

import com.scheduling.common.RetCode;
import com.scheduling.common.util.Result;
import com.scheduling.common.util.StringUtil;
import com.scheduling.service.ifac.ExtendsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
@EnableAutoConfiguration
@RequestMapping({"/hyhelp"})
@Api(
        tags = {"hyhelp_service"}
)
public class HYHelpApi {

    public static final Logger logger = Logger.getLogger(HYHelpApi.class);
    @Resource
    private ExtendsService extendsService;
    @ApiOperation(
            value = "帮助中心",
            notes = "帮助中心(dddd)"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "helplableid", dataType = "String", value = "分类id(默认传空字符串,点击查看所有文章传id)"),
    })
    @RequestMapping(
            value = {"/v1/commenques"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result commenques(String helplableid) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<Map> listAllhelplable = extendsService.listAll_sql("HyuserMapper.listAll",map);
        map.put("listAllhelplable", listAllhelplable);
        return new Result().setCode(RetCode.OK.code()).setMsg_cn(RetCode.OK.msg()).setData(map);
    }
    @ApiOperation(
            value = "帮助中心查询答案",
            notes = "帮助中心查询答案(dddd)"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "helplablecontextid", dataType = "String", required = true, value = "问题id"),
    })
    @RequestMapping(
            value = {"/v1/commenan"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Result commenan(String helplablecontextid) {
        //需要检查是否为空的参数
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("helplablecontextid", helplablecontextid);
        String[] str = {"helplablecontextid"};
        Map<String, Object> _result = StringUtil.checkParamIsBank(map, str);
        if (_result != null) {
            return new Result().setCode(400).setData(_result).setMsg_cn("帮助中心查询答案失败");
        } else {
            List<Map> listAllhelplablecontext = extendsService.listAll_sql("TestMapper.listAllhelplablecontext",map);//列表
            if (null!=listAllhelplablecontext&&listAllhelplablecontext.size()>0){
                map.put("helplablecontexttext", listAllhelplablecontext.get(0));
            }
            return new Result().setCode(200).setData(map);
        }
    }
}
