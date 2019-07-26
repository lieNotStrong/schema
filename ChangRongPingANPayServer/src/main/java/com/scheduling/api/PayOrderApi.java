package com.scheduling.api;

import com.scheduling.common.util.Result;
import com.scheduling.utils.PayOrderDemo;
import com.scheduling.utils.QueryOrderViewDemo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping({"/pay"})
@Api(
        tags = {"支付接口"}
)
@Slf4j
/**
 * 支付接口
 * wly
 */
public class PayOrderApi {


    @ApiOperation(
            value = "payOrder",
            notes = "支付"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outNo", dataType = "String", required = true, value = "订单号"),
            @ApiImplicitParam(paramType = "query", name = "tradeAmount", dataType = "Int", required = true, value = "实际交易金额（以分为单位，没有小数点，一分为1）"),
            @ApiImplicitParam(paramType = "query", name = "originalAmount", dataType = "Int", required = true, value = "原始交易金额（以分为单位，没有小数点，一分为1）"),
            @ApiImplicitParam(paramType = "query", name = "suborderId", dataType = "String", required = true, value = "子订单号多个用，拼接"),
            @ApiImplicitParam(paramType = "query", name = "subamount", dataType = "String", required = true, value = "子订单号对应的价格多个用，拼接(以元为单位1分为0.01)")


    })
    @RequestMapping(
            value = {"payOrder"},
            method = {RequestMethod.POST}
    )
    /**
     * 下订单
     */
    @ResponseBody
    public Result payOrder(String outNo,Integer tradeAmount,Integer originalAmount,String suborderId,String subamount){
        Object object=new PayOrderDemo().order(outNo, "WeixinCS", null, "微信支付测试(服务器测试)",originalAmount,
                0, 0, tradeAmount,"201712250001", "4200000032201711144648000277", null, "下单接口",
                null, null, null, "http://localhost:8085/demo111/callback.do",suborderId,subamount);
        return new Result().setData(object);
    }

    @ApiOperation(
            value = "queryOrder",
            notes = "查询订单"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outNo", dataType = "String", required = true, value = "订单号")
    })
    @RequestMapping(
            value = {"queryOrder"},
            method = {RequestMethod.POST}
    )
    /**
     * 查询订单状态
     */
    @ResponseBody
    public Result queryOrder(String outNo){
        Object object=new QueryOrderViewDemo().queryOrderView(outNo);
        return new Result().setData(object);
    }
}
