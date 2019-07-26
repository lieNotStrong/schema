package com.scheduling.service.impl;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.scheduling.service.ifac.ExtendsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 胡国旺
 */
@Transactional
@Service("extendsService")
@SuppressWarnings("rawtypes")
public class ExtendsServiceImpl extends BaseServiceImpl implements ExtendsService {
	static SimpleDateFormat longzong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 点击平仓
	 * @param order
	 * @param express
	 * @return
	 */
	@Override
	public int updateOrder(Map order, Map express) {

		order.put("pctime",longzong.format(new Date()));
		if (null!=order.get("createtime")&&!"".equals(order.get("createtime"))){
			order.put("createtime",longzong.format(order.get("createtime")));
		}
		if (null!=order.get("jgtime")&&!"".equals(order.get("jgtime"))){
			order.put("jgtime",longzong.format(order.get("jgtime")));
		}
		if (null!=order.get("pctime")&&!"".equals(order.get("pctime"))){
			order.put("pctime",longzong.format(new Date()));
		}
		order.put("hangtype","3");
		order.put("sussprice",order.get("pricetb")+"");
		int q=edit_sql("HyorderMapper.edit", order);

		if ("1".equals(order.get("type")+"")){
			q=edit_sql("HybtcuserMapper.edit", express);
		}
		if ("2".equals(order.get("type")+"")){
			q=edit_sql("HyethuserMapper.edit", express);
		}
		if ("3".equals(order.get("type")+"")){
			q=edit_sql("HyeosuserMapper.edit", express);
		}
		Map moneylog=new HashMap();
		moneylog.put("money",order.get("bondprice"));
		moneylog.put("balance",order.get("dsbzje"));
		moneylog.put("content","平仓");
		moneylog.put("type","2");//1购买 2平仓3交割
		moneylog.put("paytype",order.get("type"));//1btc 2eth 3eos
		moneylog.put("createtime",longzong.format(new Date()));
		moneylog.put("flag",null);
		moneylog.put("hy_order_id",order.get("hy_order_id"));
		moneylog.put("userid",order.get("userid"));
		q=save_sql("HymoneylogMapper.save", moneylog);
		return q;
	}
	/**
	 * 点击撤单
	 */
	@Override
	public int delorder(Map zzhye, String userid) {
		int q=0;
		BigDecimal bondprice=(BigDecimal)zzhye.get("bondprice");//保证金
		zzhye.put("hangtype","6");
		zzhye.put("pricetb","0.00");
		zzhye.put("profitprice","0.00");
		Map maprek=new HashMap();
		if ("1".equals(zzhye.get("type")+"")){
			maprek=new HashMap();
			maprek.put("userid",Integer.valueOf(userid));
			maprek =findById_sql("HybtcuserMapper.findById",maprek);
			BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
			if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
				hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
			}
			BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
			if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
				hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
			}
			hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
			maprek.put("hy_yybzj_count",hy_yybzj_count.doubleValue()<=0?"0.00":hy_yybzj_count.setScale(10, BigDecimal.ROUND_HALF_UP));
			hy_kybzj_count=hy_kybzj_count.add(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
			maprek.put("hy_kybzj_count",hy_kybzj_count.doubleValue()<=0?"0.00":hy_kybzj_count.setScale(10, BigDecimal.ROUND_HALF_UP));
		}
		if ("2".equals(zzhye.get("type")+"")){
			maprek=new HashMap();
			maprek.put("userid",Integer.valueOf(userid));
			maprek =findById_sql("HyethuserMapper.findById",maprek);
			BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
			if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
				hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
			}
			BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
			if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
				hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
			}
			hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
			maprek.put("hy_yybzj_count",hy_yybzj_count.doubleValue()<=0?"0.00":hy_yybzj_count.setScale(10, BigDecimal.ROUND_HALF_UP));
			hy_kybzj_count=hy_kybzj_count.add(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
			maprek.put("hy_kybzj_count",hy_kybzj_count.doubleValue()<=0?"0.00":hy_kybzj_count.setScale(10, BigDecimal.ROUND_HALF_UP));
		}
		if ("3".equals(zzhye.get("type")+"")){
			//eos开空季度
			maprek=new HashMap();
			maprek.put("userid",Integer.valueOf(userid));
			maprek =findById_sql("HyeosuserMapper.findById",maprek);
			BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
			if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
				hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
			}
			BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
			if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
				hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
			}
			hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
			maprek.put("hy_yybzj_count",hy_yybzj_count.doubleValue()<=0?"0.00":hy_yybzj_count.setScale(10, BigDecimal.ROUND_HALF_UP));
			hy_kybzj_count=hy_kybzj_count.add(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
			maprek.put("hy_kybzj_count",hy_kybzj_count.doubleValue()<=0?"0.00":hy_kybzj_count.setScale(10, BigDecimal.ROUND_HALF_UP));
		}
		zzhye.put("pctime",longzong.format(new Date()));
		if (null!=zzhye.get("createtime")&&!"".equals(zzhye.get("createtime"))){
			zzhye.put("createtime",longzong.format(zzhye.get("createtime")));
		}
		if (null!=zzhye.get("jgtime")&&!"".equals(zzhye.get("jgtime"))){
			zzhye.put("jgtime",longzong.format(zzhye.get("jgtime")));
		}
		if (null!=zzhye.get("pctime")&&!"".equals(zzhye.get("pctime"))){
			zzhye.put("pctime",longzong.format(new Date()));
		}
		zzhye.put("hangtype","6");
		q=edit_sql("HyorderMapper.edit", zzhye);

		if ("1".equals(zzhye.get("type")+"")){
			q=edit_sql("HybtcuserMapper.edit", maprek);
		}
		if ("2".equals(zzhye.get("type")+"")){
			q=edit_sql("HyethuserMapper.edit", maprek);
		}
		if ("3".equals(zzhye.get("type")+"")){
			q=edit_sql("HyeosuserMapper.edit", maprek);
		}
		Map moneylog=new HashMap();
		moneylog.put("money",zzhye.get("bondprice"));
		moneylog.put("balance",zzhye.get("dsbzje"));
		moneylog.put("content","撤单");
		moneylog.put("type","13");//1购买 2平仓3交割
		moneylog.put("paytype",zzhye.get("type"));//1btc 2eth 3eos
		moneylog.put("createtime",longzong.format(new Date()));
		moneylog.put("flag",null);
		moneylog.put("hy_order_id",zzhye.get("hy_order_id"));
		moneylog.put("userid",zzhye.get("userid"));
		q=save_sql("HymoneylogMapper.save", moneylog);
		return q;
	}
	/**
	 * 钱包和法币转合约资金划转资金划转
	 * @param hyuser
	 * @param btcuser
	 * @param ethuser
	 * @param eosuser
	 * @return
	 */
	@Override
	public int adduhyusee(Map hyuser,Map btcuser,Map ethuser,Map eosuser) {
		int q=0;
		String hysucc_count =hyuser.get("hyb_succ_count")+"";
		String ztype=hyuser.get("ztype")+"";
		Map jiuuser= new HashMap();
		jiuuser.put("userid",hyuser.get("userid"));
		jiuuser=findById_sql("HyuserMapper.findById",jiuuser);
		int hyb_count=0;
		BigDecimal hyb_succ_count= new BigDecimal(0.00);
		if (null!=jiuuser&&null!=jiuuser.get("hyb_count")&&!"".equals(jiuuser.get("hyb_count")+"")){
			hyb_count=Integer.valueOf(jiuuser.get("hyb_count")+"")+1;
		}
		if (null!=jiuuser&&null!=jiuuser.get("hyb_succ_count")&&!"".equals(jiuuser.get("hyb_succ_count")+"")){
			hyb_succ_count=new BigDecimal(jiuuser.get("hyb_succ_count")+"").add(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
		}
		if (null!=jiuuser){
			jiuuser.put("hyb_count",hyb_count);
			jiuuser.put("hyb_succ_count",hyb_succ_count);
			q=edit_sql("HyuserMapper.edit", hyuser);
		}else{
			hyuser.put("hyb_count",hyb_count);
			hyuser.put("hyb_succ_count",hyuser.get("hyb_succ_count"));
			hyuser.put("bzj_frozen",new BigDecimal(0.00));
			hyuser.put("hyb_frozen",new BigDecimal(0.00));
			hyuser.put("typezh","1");
			hyuser.put("createtime",longzong.format(new Date()));
			q=save_sql("HyuserMapper.save", hyuser);
		}
		BigDecimal zq= new BigDecimal(0.00);
		if ("1".equals(hyuser.get("type")+"")){
			BigDecimal hy_btc_count= new BigDecimal(0.00);
			BigDecimal hy_kybzj_count= new BigDecimal(0.00);
			Map btcmap= new HashMap();
			btcmap.put("userid",hyuser.get("userid"));
			btcmap=findById_sql("HybtcuserMapper.findById",btcmap);
			if (null!=btcmap&&null!=btcmap.get("hy_btc_count")&&!"".equals(btcmap.get("hy_btc_count")+"")){
				hy_btc_count=new BigDecimal(btcmap.get("hy_btc_count")+"").add(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
			}
			if (null!=btcmap&&null!=btcmap.get("hy_kybzj_count")&&!"".equals(btcmap.get("hy_kybzj_count")+"")){
				zq=new BigDecimal(btcmap.get("hy_kybzj_count")+"");
				hy_kybzj_count=new BigDecimal(btcmap.get("hy_kybzj_count")+"").add(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
			}
			if (null!=btcmap){
				btcmap.put("hy_btc_count",hy_btc_count);
				btcmap.put("hy_kybzj_count",hy_kybzj_count);
				q=edit_sql("HybtcuserMapper.edit", btcmap);
			}else{
				btcuser.put("hy_btc_count",hyuser.get("hyb_succ_count"));
				btcuser.put("hy_kybzj_count",hyuser.get("hyb_succ_count"));
				btcuser.put("hy_dzduo_count",new BigDecimal(0.00));
				btcuser.put("hy_dzkong_count",new BigDecimal(0.00));
				btcuser.put("hy_czduo_count",new BigDecimal(0.00));
				btcuser.put("hy_czkong_count",new BigDecimal(0.00));
				btcuser.put("hy_jdduo_count",new BigDecimal(0.00));
				btcuser.put("hy_jdkong_count",new BigDecimal(0.00));
				btcuser.put("hy_ysxyk_count",new BigDecimal(0.00));
				btcuser.put("hy_djbzj_count",new BigDecimal(0.00));
				btcuser.put("hy_yybzj_count",new BigDecimal(0.00));
				btcuser.put("userid",hyuser.get("userid"));
				btcuser.put("createtime",longzong.format(new Date()));
				q=save_sql("HybtcuserMapper.save", btcuser);

			}
			if("1".equals(ztype)){
				//钱包转

				Map fwmap= new HashMap();
				fwmap.put("userid",hyuser.get("userid"));
				fwmap.put("id","1");
				fwmap.put("defaultEnName","BTC");
				fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
					amount=new BigDecimal(fwmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}

				Map fwWmap= new HashMap();
				fwWmap.put("userid",hyuser.get("userid"));
				fwWmap.put("coinId",fwmap.get("coinId"));
				if (amount.doubleValue()<=0){
					fwWmap.put("amount","0.00");
				}else{
					fwWmap.put("amount",amount);
				}

				q=edit_sql("TestMapper.editfinance", fwWmap);
			}
			if("2".equals(ztype)){
				//法币转
				Map c2cmap= new HashMap();
				c2cmap.put("userid",hyuser.get("userid"));
				c2cmap.put("id","1");
				c2cmap.put("defaultEnName","BTC");
				c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
					amount=new BigDecimal(c2cmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}
				Map c2ccmap= new HashMap();
				c2ccmap.put("userid",hyuser.get("userid"));
				c2ccmap.put("coinId",c2cmap.get("coinId"));
				if (amount.doubleValue()<=0){
					c2ccmap.put("amount","0.00");
				}else{
					c2ccmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editc2c", c2ccmap);
			}
			Map moneylog=new HashMap();
			moneylog.put("money",hyuser.get("hyb_succ_count"));
			moneylog.put("balance",zq);
			moneylog.put("content","资金划转btc");
			if("1".equals(ztype)){
				//钱包转
				moneylog.put("type","7");
			}
			if("2".equals(ztype)){
				//法币转
				moneylog.put("type","11");
			}
			moneylog.put("paytype","1");//1btc 2eth 3eos
			moneylog.put("createtime",longzong.format(new Date()));
			moneylog.put("flag",null);
			moneylog.put("userid",hyuser.get("userid"));
			q=save_sql("HymoneylogMapper.save", moneylog);
		}
		if ("2".equals(hyuser.get("type")+"")){
			BigDecimal hy_eth_count= new BigDecimal(0.00);
			BigDecimal hy_kybzj_count= new BigDecimal(0.00);
			Map ethmap= new HashMap();
			ethmap.put("userid",hyuser.get("userid"));
			ethmap=findById_sql("HyethuserMapper.findById",ethmap);
			if (null!=ethmap&&null!=ethmap.get("hy_eth_count")&&!"".equals(ethmap.get("hy_eth_count")+"")){
				hy_eth_count=new BigDecimal(ethmap.get("hy_eth_count")+"").add(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
			}
			if (null!=ethmap&&null!=ethmap.get("hy_kybzj_count")&&!"".equals(ethmap.get("hy_kybzj_count")+"")){
				zq=new BigDecimal(ethmap.get("hy_kybzj_count")+"");
				hy_kybzj_count=new BigDecimal(ethmap.get("hy_kybzj_count")+"").add(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
			}
			if (null!=ethmap){
				ethmap.put("hy_eth_count",hy_eth_count);
				ethmap.put("hy_kybzj_count",hy_kybzj_count);
				q=edit_sql("HyethuserMapper.edit", ethmap);
			}else{
				ethuser.put("hy_eth_count",hyuser.get("hyb_succ_count"));
				ethuser.put("hy_kybzj_count",hyuser.get("hyb_succ_count"));
				ethuser.put("hy_dzduo_count",new BigDecimal(0.00));
				ethuser.put("hy_dzkong_count",new BigDecimal(0.00));
				ethuser.put("hy_czduo_count",new BigDecimal(0.00));
				ethuser.put("hy_czkong_count",new BigDecimal(0.00));
				ethuser.put("hy_jdduo_count",new BigDecimal(0.00));
				ethuser.put("hy_jdkong_count",new BigDecimal(0.00));
				ethuser.put("hy_ysxyk_count",new BigDecimal(0.00));
				ethuser.put("hy_djbzj_count",new BigDecimal(0.00));
				ethuser.put("hy_yybzj_count",new BigDecimal(0.00));
				ethuser.put("userid",hyuser.get("userid"));
				ethuser.put("createtime",longzong.format(new Date()));
				q=save_sql("HyethuserMapper.save", ethuser);
			}
			if("1".equals(ztype)){
				//钱包转
				Map fwmap= new HashMap();
				fwmap.put("userid",hyuser.get("userid"));
				fwmap.put("id","2");
				fwmap.put("defaultEnName","ETH");
				fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
					amount=new BigDecimal(fwmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}

				Map fwWmap= new HashMap();
				fwWmap.put("userid",hyuser.get("userid"));
				fwWmap.put("coinId",fwmap.get("coinId"));
				if (amount.doubleValue()<=0){
					fwWmap.put("amount","0.00");
				}else{
					fwWmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editfinance", fwWmap);
			}
			if("2".equals(ztype)){
				//法币转
				Map c2cmap= new HashMap();
				c2cmap.put("userid",hyuser.get("userid"));
				c2cmap.put("id","2");
				c2cmap.put("defaultEnName","ETH");
				c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
					amount=new BigDecimal(c2cmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}
				Map c2ccmap= new HashMap();
				c2ccmap.put("userid",hyuser.get("userid"));
				c2ccmap.put("coinId",c2cmap.get("coinId"));
				if (amount.doubleValue()<=0){
					c2ccmap.put("amount","0.00");
				}else{
					c2ccmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editc2c", c2ccmap);
			}
			Map moneylog=new HashMap();
			moneylog.put("money",hyuser.get("hyb_succ_count"));
			moneylog.put("balance",zq);
			moneylog.put("content","资金划转eth");
			if("1".equals(ztype)){
				//钱包转
				moneylog.put("type","7");
			}
			if("2".equals(ztype)){
				//法币转
				moneylog.put("type","11");
			}
			moneylog.put("paytype","2");//1btc 2eth 3eos
			moneylog.put("createtime",longzong.format(new Date()));
			moneylog.put("flag",null);
			moneylog.put("userid",hyuser.get("userid"));
			q=save_sql("HymoneylogMapper.save", moneylog);
		}
		if ("3".equals(hyuser.get("type")+"")){
			BigDecimal hy_eos_count= new BigDecimal(0.00);
			BigDecimal hy_kybzj_count= new BigDecimal(0.00);
			Map eosmap= new HashMap();
			eosmap.put("userid",hyuser.get("userid"));
			eosmap=findById_sql("HyeosuserMapper.findById",eosmap);
			if (null!=eosmap&&null!=eosmap.get("hy_eos_count")&&!"".equals(eosmap.get("hy_eos_count")+"")){
				hy_eos_count=new BigDecimal(eosmap.get("hy_eos_count")+"").add(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
			}
			if (null!=eosmap&&null!=eosmap.get("hy_kybzj_count")&&!"".equals(eosmap.get("hy_kybzj_count")+"")){
				zq=new BigDecimal(eosmap.get("hy_kybzj_count")+"");
				hy_kybzj_count=new BigDecimal(eosmap.get("hy_kybzj_count")+"").add(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
			}

			if (null!=eosmap){
				eosmap.put("hy_eos_count",hy_eos_count);
				eosmap.put("hy_kybzj_count",hy_kybzj_count);
				q=edit_sql("HyeosuserMapper.edit", eosmap);
			}else{
				eosuser.put("hy_eos_count",hyuser.get("hyb_succ_count"));
				eosuser.put("hy_kybzj_count",hyuser.get("hyb_succ_count"));
				eosuser.put("hy_dzduo_count",new BigDecimal(0.00));
				eosuser.put("hy_dzkong_count",new BigDecimal(0.00));
				eosuser.put("hy_czduo_count",new BigDecimal(0.00));
				eosuser.put("hy_czkong_count",new BigDecimal(0.00));
				eosuser.put("hy_jdduo_count",new BigDecimal(0.00));
				eosuser.put("hy_jdkong_count",new BigDecimal(0.00));
				eosuser.put("hy_ysxyk_count",new BigDecimal(0.00));
				eosuser.put("hy_djbzj_count",new BigDecimal(0.00));
				eosuser.put("hy_yybzj_count",new BigDecimal(0.00));
				eosuser.put("userid",hyuser.get("userid"));
				eosuser.put("createtime",longzong.format(new Date()));
				q=save_sql("HyeosuserMapper.save", eosuser);

			}
			if("1".equals(ztype)){
				//钱包转
				Map fwmap= new HashMap();
				fwmap.put("userid",hyuser.get("userid"));
				fwmap.put("id","3");
				fwmap.put("defaultEnName","EOS");
				fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
					amount=new BigDecimal(fwmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}

				Map fwWmap= new HashMap();
				fwWmap.put("userid",hyuser.get("userid"));
				fwWmap.put("coinId",fwmap.get("coinId"));
				if (amount.doubleValue()<=0){
					fwWmap.put("amount","0.00");
				}else{
					fwWmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editfinance", fwWmap);
			}
			if("2".equals(ztype)){
				//法币转
				Map c2cmap= new HashMap();
				c2cmap.put("userid",hyuser.get("userid"));
				c2cmap.put("id","3");
				c2cmap.put("defaultEnName","EOS");
				c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
					amount=new BigDecimal(c2cmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}
				Map c2ccmap= new HashMap();
				c2ccmap.put("userid",hyuser.get("userid"));
				c2ccmap.put("coinId",c2cmap.get("coinId"));
				if (amount.doubleValue()<=0){
					c2ccmap.put("amount","0.00");
				}else{
					c2ccmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editc2c", c2ccmap);
			}
			Map moneylog=new HashMap();
			moneylog.put("money",hyuser.get("hyb_succ_count"));
			moneylog.put("balance",zq);
			moneylog.put("content","资金划转eos");
			if("1".equals(ztype)){
				//钱包转
				moneylog.put("type","7");
			}
			if("2".equals(ztype)){
				//法币转
				moneylog.put("type","11");
			}
			moneylog.put("paytype","3");//1btc 2eth 3eos
			moneylog.put("createtime",longzong.format(new Date()));
			moneylog.put("flag",null);
			moneylog.put("userid",hyuser.get("userid"));
			q=save_sql("HymoneylogMapper.save", moneylog);
		}

		return q;
	}
	/**
	 * 合约和法币转钱包资金划转
	 * @param hyuser
	 * @param btcuser
	 * @param ethuser
	 * @param eosuser
	 * @return
	 */
	@Override
	public int adduhyuseefw(Map hyuser,Map btcuser,Map ethuser,Map eosuser) {
		int q=0;
		String hysucc_count =hyuser.get("hyb_succ_count")+"";
		String ztype=hyuser.get("ztype")+"";
		//更新合约减数量
		if("1".equals(ztype)){
			//合约转钱包
			Map jiuuser= new HashMap();
			jiuuser.put("userid",hyuser.get("userid"));
			jiuuser=findById_sql("HyuserMapper.findById",jiuuser);
			int hyb_count=0;
			BigDecimal hyb_succ_count= new BigDecimal(0.00);
			if (null!=jiuuser&&null!=jiuuser.get("hyb_count")&&!"".equals(jiuuser.get("hyb_count")+"")){
				hyb_count=Integer.valueOf(jiuuser.get("hyb_count")+"")+1;
			}
			if (null!=jiuuser&&null!=jiuuser.get("hyb_succ_count")&&!"".equals(jiuuser.get("hyb_succ_count")+"")){
				hyb_succ_count=new BigDecimal(jiuuser.get("hyb_succ_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
			}
			if (null!=jiuuser){
				jiuuser.put("hyb_count",hyb_count);
				if (hyb_succ_count.doubleValue()<=0){
					jiuuser.put("hyb_succ_count","0.00");
				}else{
					jiuuser.put("hyb_succ_count",hyb_succ_count);
				}
				q=edit_sql("HyuserMapper.edit", hyuser);
			}else{
				hyuser.put("hyb_count",hyb_count);
				hyuser.put("hyb_succ_count","0.00");
				hyuser.put("bzj_frozen","0.00");
				hyuser.put("hyb_frozen","0.00");
				hyuser.put("typezh","1");
				hyuser.put("createtime",longzong.format(new Date()));
				q=save_sql("HyuserMapper.save", hyuser);
			}
			BigDecimal zq= new BigDecimal(0.00);
			if ("1".equals(hyuser.get("type")+"")){
				BigDecimal hy_btc_count= new BigDecimal(0.00);
				BigDecimal hy_kybzj_count= new BigDecimal(0.00);
				Map btcmap= new HashMap();
				btcmap.put("userid",hyuser.get("userid"));
				btcmap=findById_sql("HybtcuserMapper.findById",btcmap);
				if (null!=btcmap&&null!=btcmap.get("hy_btc_count")&&!"".equals(btcmap.get("hy_btc_count")+"")){
					hy_btc_count=new BigDecimal(btcmap.get("hy_btc_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=btcmap&&null!=btcmap.get("hy_kybzj_count")&&!"".equals(btcmap.get("hy_kybzj_count")+"")){
					zq=new BigDecimal(btcmap.get("hy_kybzj_count")+"");
					hy_kybzj_count=new BigDecimal(btcmap.get("hy_kybzj_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=btcmap){
					if (hy_btc_count.doubleValue()<=0){
						btcmap.put("hy_btc_count","0.00");
					}else{
						btcmap.put("hy_btc_count",hy_btc_count);
					}
					if (hy_kybzj_count.doubleValue()<=0){
						btcmap.put("hy_kybzj_count","0.00");
					}else{
						btcmap.put("hy_kybzj_count",hy_kybzj_count);
					}
					q=edit_sql("HybtcuserMapper.edit", btcmap);
				}else{
					btcuser.put("hy_btc_count","0.00");
					btcuser.put("hy_kybzj_count","0.00");
					btcuser.put("hy_dzduo_count",new BigDecimal(0.00));
					btcuser.put("hy_dzkong_count",new BigDecimal(0.00));
					btcuser.put("hy_czduo_count",new BigDecimal(0.00));
					btcuser.put("hy_czkong_count",new BigDecimal(0.00));
					btcuser.put("hy_jdduo_count",new BigDecimal(0.00));
					btcuser.put("hy_jdkong_count",new BigDecimal(0.00));
					btcuser.put("hy_ysxyk_count",new BigDecimal(0.00));
					btcuser.put("hy_djbzj_count",new BigDecimal(0.00));
					btcuser.put("hy_yybzj_count",new BigDecimal(0.00));
					btcuser.put("userid",hyuser.get("userid"));
					btcuser.put("createtime",longzong.format(new Date()));
					q=save_sql("HybtcuserMapper.save", btcuser);

				}
				Map moneylog=new HashMap();
				moneylog.put("money",hyuser.get("hyb_succ_count"));
				moneylog.put("balance",zq);
				moneylog.put("content","资金划转btc");
				if("1".equals(ztype)){
					//合约转
					moneylog.put("type","10");
				}
				if("2".equals(ztype)){
					//法币转
					moneylog.put("type","12");
				}
				moneylog.put("paytype","1");//1btc 2eth 3eos
				moneylog.put("createtime",longzong.format(new Date()));
				moneylog.put("flag",null);
				moneylog.put("userid",hyuser.get("userid"));
				q=save_sql("HymoneylogMapper.save", moneylog);
			}
			if ("2".equals(hyuser.get("type")+"")){
				BigDecimal hy_eth_count= new BigDecimal(0.00);
				BigDecimal hy_kybzj_count= new BigDecimal(0.00);
				Map ethmap= new HashMap();
				ethmap.put("userid",hyuser.get("userid"));
				ethmap=findById_sql("HyethuserMapper.findById",ethmap);
				if (null!=ethmap&&null!=ethmap.get("hy_eth_count")&&!"".equals(ethmap.get("hy_eth_count")+"")){
					hy_eth_count=new BigDecimal(ethmap.get("hy_eth_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=ethmap&&null!=ethmap.get("hy_kybzj_count")&&!"".equals(ethmap.get("hy_kybzj_count")+"")){
					zq=new BigDecimal(ethmap.get("hy_kybzj_count")+"");
					hy_kybzj_count=new BigDecimal(ethmap.get("hy_kybzj_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=ethmap){
					if (hy_eth_count.doubleValue()<=0){
						ethmap.put("hy_eth_count","0.00");
					}else{
						ethmap.put("hy_eth_count",hy_eth_count);
					}
					if (hy_kybzj_count.doubleValue()<=0){
						ethmap.put("hy_kybzj_count","0.00");
					}else{
						ethmap.put("hy_kybzj_count",hy_kybzj_count);
					}
					q=edit_sql("HyethuserMapper.edit", ethmap);
				}else{
					ethuser.put("hy_eth_count","0.00");
					ethuser.put("hy_kybzj_count","0.00");
					ethuser.put("hy_dzduo_count",new BigDecimal(0.00));
					ethuser.put("hy_dzkong_count",new BigDecimal(0.00));
					ethuser.put("hy_czduo_count",new BigDecimal(0.00));
					ethuser.put("hy_czkong_count",new BigDecimal(0.00));
					ethuser.put("hy_jdduo_count",new BigDecimal(0.00));
					ethuser.put("hy_jdkong_count",new BigDecimal(0.00));
					ethuser.put("hy_ysxyk_count",new BigDecimal(0.00));
					ethuser.put("hy_djbzj_count",new BigDecimal(0.00));
					ethuser.put("hy_yybzj_count",new BigDecimal(0.00));
					ethuser.put("userid",hyuser.get("userid"));
					ethuser.put("createtime",longzong.format(new Date()));
					q=save_sql("HyethuserMapper.save", ethuser);
				}
				Map moneylog=new HashMap();
				moneylog.put("money",hyuser.get("hyb_succ_count"));
				moneylog.put("balance",zq);
				moneylog.put("content","资金划转eth");
				if("1".equals(ztype)){
					//钱包转
					moneylog.put("type","10");
				}
				if("2".equals(ztype)){
					//法币转
					moneylog.put("type","12");
				}
				moneylog.put("paytype","2");//1btc 2eth 3eos
				moneylog.put("createtime",longzong.format(new Date()));
				moneylog.put("flag",null);
				moneylog.put("userid",hyuser.get("userid"));
				q=save_sql("HymoneylogMapper.save", moneylog);
			}
			if ("3".equals(hyuser.get("type")+"")){
				BigDecimal hy_eos_count= new BigDecimal(0.00);
				BigDecimal hy_kybzj_count= new BigDecimal(0.00);
				Map eosmap= new HashMap();
				eosmap.put("userid",hyuser.get("userid"));
				eosmap=findById_sql("HyeosuserMapper.findById",eosmap);
				if (null!=eosmap&&null!=eosmap.get("hy_eos_count")&&!"".equals(eosmap.get("hy_eos_count")+"")){
					hy_eos_count=new BigDecimal(eosmap.get("hy_eos_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=eosmap&&null!=eosmap.get("hy_kybzj_count")&&!"".equals(eosmap.get("hy_kybzj_count")+"")){
					zq=new BigDecimal(eosmap.get("hy_kybzj_count")+"");
					hy_kybzj_count=new BigDecimal(eosmap.get("hy_kybzj_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=eosmap){
					if (hy_eos_count.doubleValue()<=0){
						eosmap.put("hy_eth_count","0.00");
					}else{
						eosmap.put("hy_eos_count",hy_eos_count);
					}
					if (hy_kybzj_count.doubleValue()<=0){
						eosmap.put("hy_kybzj_count","0.00");
					}else{
						eosmap.put("hy_kybzj_count",hy_kybzj_count);
					}


					q=edit_sql("HyeosuserMapper.edit", eosmap);
				}else{
					eosuser.put("hy_eos_count","0.00");
					eosuser.put("hy_kybzj_count","0.00");
					eosuser.put("hy_dzduo_count",new BigDecimal(0.00));
					eosuser.put("hy_dzkong_count",new BigDecimal(0.00));
					eosuser.put("hy_czduo_count",new BigDecimal(0.00));
					eosuser.put("hy_czkong_count",new BigDecimal(0.00));
					eosuser.put("hy_jdduo_count",new BigDecimal(0.00));
					eosuser.put("hy_jdkong_count",new BigDecimal(0.00));
					eosuser.put("hy_ysxyk_count",new BigDecimal(0.00));
					eosuser.put("hy_djbzj_count",new BigDecimal(0.00));
					eosuser.put("hy_yybzj_count",new BigDecimal(0.00));
					eosuser.put("userid",hyuser.get("userid"));
					eosuser.put("createtime",longzong.format(new Date()));
					q=save_sql("HyeosuserMapper.save", eosuser);

				}
				Map moneylog=new HashMap();
				moneylog.put("money",hyuser.get("hyb_succ_count"));
				moneylog.put("balance",zq);
				moneylog.put("content","资金划转eos");
				if("1".equals(ztype)){
					//合约转
					moneylog.put("type","10");
				}
				if("2".equals(ztype)){
					//法币转
					moneylog.put("type","12");
				}
				moneylog.put("paytype","3");//1btc 2eth 3eos
				moneylog.put("createtime",longzong.format(new Date()));
				moneylog.put("flag",null);
				moneylog.put("userid",hyuser.get("userid"));
				q=save_sql("HymoneylogMapper.save", moneylog);
			}
		}
		//更新法币减数量
		if("2".equals(ztype)){
			//法币转钱包
			if ("1".equals(hyuser.get("type")+"")){
				//法币转
				Map c2cmap= new HashMap();
				c2cmap.put("userid",hyuser.get("userid"));
				c2cmap.put("id","1");
				c2cmap.put("defaultEnName","BTC");
				c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
					amount=new BigDecimal(c2cmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}
				Map c2ccmap= new HashMap();
				c2ccmap.put("userid",hyuser.get("userid"));
				c2ccmap.put("coinId",c2cmap.get("coinId"));
				if (amount.doubleValue()<=0){
					c2ccmap.put("amount","0.00");
				}else{
					c2ccmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editc2c", c2ccmap);
			}
			if ("2".equals(hyuser.get("type")+"")){
				//法币转
				Map c2cmap= new HashMap();
				c2cmap.put("userid",hyuser.get("userid"));
				c2cmap.put("id","2");
				c2cmap.put("defaultEnName","ETH");
				c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
					amount=new BigDecimal(c2cmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}
				Map c2ccmap= new HashMap();
				c2ccmap.put("userid",hyuser.get("userid"));
				c2ccmap.put("coinId",c2cmap.get("coinId"));
				if (amount.doubleValue()<=0){
					c2ccmap.put("amount","0.00");
				}else{
					c2ccmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editc2c", c2ccmap);
			}
			if ("3".equals(hyuser.get("type")+"")){
				//法币转
				Map c2cmap= new HashMap();
				c2cmap.put("userid",hyuser.get("userid"));
				c2cmap.put("id","3");
				c2cmap.put("defaultEnName","EOS");
				c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
					amount=new BigDecimal(c2cmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}
				Map c2ccmap= new HashMap();
				c2ccmap.put("userid",hyuser.get("userid"));
				c2ccmap.put("coinId",c2cmap.get("coinId"));
				if (amount.doubleValue()<=0){
					c2ccmap.put("amount","0.00");
				}else{
					c2ccmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editc2c", c2ccmap);
			}
		}
		//更新钱包加数量
		if ("1".equals(hyuser.get("type")+"")){
			//钱包
			Map fwmap= new HashMap();
			fwmap.put("userid",hyuser.get("userid"));
			fwmap.put("id","1");
			fwmap.put("defaultEnName","BTC");
			fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
			BigDecimal amount= new BigDecimal(0.00);
			if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
				amount=new BigDecimal(hysucc_count).add(new BigDecimal(fwmap.get("amount")+""));
			}

			Map fwWmap= new HashMap();
			fwWmap.put("userid",hyuser.get("userid"));
			fwWmap.put("coinId",fwmap.get("coinId"));
			if (amount.doubleValue()<=0){
				fwWmap.put("amount","0.00");
			}else{
				fwWmap.put("amount",amount);
			}
			q=edit_sql("TestMapper.editfinance", fwWmap);
		}
		if ("2".equals(hyuser.get("type")+"")){
			//钱包
			Map fwmap= new HashMap();
			fwmap.put("userid",hyuser.get("userid"));
			fwmap.put("id","2");
			fwmap.put("defaultEnName","ETH");
			fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
			BigDecimal amount= new BigDecimal(0.00);
			if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
				amount=new BigDecimal(hysucc_count).add(new BigDecimal(fwmap.get("amount")+""));
			}

			Map fwWmap= new HashMap();
			fwWmap.put("userid",hyuser.get("userid"));
			fwWmap.put("coinId",fwmap.get("coinId"));
			if (amount.doubleValue()<=0){
				fwWmap.put("amount","0.00");
			}else{
				fwWmap.put("amount",amount);
			}
			q=edit_sql("TestMapper.editfinance", fwWmap);
		}
		if ("3".equals(hyuser.get("type")+"")){
			//钱包转
			Map fwmap= new HashMap();
			fwmap.put("userid",hyuser.get("userid"));
			fwmap.put("id","3");
			fwmap.put("defaultEnName","EOS");
			fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
			BigDecimal amount= new BigDecimal(0.00);
			if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
				amount=new BigDecimal(hysucc_count).add(new BigDecimal(fwmap.get("amount")+""));
			}

			Map fwWmap= new HashMap();
			fwWmap.put("userid",hyuser.get("userid"));
			fwWmap.put("coinId",fwmap.get("coinId"));
			if (amount.doubleValue()<=0){
				fwWmap.put("amount","0.00");
			}else{
				fwWmap.put("amount",amount);
			}
			q=edit_sql("TestMapper.editfinance", fwWmap);
		}
		return q;
	}

	/**
	 * 合约和钱包转法币资金划转
	 * @param hyuser
	 * @param btcuser
	 * @param ethuser
	 * @param eosuser
	 * @return
	 */
	@Override
	public int adduhyuseec2c(Map hyuser,Map btcuser,Map ethuser,Map eosuser) {
		int q=0;
		String hysucc_count =hyuser.get("hyb_succ_count")+"";
		String ztype=hyuser.get("ztype")+"";
		//更新合约减数量
		if("1".equals(ztype)){
			//合约转钱包
			Map jiuuser= new HashMap();
			jiuuser.put("userid",hyuser.get("userid"));
			jiuuser=findById_sql("HyuserMapper.findById",jiuuser);
			int hyb_count=0;
			BigDecimal hyb_succ_count= new BigDecimal(0.00);
			if (null!=jiuuser&&null!=jiuuser.get("hyb_count")&&!"".equals(jiuuser.get("hyb_count")+"")){
				hyb_count=Integer.valueOf(jiuuser.get("hyb_count")+"")+1;
			}
			if (null!=jiuuser&&null!=jiuuser.get("hyb_succ_count")&&!"".equals(jiuuser.get("hyb_succ_count")+"")){
				hyb_succ_count=new BigDecimal(jiuuser.get("hyb_succ_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
			}
			if (null!=jiuuser){
				jiuuser.put("hyb_count",hyb_count);
				if (hyb_succ_count.doubleValue()<=0){
					jiuuser.put("hyb_succ_count","0.00");
				}else{
					jiuuser.put("hyb_succ_count",hyb_succ_count);
				}
				q=edit_sql("HyuserMapper.edit", hyuser);
			}else{
				hyuser.put("hyb_count",hyb_count);
				hyuser.put("hyb_succ_count","0.00");
				hyuser.put("bzj_frozen","0.00");
				hyuser.put("hyb_frozen","0.00");
				hyuser.put("typezh","1");
				hyuser.put("createtime",longzong.format(new Date()));
				q=save_sql("HyuserMapper.save", hyuser);
			}
			BigDecimal zq= new BigDecimal(0.00);
			if ("1".equals(hyuser.get("type")+"")){
				BigDecimal hy_btc_count= new BigDecimal(0.00);
				BigDecimal hy_kybzj_count= new BigDecimal(0.00);
				Map btcmap= new HashMap();
				btcmap.put("userid",hyuser.get("userid"));
				btcmap=findById_sql("HybtcuserMapper.findById",btcmap);
				if (null!=btcmap&&null!=btcmap.get("hy_btc_count")&&!"".equals(btcmap.get("hy_btc_count")+"")){
					hy_btc_count=new BigDecimal(btcmap.get("hy_btc_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=btcmap&&null!=btcmap.get("hy_kybzj_count")&&!"".equals(btcmap.get("hy_kybzj_count")+"")){
					zq=new BigDecimal(btcmap.get("hy_kybzj_count")+"");
					hy_kybzj_count=new BigDecimal(btcmap.get("hy_kybzj_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=btcmap){
					if (hy_btc_count.doubleValue()<=0){
						btcmap.put("hy_btc_count","0.00");
					}else{
						btcmap.put("hy_btc_count",hy_btc_count);
					}
					if (hy_kybzj_count.doubleValue()<=0){
						btcmap.put("hy_kybzj_count","0.00");
					}else{
						btcmap.put("hy_kybzj_count",hy_kybzj_count);
					}
					q=edit_sql("HybtcuserMapper.edit", btcmap);
				}else{
					btcuser.put("hy_btc_count","0.00");
					btcuser.put("hy_kybzj_count","0.00");
					btcuser.put("hy_dzduo_count",new BigDecimal(0.00));
					btcuser.put("hy_dzkong_count",new BigDecimal(0.00));
					btcuser.put("hy_czduo_count",new BigDecimal(0.00));
					btcuser.put("hy_czkong_count",new BigDecimal(0.00));
					btcuser.put("hy_jdduo_count",new BigDecimal(0.00));
					btcuser.put("hy_jdkong_count",new BigDecimal(0.00));
					btcuser.put("hy_ysxyk_count",new BigDecimal(0.00));
					btcuser.put("hy_djbzj_count",new BigDecimal(0.00));
					btcuser.put("hy_yybzj_count",new BigDecimal(0.00));
					btcuser.put("userid",hyuser.get("userid"));
					btcuser.put("createtime",longzong.format(new Date()));
					q=save_sql("HybtcuserMapper.save", btcuser);

				}
				Map moneylog=new HashMap();
				moneylog.put("money",hyuser.get("hyb_succ_count"));
				moneylog.put("balance",zq);
				moneylog.put("content","资金划转btc");
				if("1".equals(ztype)){
					//合约转
					moneylog.put("type","10");
				}
				if("2".equals(ztype)){
					//法币转
					moneylog.put("type","12");
				}
				moneylog.put("paytype","1");//1btc 2eth 3eos
				moneylog.put("createtime",longzong.format(new Date()));
				moneylog.put("flag",null);
				moneylog.put("userid",hyuser.get("userid"));
				q=save_sql("HymoneylogMapper.save", moneylog);
			}
			if ("2".equals(hyuser.get("type")+"")){
				BigDecimal hy_eth_count= new BigDecimal(0.00);
				BigDecimal hy_kybzj_count= new BigDecimal(0.00);
				Map ethmap= new HashMap();
				ethmap.put("userid",hyuser.get("userid"));
				ethmap=findById_sql("HyethuserMapper.findById",ethmap);
				if (null!=ethmap&&null!=ethmap.get("hy_eth_count")&&!"".equals(ethmap.get("hy_eth_count")+"")){
					hy_eth_count=new BigDecimal(ethmap.get("hy_eth_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=ethmap&&null!=ethmap.get("hy_kybzj_count")&&!"".equals(ethmap.get("hy_kybzj_count")+"")){
					zq=new BigDecimal(ethmap.get("hy_kybzj_count")+"");
					hy_kybzj_count=new BigDecimal(ethmap.get("hy_kybzj_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=ethmap){
					if (hy_eth_count.doubleValue()<=0){
						ethmap.put("hy_eth_count","0.00");
					}else{
						ethmap.put("hy_eth_count",hy_eth_count);
					}
					if (hy_kybzj_count.doubleValue()<=0){
						ethmap.put("hy_kybzj_count","0.00");
					}else{
						ethmap.put("hy_kybzj_count",hy_kybzj_count);
					}
					q=edit_sql("HyethuserMapper.edit", ethmap);
				}else{
					ethuser.put("hy_eth_count","0.00");
					ethuser.put("hy_kybzj_count","0.00");
					ethuser.put("hy_dzduo_count",new BigDecimal(0.00));
					ethuser.put("hy_dzkong_count",new BigDecimal(0.00));
					ethuser.put("hy_czduo_count",new BigDecimal(0.00));
					ethuser.put("hy_czkong_count",new BigDecimal(0.00));
					ethuser.put("hy_jdduo_count",new BigDecimal(0.00));
					ethuser.put("hy_jdkong_count",new BigDecimal(0.00));
					ethuser.put("hy_ysxyk_count",new BigDecimal(0.00));
					ethuser.put("hy_djbzj_count",new BigDecimal(0.00));
					ethuser.put("hy_yybzj_count",new BigDecimal(0.00));
					ethuser.put("userid",hyuser.get("userid"));
					ethuser.put("createtime",longzong.format(new Date()));
					q=save_sql("HyethuserMapper.save", ethuser);
				}
				Map moneylog=new HashMap();
				moneylog.put("money",hyuser.get("hyb_succ_count"));
				moneylog.put("balance",zq);
				moneylog.put("content","资金划转eth");
				if("1".equals(ztype)){
					//钱包转
					moneylog.put("type","10");
				}
				if("2".equals(ztype)){
					//法币转
					moneylog.put("type","12");
				}
				moneylog.put("paytype","2");//1btc 2eth 3eos
				moneylog.put("createtime",longzong.format(new Date()));
				moneylog.put("flag",null);
				moneylog.put("userid",hyuser.get("userid"));
				q=save_sql("HymoneylogMapper.save", moneylog);
			}
			if ("3".equals(hyuser.get("type")+"")){
				BigDecimal hy_eos_count= new BigDecimal(0.00);
				BigDecimal hy_kybzj_count= new BigDecimal(0.00);
				Map eosmap= new HashMap();
				eosmap.put("userid",hyuser.get("userid"));
				eosmap=findById_sql("HyeosuserMapper.findById",eosmap);
				if (null!=eosmap&&null!=eosmap.get("hy_eos_count")&&!"".equals(eosmap.get("hy_eos_count")+"")){
					hy_eos_count=new BigDecimal(eosmap.get("hy_eos_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}
				if (null!=eosmap&&null!=eosmap.get("hy_kybzj_count")&&!"".equals(eosmap.get("hy_kybzj_count")+"")){
					zq=new BigDecimal(eosmap.get("hy_kybzj_count")+"");
					hy_kybzj_count=new BigDecimal(eosmap.get("hy_kybzj_count")+"").subtract(new BigDecimal(hyuser.get("hyb_succ_count")+"")).setScale(10, BigDecimal.ROUND_HALF_UP);;
				}

				if (null!=eosmap){
					if (hy_eos_count.doubleValue()<=0){
						eosmap.put("hy_eth_count","0.00");
					}else{
						eosmap.put("hy_eos_count",hy_eos_count);
					}
					if (hy_kybzj_count.doubleValue()<=0){
						eosmap.put("hy_kybzj_count","0.00");
					}else{
						eosmap.put("hy_kybzj_count",hy_kybzj_count);
					}


					q=edit_sql("HyeosuserMapper.edit", eosmap);
				}else{
					eosuser.put("hy_eos_count","0.00");
					eosuser.put("hy_kybzj_count","0.00");
					eosuser.put("hy_dzduo_count",new BigDecimal(0.00));
					eosuser.put("hy_dzkong_count",new BigDecimal(0.00));
					eosuser.put("hy_czduo_count",new BigDecimal(0.00));
					eosuser.put("hy_czkong_count",new BigDecimal(0.00));
					eosuser.put("hy_jdduo_count",new BigDecimal(0.00));
					eosuser.put("hy_jdkong_count",new BigDecimal(0.00));
					eosuser.put("hy_ysxyk_count",new BigDecimal(0.00));
					eosuser.put("hy_djbzj_count",new BigDecimal(0.00));
					eosuser.put("hy_yybzj_count",new BigDecimal(0.00));
					eosuser.put("userid",hyuser.get("userid"));
					eosuser.put("createtime",longzong.format(new Date()));
					q=save_sql("HyeosuserMapper.save", eosuser);

				}
				Map moneylog=new HashMap();
				moneylog.put("money",hyuser.get("hyb_succ_count"));
				moneylog.put("balance",zq);
				moneylog.put("content","资金划转eos");
				if("1".equals(ztype)){
					//合约转
					moneylog.put("type","10");
				}
				if("2".equals(ztype)){
					//法币转
					moneylog.put("type","12");
				}
				moneylog.put("paytype","3");//1btc 2eth 3eos
				moneylog.put("createtime",longzong.format(new Date()));
				moneylog.put("flag",null);
				moneylog.put("userid",hyuser.get("userid"));
				q=save_sql("HymoneylogMapper.save", moneylog);
			}
		}
		//更新钱包减数量
		if("2".equals(ztype)){
			if ("1".equals(hyuser.get("type")+"")){
				//钱包
				Map fwmap= new HashMap();
				fwmap.put("userid",hyuser.get("userid"));
				fwmap.put("id","1");
				fwmap.put("defaultEnName","BTC");
				fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
					amount=new BigDecimal(fwmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}

				Map fwWmap= new HashMap();
				fwWmap.put("userid",hyuser.get("userid"));
				fwWmap.put("coinId",fwmap.get("coinId"));
				if (amount.doubleValue()<=0){
					fwWmap.put("amount","0.00");
				}else{
					fwWmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editfinance", fwWmap);
			}
			if ("2".equals(hyuser.get("type")+"")){
				//钱包
				Map fwmap= new HashMap();
				fwmap.put("userid",hyuser.get("userid"));
				fwmap.put("id","2");
				fwmap.put("defaultEnName","ETH");
				fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
					amount=new BigDecimal(fwmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}

				Map fwWmap= new HashMap();
				fwWmap.put("userid",hyuser.get("userid"));
				fwWmap.put("coinId",fwmap.get("coinId"));
				if (amount.doubleValue()<=0){
					fwWmap.put("amount","0.00");
				}else{
					fwWmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editfinance", fwWmap);
			}
			if ("3".equals(hyuser.get("type")+"")){
				//钱包转
				Map fwmap= new HashMap();
				fwmap.put("userid",hyuser.get("userid"));
				fwmap.put("id","3");
				fwmap.put("defaultEnName","EOS");
				fwmap=findById_sql("TestMapper.findfinance_wallet",fwmap);
				BigDecimal amount= new BigDecimal(0.00);
				if (null!=fwmap&&null!=fwmap.get("amount")&&!"".equals(fwmap.get("amount")+"")){
					amount=new BigDecimal(fwmap.get("amount")+"").subtract(new BigDecimal(hysucc_count));
				}

				Map fwWmap= new HashMap();
				fwWmap.put("userid",hyuser.get("userid"));
				fwWmap.put("coinId",fwmap.get("coinId"));
				if (amount.doubleValue()<=0){
					fwWmap.put("amount","0.00");
				}else{
					fwWmap.put("amount",amount);
				}
				q=edit_sql("TestMapper.editfinance", fwWmap);
			}
		}
		//更新法币加数量
		//转法币
		if ("1".equals(hyuser.get("type")+"")){
			//法币转
			Map c2cmap= new HashMap();
			c2cmap.put("userid",hyuser.get("userid"));
			c2cmap.put("id","1");
			c2cmap.put("defaultEnNam e","BTC");
			c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
			BigDecimal amount= new BigDecimal(0.00);
			if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
				amount=new BigDecimal(hysucc_count).add(new BigDecimal(c2cmap.get("amount")+""));
			}
			Map c2ccmap= new HashMap();
			c2ccmap.put("userid",hyuser.get("userid"));
			c2ccmap.put("coinId",c2cmap.get("coinId"));
			if (amount.doubleValue()<=0){
				c2ccmap.put("amount","0.00");
			}else{
				c2ccmap.put("amount",amount);
			}
			q=edit_sql("TestMapper.editc2c", c2ccmap);
		}
		if ("2".equals(hyuser.get("type")+"")){
			//法币转
			Map c2cmap= new HashMap();
			c2cmap.put("userid",hyuser.get("userid"));
			c2cmap.put("id","2");
			c2cmap.put("defaultEnName","ETH");
			c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
			BigDecimal amount= new BigDecimal(0.00);
			if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
				amount=new BigDecimal(hysucc_count).add(new BigDecimal(c2cmap.get("amount")+""));
			}
			Map c2ccmap= new HashMap();
			c2ccmap.put("userid",hyuser.get("userid"));
			c2ccmap.put("coinId",c2cmap.get("coinId"));
			if (amount.doubleValue()<=0){
				c2ccmap.put("amount","0.00");
			}else{
				c2ccmap.put("amount",amount);
			}
			q=edit_sql("TestMapper.editc2c", c2ccmap);
		}
		if ("3".equals(hyuser.get("type")+"")){
			//法币转
			Map c2cmap= new HashMap();
			c2cmap.put("userid",hyuser.get("userid"));
			c2cmap.put("id","3");
			c2cmap.put("defaultEnName","EOS");
			c2cmap=findById_sql("TestMapper.findc2c_wallet",c2cmap);
			BigDecimal amount= new BigDecimal(0.00);
			if (null!=c2cmap&&null!=c2cmap.get("amount")&&!"".equals(c2cmap.get("amount")+"")){
				amount=new BigDecimal(hysucc_count).add(new BigDecimal(c2cmap.get("amount")+""));
			}
			Map c2ccmap= new HashMap();
			c2ccmap.put("userid",hyuser.get("userid"));
			c2ccmap.put("coinId",c2cmap.get("coinId"));
			if (amount.doubleValue()<=0){
				c2ccmap.put("amount","0.00");
			}else{
				c2ccmap.put("amount",amount);
			}
			q=edit_sql("TestMapper.editc2c", c2ccmap);

		}

		return q;
	}
	public int addbzj(Map maouy,Map zuser,Map user,Map maouylog){
		//更新订单
		int q=edit_sql("HyorderMapper.edit", maouy);
		//修改子账户余额
		if ("1".equals(maouy.get("type")+"")){
			q=edit_sql("HybtcuserMapper.edit", zuser);
		}
		if ("2".equals(maouy.get("type")+"")){
			q=edit_sql("HyethuserMapper.edit", zuser);
		}
		if ("3".equals(maouy.get("type")+"")){
			q=edit_sql("HyeosuserMapper.edit", zuser);
		}
		//修改合约账户余额
		q=edit_sql("HyuserMapper.edit", user);
		q=save_sql("HymoneylogMapper.save", maouylog);
		return q;
	}

	/**
	 * 是否爆仓
	 * @param order
	 * @return
	 */
	public int SFbc(Map order){
		int q=0;
		//判断是逐仓还是全仓
		Map hyuser= new HashMap();
		hyuser.put("userid",order.get("userid"));
		hyuser=findById_sql("HyuserMapper.findById",hyuser);

		BigDecimal price=(BigDecimal)order.get("price");//开仓价格
		BigDecimal levers=new BigDecimal(order.get("levers")+"");//杠杆倍数
		BigDecimal bondprice=(BigDecimal)order.get("bondprice");//保证金
		BigDecimal surplusnum=(BigDecimal)order.get("surplusnum");//数量
		BigDecimal hy_wsxyk_count=new BigDecimal(0);//未实现盈亏
		BigDecimal syl1= (new BigDecimal(""+order.get("pricetb")).subtract(price));
		BigDecimal syl2= (new BigDecimal(""+order.get("pricetb")).multiply(levers));
		BigDecimal syl= syl1.divide(syl2,10, BigDecimal.ROUND_HALF_UP);//收益率
		BigDecimal sy=bondprice.multiply(syl).setScale(10, BigDecimal.ROUND_HALF_UP);
		hy_wsxyk_count=hy_wsxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
		//更新订单的未实现盈亏
		Map wsxsy= new HashMap();
		wsxsy.put("hy_order_id",order.get("hy_order_id"));
		wsxsy.put("wsxsyprice",hy_wsxyk_count+"");
		wsxsy.put("profitrate",syl+"");
		wsxsy.put("profitprice",sy+"");
		order.put("wsxsyprice",hy_wsxyk_count+"");

		if (edit_sql("HyorderMapper.edit",wsxsy)>0){
			//计算是否爆仓 1逐仓2全仓
			if (null!=hyuser&&"1".equals(hyuser.get("typezh")+"")){
				//逐仓保证金率=（固定保证金+未实现盈亏）*开仓均价*杠杆／（合约面值*持仓数量）
				//btc1张为100美金，其余币种为10美金
				//（固定保证金+未实现盈亏）
				BigDecimal gdwsx= bondprice.add(hy_wsxyk_count);
				//（合约面值*持仓数量）
				BigDecimal hycc=new BigDecimal(0.00);
				//1 BTC 2 ETH 3 EOS
				if ("1".equals(order.get("type")+"")){
					hycc=new BigDecimal(100.00).multiply(surplusnum);
				}
				if ("2".equals(order.get("type")+"")){
					hycc=new BigDecimal(10.00).multiply(surplusnum);
				}
				if ("3".equals(order.get("type")+"")){
					hycc=new BigDecimal(10.00).multiply(surplusnum);
				}
				//保证金率
				BigDecimal bzjl=gdwsx.multiply(price).multiply(levers).divide(hycc);
				bzjl=bzjl.multiply(new BigDecimal(100));
				//当保证金率小于等于杠杆，才会触发爆仓线
				if (bzjl.doubleValue()<=levers.doubleValue()){
					//爆仓超出多少
					if (sy.doubleValue()<0){
						double bcccprice=bondprice.subtract(sy.abs()).doubleValue();
						if (bcccprice<0){
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","5");//爆仓
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",order.get("hy_order_id"));
							wsxsy1.put("exploprice",sy.abs().subtract(bondprice));
							wsxsy1.put("explopriceqs",sy.abs().subtract(bondprice));
							wsxsy1.put("pctime",longzong.format(new Date()));
							wsxsy1.put("sussprice",order.get("pricetb")+"");
							edit_sql("HyorderMapper.edit",wsxsy1);

							if (sy.doubleValue()>0){
								order.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								order.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								order.put("yktype","3");
							}
							order.put("hangtype","5");//爆仓
							order.put("profitprice",sy);
							order.put("hy_order_id",order.get("hy_order_id"));
							order.put("exploprice",sy.abs().subtract(bondprice));
							order.put("explopriceqs",sy.abs().subtract(bondprice));
							order.put("pctime",longzong.format(new Date()));

						}
					}else if (sy.doubleValue()>0){
						double bcccprice=bondprice.subtract(sy).doubleValue();
						if (bcccprice<0){
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","5");//爆仓
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",order.get("hy_order_id"));
							wsxsy1.put("exploprice",sy.subtract(bondprice));
							wsxsy1.put("explopriceqs",sy.subtract(bondprice));
							wsxsy1.put("pctime",longzong.format(new Date()));
							wsxsy1.put("sussprice",order.get("pricetb")+"");
							edit_sql("HyorderMapper.edit",wsxsy1);

							if (sy.doubleValue()>0){
								order.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								order.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								order.put("yktype","3");
							}
							order.put("hangtype","5");//爆仓
							order.put("profitprice",sy);
							order.put("hy_order_id",order.get("hy_order_id"));
							order.put("exploprice",sy.subtract(bondprice));
							order.put("explopriceqs",sy.subtract(bondprice));
							order.put("pctime",longzong.format(new Date()));


						}
					}

					//强制平仓
					selling(String.valueOf(order.get("hy_order_id")),String.valueOf(order.get("userid")),"1",order);
					//清空订单未实现盈利
					Map wsxsy1= new HashMap();
					wsxsy1.put("hy_order_id",order.get("hy_order_id"));
					wsxsy1.put("wsxsyprice",0.00+"");
					edit_sql("HyorderMapper.edit",wsxsy1);
				}

			}
			if (null!=hyuser&&"2".equals(hyuser.get("typezh")+"")){
				//全仓保证金率=账户权益／（用户持仓所需的保证金+挂单冻结保证金）
				//btc1张为100美金，其余币种为10美金
				//账户权益
				BigDecimal zhqy= new BigDecimal(0.00);
				//挂单冻结保证金
				BigDecimal gddj=new BigDecimal(0.00);
				//1 BTC 2 ETH 3 EOS
				if ("1".equals(order.get("type")+"")){
					Map hyuserzh= new HashMap();
					hyuserzh.put("userid",order.get("userid"));
					hyuserzh=findById_sql("HybtcuserMapper.findById",hyuserzh);
					zhqy=(BigDecimal)hyuserzh.get("hy_kybzj_count");
					hyuserzh= new HashMap();
					hyuserzh.put("type",order.get("type"));
					hyuserzh.put("datetype",order.get("datetype"));
					hyuserzh.put("usertype",order.get("usertype"));
					hyuserzh.put("hangtype","2");
					hyuserzh.put("pagenum",0);
					hyuserzh.put("pagesize",1);
					List<Map> listmap=listAll_sql("HyorderMapper.listAllsum",hyuserzh);
					if (null!=listmap&&listmap.size()>0){
						gddj=new BigDecimal(listmap.get(0).get("zongbondprice")+"");
					}
				}
				if ("2".equals(order.get("type")+"")){
					Map hyuserzh= new HashMap();
					hyuserzh.put("userid",order.get("userid"));
					hyuserzh=findById_sql("HyethuserMapper.findById",hyuserzh);
					zhqy=(BigDecimal)hyuserzh.get("hy_kybzj_count");
					hyuserzh= new HashMap();
					hyuserzh.put("type",order.get("type"));
					hyuserzh.put("datetype",order.get("datetype"));
					hyuserzh.put("usertype",order.get("usertype"));
					hyuserzh.put("hangtype","2");
					hyuserzh.put("pagenum",0);
					hyuserzh.put("pagesize",1);
					List<Map> listmap=listAll_sql("HyorderMapper.listAllsum",hyuserzh);
					if (null!=listmap&&listmap.size()>0){
						gddj=new BigDecimal(listmap.get(0).get("zongbondprice")+"");
					}
				}
				if ("3".equals(order.get("type")+"")){
					Map hyuserzh= new HashMap();
					hyuserzh.put("userid",order.get("userid"));
					hyuserzh=findById_sql("HyeosuserMapper.findById",hyuserzh);
					zhqy=(BigDecimal)hyuserzh.get("hy_kybzj_count");
					hyuserzh= new HashMap();
					hyuserzh.put("type",order.get("type"));
					hyuserzh.put("datetype",order.get("datetype"));
					hyuserzh.put("usertype",order.get("usertype"));
					hyuserzh.put("hangtype","2");
					hyuserzh.put("pagenum",0);
					hyuserzh.put("pagesize",1);
					List<Map> listmap=listAll_sql("HyorderMapper.listAllsum",hyuserzh);
					if (null!=listmap&&listmap.size()>0){
						gddj=new BigDecimal(listmap.get(0).get("zongbondprice")+"");
					}
				}
				//保证金率 全仓保证金率=账户权益／（用户持仓所需的保证金+挂单冻结保证金）
				BigDecimal bzjl=zhqy.divide((gddj.add(bondprice)));
				bzjl=bzjl.multiply(new BigDecimal(100));
				//当保证金率小于等于杠杆，才会触发爆仓线
				if (bzjl.doubleValue()<=levers.doubleValue()){
					//爆仓超出多少
					if (sy.doubleValue()<0){
						double bcccprice=bondprice.subtract(sy.abs()).doubleValue();
						if (bcccprice<0){
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","5");//爆仓
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",order.get("hy_order_id"));
							wsxsy1.put("exploprice",sy.abs().subtract(bondprice));
							wsxsy1.put("explopriceqs",sy.abs().subtract(bondprice));
							wsxsy1.put("pctime",longzong.format(new Date()));
							wsxsy1.put("sussprice",order.get("pricetb")+"");
							edit_sql("HyorderMapper.edit",wsxsy1);

							if (sy.doubleValue()>0){
								order.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								order.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								order.put("yktype","3");
							}
							order.put("hangtype","5");//爆仓
							order.put("profitprice",sy);
							order.put("hy_order_id",order.get("hy_order_id"));
							order.put("exploprice",sy.abs().subtract(bondprice));
							order.put("explopriceqs",sy.abs().subtract(bondprice));
							order.put("pctime",longzong.format(new Date()));

						}
					}else if (sy.doubleValue()>0){
						double bcccprice=bondprice.subtract(sy).doubleValue();
						if (bcccprice<0){
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","5");//爆仓
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",order.get("hy_order_id"));
							wsxsy1.put("exploprice",sy.subtract(bondprice));
							wsxsy1.put("explopriceqs",sy.subtract(bondprice));
							wsxsy1.put("pctime",longzong.format(new Date()));
							wsxsy1.put("sussprice",order.get("pricetb")+"");
							edit_sql("HyorderMapper.edit",wsxsy1);

							if (sy.doubleValue()>0){
								order.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								order.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								order.put("yktype","3");
							}
							order.put("hangtype","5");//爆仓
							order.put("profitprice",sy);
							order.put("hy_order_id",order.get("hy_order_id"));
							order.put("exploprice",sy.subtract(bondprice));
							order.put("explopriceqs",sy.subtract(bondprice));
							order.put("pctime",longzong.format(new Date()));

						}
					}

					//强制平仓
					selling(String.valueOf(order.get("hy_order_id")),String.valueOf(order.get("userid")),"1",order);
					//清空订单未实现盈利
					Map wsxsy1= new HashMap();
					wsxsy1.put("hy_order_id",order.get("hy_order_id"));
					wsxsy1.put("wsxsyprice",0.00);
					edit_sql("HyorderMapper.edit",wsxsy1);
				}

			}


		}
		return q;
	}

	/**
	 * 交割
	 * @return
	 */
	public int jgcc(Map order) {
		int q=0;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
			String formatStr =formatter.format(new Date())+ "16:00:00";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date dt1 = df.parse(formatStr);
			//1 BTC 2 ETH 3 EOS
			Map maporder =new HashMap();
			maporder.put("hangtype","2");//1挂单2持仓3平仓4交割5爆仓
			maporder.put("jgtime1",dt1);
			List<Map> listorder =listAll_sql("HyorderMapper.listAll",maporder);
			List<Map> listsysfor =listAll_sql("TestMapper.findhysysinformation",new HashMap());
			//1 BTC 2 ETH 3 EOS 的起始价
			BigDecimal pricetb=new BigDecimal(0.00);
			//取k线现价
			/*List<Map> linelist=listAll_sql("TestMapper.findhyhy_kline",order);
			if (null!=linelist&&linelist.size()>0){
				//计算收益
				if (null!=linelist.get(0).get("hy_kline_last")&&!"".equals(linelist.get(0).get("hy_kline_last"))){
					pricetb=(BigDecimal) linelist.get(0).get("hy_kline_last");
				}
			}*/
			List<Map> linelist=listAll_sql("TestMapper.findhyhy_kliness1",order);//修改 原findhyhy_kliness
			System.out.println(JSON.toJSON(linelist));
			if (null!=linelist&&linelist.size()>0&&linelist.size()==2){
				//计算收益
				BigDecimal keyvalue_price1=(BigDecimal)linelist.get(0).get("keyvalue_price");
				BigDecimal keyvalue_num1=(BigDecimal)linelist.get(0).get("keyvalue_num");
				BigDecimal keyvalue_price2=(BigDecimal)linelist.get(1).get("keyvalue_price");
				BigDecimal keyvalue_num2=(BigDecimal)linelist.get(1).get("keyvalue_num");
				BigDecimal zong1=keyvalue_price1.multiply(keyvalue_num1);
				BigDecimal zong2=keyvalue_price2.multiply(keyvalue_num2);
				BigDecimal zongnum=keyvalue_num1.add(keyvalue_num2);
				BigDecimal zong12=zong1.add(zong2);
				BigDecimal prc=zong12.divide(zongnum,10, BigDecimal.ROUND_HALF_UP);
				pricetb=new BigDecimal(prc+"");
			}
            System.out.println("==================实时价格<"+pricetb+">=======================");
			List<Map> bcuser= new ArrayList<>();//爆仓人
			List<Map> zcuser= new ArrayList<>();//没有爆仓人
			List<Map> fexuser= new ArrayList<>();//更新没有爆仓人
			if (null!=listorder&&listorder.size()>0){
				for (int i = 0; i <listorder.size() ; i++) {
					Map zzhye=listorder.get(i);
					if (pricetb.doubleValue()==0&&null!=listsysfor&&listsysfor.size()>0){
						Map sysmap=listsysfor.get(0);
						if ("1".equals(zzhye.get("type")+"")){
							if (null!=sysmap&&null!=sysmap.get("btcbeginprice")&&!"".equals(sysmap.get("btcbeginprice")+"")){
								pricetb=(BigDecimal)sysmap.get("btcbeginprice");
							}
						}
						if ("2".equals(zzhye.get("type")+"")){
							if (null!=sysmap&&null!=sysmap.get("ethbeginprice")&&!"".equals(sysmap.get("ethbeginprice")+"")){
								pricetb=(BigDecimal)sysmap.get("ethbeginprice");
							}
						}
						if ("3".equals(zzhye.get("type")+"")){
							if (null!=sysmap&&null!=sysmap.get("eosbeginprice")&&!"".equals(sysmap.get("eosbeginprice")+"")){
								pricetb=(BigDecimal)sysmap.get("eosbeginprice");
							}
						}
					}
					zzhye.put("pricetb",pricetb);
					zzhye.put("cjprice",pricetb);
					BigDecimal price=(BigDecimal)zzhye.get("price");//开仓价格
					BigDecimal levers=new BigDecimal(zzhye.get("levers")+"");//杠杆倍数
					BigDecimal bondprice=(BigDecimal)zzhye.get("bondprice");//保证金
					BigDecimal surplusnum=(BigDecimal)zzhye.get("surplusnum");//数量
					BigDecimal hy_wsxyk_count=new BigDecimal(0);//未实现盈亏
					BigDecimal syl1= (pricetb).subtract(price);
					BigDecimal syl2= (pricetb).multiply(levers);
					BigDecimal syl= syl1.divide(syl2,10, BigDecimal.ROUND_HALF_UP);//收益率
					BigDecimal sy=bondprice.multiply(syl).setScale(10, BigDecimal.ROUND_HALF_UP);
					Date dt2 = (Date) zzhye.get("jgtime");
					if (dt1.getTime() == dt2.getTime()) {
						//先判断是否爆仓 更新收益 更新订单爆仓金额等
						Map cqwe =SFbcqwe(zzhye);
						if (null!=cqwe){
							/*Map zzhye1=new HashMap();
							zzhye1.put("hy_order_id",Integer.valueOf(cqwe));
							zzhye1 =findById_sql("HyorderMapper.findById",zzhye1);*/
							bcuser.add(cqwe);
						}else{
							//没有爆仓正常算收益 更新订单收益等 判断是否是收益的如果是存入集合 再平分爆仓的金额
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","4");//交割
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",zzhye.get("hy_order_id"));
							wsxsy1.put("pctime",longzong.format(new Date()));
							wsxsy1.put("wsxsyprice",0.00);
							wsxsy1.put("cjprice",pricetb);
							edit_sql("HyorderMapper.edit",wsxsy1);
							/*Map zzhye1=new HashMap();
							zzhye1.put("hy_order_id",Integer.valueOf(zzhye.get("hy_order_id")+""));
							zzhye1 =findById_sql("HyorderMapper.findById",zzhye1);*/
							if (sy.doubleValue()>0){
								zzhye.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								zzhye.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								zzhye.put("yktype","3");
							}
							zzhye.put("hangtype","4");//交割
							zzhye.put("profitprice",sy);
							zzhye.put("hy_order_id",zzhye.get("hy_order_id"));
							zzhye.put("pctime",longzong.format(new Date()));
							zzhye.put("wsxsyprice",0.00);
							zcuser.add(zzhye);
						}
						//最后更新账户 收益=订单收益+支出爆仓
						//extendsService. updateOrder(zzhye);
					}

				}
				//判断又没有爆仓订单 没有更新用户信息
				if (null!=bcuser&&bcuser.size()>0){
					//总爆仓金额
					BigDecimal zongexploprice = new BigDecimal(0.00);
					for (int i = 0; i <bcuser.size() ; i++) {
						Map zzhye = listorder.get(i);
						zongexploprice =zongexploprice.add(new BigDecimal(zzhye.get("exploprice") + "")) ;
					}
					//爆仓的订单
					for (int i = 0; i <bcuser.size() ; i++) {
						Map zzhye=listorder.get(i);
						String type=zzhye.get("type")+"";
						String usertype=zzhye.get("usertype")+"";
						String datetype=zzhye.get("datetype")+"";
						List<Map> usli=new ArrayList<>();//盈利相同
						BigDecimal exploprice=new BigDecimal(zzhye.get("exploprice")+"");
						int num=0;//相同数量
						for (int j = 0; j < zcuser.size(); j++) {
							Map zchye=listorder.get(i);
							String zctype=zchye.get("type")+"";
							String zcusertype=zchye.get("usertype")+"";
							String zcdatetype=zchye.get("datetype")+"";

							String hangtype=zchye.get("hangtype")+"";
							String yktype=zchye.get("yktype")+"";
							//循环查找与爆仓相同盈利的订单 type usertype hangtype(4) datetype yktype(1)  //交割 盈利的
							if (type.equals(zctype)&&usertype.equals(zcusertype)&&datetype.equals(zcdatetype)&&"4".equals(hangtype)&&"1".equals(yktype)){
								usli.add(zchye);//将盈利相同的存list\
								num++;
							}else{
								fexuser.add(zchye);
							}
						}
						if (num==0){
							Map moneylog=new HashMap();
							moneylog.put("money",exploprice);
							moneylog.put("balance",0.00);
							moneylog.put("content","爆仓剩余");
							moneylog.put("type","6");//1购买 2平仓3交割
							moneylog.put("paytype",zzhye.get("type"));//1btc 2eth 3eos
							moneylog.put("createtime",longzong.format(new Date()));
							moneylog.put("flag",null);
							moneylog.put("hy_order_id",zzhye.get("hy_order_id"));
							moneylog.put("userid",zzhye.get("userid"));
							save_sql("HymoneylogMapper.save", moneylog);
						}else if (num>0){
							//将爆仓超出部分平分
							BigDecimal pf=exploprice.divide(new BigDecimal(num+""));
							BigDecimal zhpf=new BigDecimal(0.00);//是否有剩余
							int as=num;
							for (int j = 0; j <usli.size() ; j++) {
								Map zchye=usli.get(i);
								BigDecimal profitprice=(BigDecimal)zchye.get("profitprice");
								BigDecimal profitprice11=(profitprice).subtract(pf);
								if (as>1){
									if (profitprice11.doubleValue()<0){
										Map zzhye1=new HashMap();
										zzhye1.put("hy_order_id",Integer.valueOf(zchye.get("hy_order_id")+""));
										zzhye1.put("profitprice",0.00);
										zzhye1.put("wsxsyprice",0.00);
										zzhye1.put("explosionpayprice",profitprice+"");
										zzhye1.put("explosionpayrate",new BigDecimal(100).multiply((profitprice.divide(zongexploprice)))+"");
										edit_sql("HyorderMapper.edit",zzhye1);
										Map moneylog=new HashMap();
										moneylog.put("exuserid",zzhye.get("userid"));
										moneylog.put("userid",zchye.get("userid"));
										moneylog.put("money",profitprice);

										save_sql("TestMapper.saveu", moneylog);
										as--;
										exploprice=exploprice.subtract(profitprice);
										pf=new BigDecimal((exploprice.doubleValue()/Double.valueOf(as)));

										zchye.put("hy_order_id",Integer.valueOf(zchye.get("hy_order_id")+""));
										zchye.put("profitprice",0.00);
										zchye.put("wsxsyprice",0.00);
										zchye.put("explosionpayprice",profitprice+"");
										zchye.put("explosionpayrate",new BigDecimal(100).multiply((profitprice.divide(zongexploprice)))+"");
										fexuser.add(zchye);
									}else{
										Map zzhye1=new HashMap();
										zzhye1.put("hy_order_id",Integer.valueOf(zchye.get("hy_order_id")+""));
										zzhye1.put("profitprice",profitprice11);
										zzhye1.put("wsxsyprice",0.00);
										zzhye1.put("explosionpayprice",pf+"");
										zzhye1.put("explosionpayrate",new BigDecimal(100).multiply((pf.divide(zongexploprice)))+"");
										edit_sql("HyorderMapper.edit",zzhye1);
										Map moneylog=new HashMap();
										moneylog.put("exuserid",zzhye.get("userid"));
										moneylog.put("userid",zchye.get("userid"));
										moneylog.put("money",pf);
										save_sql("TestMapper.saveu", moneylog);

										zchye.put("hy_order_id",Integer.valueOf(zchye.get("hy_order_id")+""));
										zchye.put("profitprice",profitprice11);
										zchye.put("wsxsyprice",0.00);
										zchye.put("explosionpayprice",pf+"");
										zchye.put("explosionpayrate",new BigDecimal(100).multiply((pf.divide(zongexploprice)))+"");
										fexuser.add(zchye);
									}
								}else{
									//减去最后那个人
									if (profitprice11.doubleValue()<0){
										Map zzhye1=new HashMap();
										zzhye1.put("hy_order_id",Integer.valueOf(zchye.get("hy_order_id")+""));
										zzhye1.put("profitprice",0.00);
										zzhye1.put("wsxsyprice",0.00);
										zzhye1.put("explosionpayprice",profitprice);
										zzhye1.put("explosionpayrate",new BigDecimal(100).multiply((profitprice.divide(zongexploprice)))+"");
										edit_sql("HyorderMapper.edit",zzhye1);
										Map moneylog=new HashMap();
										moneylog.put("exuserid",zzhye.get("userid"));
										moneylog.put("userid",zchye.get("userid"));
										moneylog.put("money",profitprice);
										save_sql("TestMapper.saveu", moneylog);
										pf=pf.subtract(profitprice);

										zchye.put("hy_order_id",Integer.valueOf(zchye.get("hy_order_id")+""));
										zchye.put("profitprice",0.00);
										zchye.put("wsxsyprice",0.00);
										zchye.put("explosionpayprice",profitprice);
										zchye.put("explosionpayrate",new BigDecimal(100).multiply((profitprice.divide(zongexploprice)))+"");
										fexuser.add(zchye);
									}else{
										Map zzhye1=new HashMap();
										zzhye1.put("hy_order_id",Integer.valueOf(zchye.get("hy_order_id")+""));
										zzhye1.put("profitprice",profitprice11);
										zzhye1.put("wsxsyprice",0.00);
										zzhye1.put("explosionpayprice",pf+"");
										zzhye1.put("explosionpayrate",new BigDecimal(100).multiply((pf.divide(zongexploprice)))+"");
										edit_sql("HyorderMapper.edit",zzhye1);
										Map moneylog=new HashMap();
										moneylog.put("exuserid",zzhye.get("userid"));
										moneylog.put("userid",zchye.get("userid"));
										moneylog.put("money",pf);
										save_sql("TestMapper.saveu", moneylog);

										zchye.put("hy_order_id",Integer.valueOf(zchye.get("hy_order_id")+""));
										zchye.put("profitprice",profitprice11);
										zchye.put("wsxsyprice",0.00);
										zchye.put("explosionpayprice",pf+"");
										zchye.put("explosionpayrate",new BigDecimal(100).multiply((pf.divide(zongexploprice)))+"");
										fexuser.add(zchye);
									}
								}

							}
							if (pf.doubleValue()>0){
								Map moneylog=new HashMap();
								moneylog.put("money",pf);
								moneylog.put("balance",0.00);
								moneylog.put("content","爆仓剩余");
								moneylog.put("type","6");//1购买 2平仓3交割
								moneylog.put("paytype",zzhye.get("type"));//1btc 2eth 3eos
								moneylog.put("createtime",longzong.format(new Date()));
								moneylog.put("flag",null);
								moneylog.put("hy_order_id",zzhye.get("hy_order_id"));
								moneylog.put("userid",zzhye.get("userid"));
								save_sql("HymoneylogMapper.save", moneylog);
							}
						}
					}
					for (int j = 0; j < fexuser.size(); j++) {
						Map zchye=listorder.get(j);
						selling(String.valueOf(zchye.get("hy_order_id")),String.valueOf(zchye.get("userid")),"1",zchye);
					}
				}else{
					for (int j = 0; j < zcuser.size(); j++) {
						Map zchye=listorder.get(j);
						selling(String.valueOf(zchye.get("hy_order_id")),String.valueOf(zchye.get("userid")),"1",zchye);
					}
				}

			}
			//没有爆仓正常算收益 更新订单收益等 判断是否是收益的如果是存入集合 再平分爆仓的金额
			//最后更新账户 收益=订单收益+支出爆仓

		}catch (Exception e){

		}

		return q;
	}
	//爆仓更新账户
	public void selling(String hy_order_id,String userid,String type,Map order){
			//先查出订单
			Map zzhye=new HashMap();
			if ("1".equals(type)){
				zzhye=order;
			}else{
				zzhye.put("hy_order_id",Integer.valueOf(hy_order_id));
				zzhye =findById_sql("HyorderMapper.findById",zzhye);
			}
			zzhye.put("jgtime",longzong.format(new Date()));
			BigDecimal bondprice=(BigDecimal)zzhye.get("bondprice");//保证金
			BigDecimal sy=(BigDecimal) zzhye.get("profitprice");
				//type 1 BTC 2 ETH 3 EOS
				//buytype 1买入开多看涨2卖出开空看跌
				//datetype 1当周2次周3季度
				//1 BTC 2 ETH 3 EOS
				Map maprek=new HashMap();
				BigDecimal dsbzje=new BigDecimal(0);
				if ("1".equals(zzhye.get("type")+"")){
					//1买入开多看涨2卖出开空看跌
					if ("1".equals(zzhye.get("buytype")+"")){
						//1当周2次周3季度
						if ("1".equals(zzhye.get("datetype")+"")){
							//btc开多当周

							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HybtcuserMapper.findById",maprek);
							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_dzduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_dzduo_count")&&!"".equals(maprek.get("hy_dzduo_count")+"")){
								hy_dzduo_count=new BigDecimal(maprek.get("hy_dzduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);

						}
						if ("2".equals(zzhye.get("datetype")+"")){
							//btc开多次周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HybtcuserMapper.findById",maprek);
							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_czduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_czduo_count")&&!"".equals(maprek.get("hy_czduo_count")+"")){
								hy_czduo_count=new BigDecimal(maprek.get("hy_czduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("3".equals(zzhye.get("datetype")+"")){
							//btc开多季度
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HybtcuserMapper.findById",maprek);
							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_jdduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_jdduo_count")&&!"".equals(maprek.get("hy_jdduo_count")+"")){
								hy_jdduo_count=new BigDecimal(maprek.get("hy_jdduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
					}
					if ("2".equals(zzhye.get("buytype")+"")){
						//btc开空
						//1当周2次周3季度
						if ("1".equals(zzhye.get("datetype")+"")){
							//btc开空当周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HybtcuserMapper.findById",maprek);
							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_dzkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_dzkong_count")&&!"".equals(maprek.get("hy_dzkong_count")+"")){
								hy_dzkong_count=new BigDecimal(maprek.get("hy_dzkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("2".equals(zzhye.get("datetype")+"")){
							//btc开空次周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HybtcuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_czkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_czkong_count")&&!"".equals(maprek.get("hy_czkong_count")+"")){
								hy_czkong_count=new BigDecimal(maprek.get("hy_czkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("3".equals(zzhye.get("datetype")+"")){
							//btc开空季度
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HybtcuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_jdkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_jdkong_count")&&!"".equals(maprek.get("hy_jdkong_count")+"")){
								hy_jdkong_count=new BigDecimal(maprek.get("hy_jdkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
					}
				}
				if ("2".equals(zzhye.get("type")+"")){
					//1买入开多看涨2卖出开空看跌
					if ("1".equals(zzhye.get("buytype")+"")){
						//1当周2次周3季度
						if ("1".equals(zzhye.get("datetype")+"")){
							//eth开多当周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyethuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_dzduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_dzduo_count")&&!"".equals(maprek.get("hy_dzduo_count")+"")){
								hy_dzduo_count=new BigDecimal(maprek.get("hy_dzduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("2".equals(zzhye.get("datetype")+"")){
							//eth开多次周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyethuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_czduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_czduo_count")&&!"".equals(maprek.get("hy_czduo_count")+"")){
								hy_czduo_count=new BigDecimal(maprek.get("hy_czduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("3".equals(zzhye.get("datetype")+"")){
							//eth开多季度
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyethuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_jdduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_jdduo_count")&&!"".equals(maprek.get("hy_jdduo_count")+"")){
								hy_jdduo_count=new BigDecimal(maprek.get("hy_jdduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
					}
					if ("2".equals(zzhye.get("buytype")+"")){
						//eth开空
						//1当周2次周3季度
						if ("1".equals(zzhye.get("datetype")+"")){
							//eth开空当周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyethuserMapper.findById",maprek);
							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_dzkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_dzkong_count")&&!"".equals(maprek.get("hy_dzkong_count")+"")){
								hy_dzkong_count=new BigDecimal(maprek.get("hy_dzkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("2".equals(zzhye.get("datetype")+"")){
							//eth开空次周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyethuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_czkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_czkong_count")&&!"".equals(maprek.get("hy_czkong_count")+"")){
								hy_czkong_count=new BigDecimal(maprek.get("hy_czkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("3".equals(zzhye.get("datetype")+"")){
							//eth开空季度
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyethuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_jdkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_jdkong_count")&&!"".equals(maprek.get("hy_jdkong_count")+"")){
								hy_jdkong_count=new BigDecimal(maprek.get("hy_jdkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
					}
				}
				if ("3".equals(zzhye.get("type")+"")){
					//1买入开多看涨2卖出开空看跌
					if ("1".equals(zzhye.get("buytype")+"")){
						//1当周2次周3季度
						if ("1".equals(zzhye.get("datetype")+"")){
							//eos开多当周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyeosuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_dzduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_dzduo_count")&&!"".equals(maprek.get("hy_dzduo_count")+"")){
								hy_dzduo_count=new BigDecimal(maprek.get("hy_dzduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("2".equals(zzhye.get("datetype")+"")){
							//eos开多次周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyeosuserMapper.findById",maprek);
							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_czduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_czduo_count")&&!"".equals(maprek.get("hy_czduo_count")+"")){
								hy_czduo_count=new BigDecimal(maprek.get("hy_czduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("3".equals(zzhye.get("datetype")+"")){
							//eos开多季度
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyeosuserMapper.findById",maprek);
							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_jdduo_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_jdduo_count")&&!"".equals(maprek.get("hy_jdduo_count")+"")){
								hy_jdduo_count=new BigDecimal(maprek.get("hy_jdduo_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
					}
					if ("2".equals(zzhye.get("buytype")+"")){
						//eos开空
						//1当周2次周3季度
						if ("1".equals(zzhye.get("datetype")+"")){
							//eos开空当周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyeosuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_dzkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_dzkong_count")&&!"".equals(maprek.get("hy_dzkong_count")+"")){
								hy_dzkong_count=new BigDecimal(maprek.get("hy_dzkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("2".equals(zzhye.get("datetype")+"")){
							//eos开空次周
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyeosuserMapper.findById",maprek);

							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_czkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_czkong_count")&&!"".equals(maprek.get("hy_czkong_count")+"")){
								hy_czkong_count=new BigDecimal(maprek.get("hy_czkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
						if ("3".equals(zzhye.get("datetype")+"")){
							//eos开空季度
							maprek=new HashMap();
							maprek.put("userid",Integer.valueOf(userid));
							maprek =findById_sql("HyeosuserMapper.findById",maprek);
							//未实现=之前-收益 已实现=之前+收益 可用保证金=保证金+收益 已用保证金=之前-保证金
							BigDecimal hy_jdkong_count=new BigDecimal(0);//已经有的未实现
							BigDecimal hy_ysxyk_count=new BigDecimal(0);//已经有的已实现
							BigDecimal hy_kybzj_count=new BigDecimal(0);//已经有的可用保证金
							BigDecimal hy_yybzj_count=new BigDecimal(0);//已经有的已用保证金
							if (null!=maprek && null!=maprek.get("hy_jdkong_count")&&!"".equals(maprek.get("hy_jdkong_count")+"")){
								hy_jdkong_count=new BigDecimal(maprek.get("hy_jdkong_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_kybzj_count")&&!"".equals(maprek.get("hy_kybzj_count")+"")){
								hy_kybzj_count=new BigDecimal(maprek.get("hy_kybzj_count")+"");
								dsbzje=new BigDecimal(maprek.get("hy_kybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_yybzj_count")&&!"".equals(maprek.get("hy_yybzj_count")+"")){
								hy_yybzj_count=new BigDecimal(maprek.get("hy_yybzj_count")+"");
							}
							if (null!=maprek && null!=maprek.get("hy_ysxyk_count")&&!"".equals(maprek.get("hy_ysxyk_count")+"")){
								hy_ysxyk_count=new BigDecimal(maprek.get("hy_ysxyk_count")+"");
							}
							//hy_dzduo_count=(hy_dzduo_count.doubleValue()-sy.doubleValue())==0?new BigDecimal(0.00):hy_dzduo_count.subtract(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_ysxyk_count=hy_ysxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_kybzj_count=hy_kybzj_count.add(sy.add(bondprice)).setScale(10, BigDecimal.ROUND_HALF_UP);
							hy_yybzj_count=(hy_yybzj_count.doubleValue()-bondprice.doubleValue())<0?new BigDecimal(0.00):hy_yybzj_count.subtract(bondprice).setScale(10, BigDecimal.ROUND_HALF_UP);
							maprek.put("hy_ysxyk_count",hy_ysxyk_count);
							maprek.put("hy_kybzj_count",hy_kybzj_count);
							maprek.put("hy_yybzj_count",hy_yybzj_count);
							maprek.put("hy_djbzj_count",hy_yybzj_count);
						}
					}
				}

				if ("1".equals(zzhye.get("type")+"")){
					edit_sql("HybtcuserMapper.edit", maprek);
				}
				if ("2".equals(zzhye.get("type")+"")){
					edit_sql("HyethuserMapper.edit", maprek);
				}
				if ("3".equals(zzhye.get("type")+"")){
					edit_sql("HyeosuserMapper.edit", maprek);
				}
				Map moneylog=new HashMap();
				moneylog.put("money",zzhye.get("bondprice"));
				moneylog.put("balance",dsbzje);
				moneylog.put("content","爆仓");
				moneylog.put("type","5");//1购买 2平仓3交割
				moneylog.put("paytype",zzhye.get("type"));//1btc 2eth 3eos
				moneylog.put("createtime",longzong.format(new Date()));
				moneylog.put("flag",null);
				moneylog.put("hy_order_id",zzhye.get("hy_order_id"));
				moneylog.put("userid",zzhye.get("userid"));
				save_sql("HymoneylogMapper.save", moneylog);
	}
	//交割时爆仓强制平仓
	public Map SFbcqwe(Map order){
		int q=0;
		//判断是逐仓还是全仓
		Map hyuser= new HashMap();
		hyuser.put("userid",order.get("userid"));
		hyuser=findById_sql("HyuserMapper.findById",hyuser);

		BigDecimal price=(BigDecimal)order.get("price");//开仓价格
		BigDecimal levers=new BigDecimal(order.get("levers")+"");//杠杆倍数
		BigDecimal bondprice=(BigDecimal)order.get("bondprice");//保证金
		BigDecimal surplusnum=(BigDecimal)order.get("surplusnum");//数量
		BigDecimal hy_wsxyk_count=new BigDecimal(0);//未实现盈亏
		BigDecimal syl1= (new BigDecimal(""+order.get("pricetb")).subtract(price));
		BigDecimal syl2= (new BigDecimal(""+order.get("pricetb")).multiply(levers));
		BigDecimal syl= syl1.divide(syl2,10, BigDecimal.ROUND_HALF_UP);//收益率
		BigDecimal sy=bondprice.multiply(syl).setScale(10, BigDecimal.ROUND_HALF_UP);
		hy_wsxyk_count.add(sy).setScale(10, BigDecimal.ROUND_HALF_UP);
		//更新订单的未实现盈亏
		Map wsxsy= new HashMap();
		wsxsy.put("hy_order_id",order.get("hy_order_id"));
		wsxsy.put("wsxsyprice",hy_wsxyk_count+"");

		order.put("wsxsyprice",hy_wsxyk_count+"");

		int dx=edit_sql("HyorderMapper.edit",wsxsy);
		if (dx>0){
			//计算是否爆仓 1逐仓2全仓
			if (null!=hyuser&&"1".equals(hyuser.get("typezh")+"")){
				//逐仓保证金率=（固定保证金+未实现盈亏）*开仓均价*杠杆／（合约面值*持仓数量）
				//btc1张为100美金，其余币种为10美金
				//（固定保证金+未实现盈亏）
				BigDecimal gdwsx= bondprice.add(hy_wsxyk_count);
				//（合约面值*持仓数量）
				BigDecimal hycc=new BigDecimal(0.00);
				//1 BTC 2 ETH 3 EOS
				if ("1".equals(order.get("type")+"")){
					hycc=new BigDecimal(100.00).multiply(surplusnum);
				}
				if ("2".equals(order.get("type")+"")){
					hycc=new BigDecimal(10.00).multiply(surplusnum);
				}
				if ("3".equals(order.get("type")+"")){
					hycc=new BigDecimal(10.00).multiply(surplusnum);
				}
				//保证金率
				BigDecimal bzjl=gdwsx.multiply(price);
				bzjl=bzjl.multiply(levers);
				bzjl=bzjl.divide(hycc);
				bzjl=bzjl.multiply(new BigDecimal(100));
				//当保证金率小于等于杠杆，才会触发爆仓线
				if (bzjl.doubleValue()<=levers.doubleValue()){
					//爆仓超出多少
					if (sy.doubleValue()<0){
						double bcccprice=bondprice.subtract(sy.abs()).doubleValue();
						if (bcccprice<0){
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","5");//爆仓
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",order.get("hy_order_id"));
							wsxsy1.put("exploprice",sy.abs().subtract(bondprice));
							wsxsy1.put("explopriceqs",sy.abs().subtract(bondprice));
							wsxsy1.put("pctime",longzong.format(new Date()));
							wsxsy1.put("sussprice",order.get("pricetb")+"");
							edit_sql("HyorderMapper.edit",wsxsy1);


							if (sy.doubleValue()>0){
								order.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								order.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								order.put("yktype","3");
							}
							order.put("hangtype","5");//爆仓
							order.put("profitprice",sy);
							order.put("hy_order_id",order.get("hy_order_id"));
							order.put("exploprice",sy.abs().subtract(bondprice));
							order.put("explopriceqs",sy.abs().subtract(bondprice));
							order.put("pctime",longzong.format(new Date()));

						}
					}else if (sy.doubleValue()>0){
						double bcccprice=bondprice.subtract(sy).doubleValue();
						if (bcccprice<0){
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","5");//爆仓
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",order.get("hy_order_id"));
							wsxsy1.put("exploprice",sy.subtract(bondprice));
							wsxsy1.put("explopriceqs",sy.subtract(bondprice));
							wsxsy1.put("sussprice",order.get("pricetb")+"");
							wsxsy1.put("pctime",longzong.format(new Date()));
							edit_sql("HyorderMapper.edit",wsxsy1);

							if (sy.doubleValue()>0){
								order.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								order.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								order.put("yktype","3");
							}
							order.put("hangtype","5");//爆仓
							order.put("profitprice",sy);
							order.put("hy_order_id",order.get("hy_order_id"));
							order.put("exploprice",sy.subtract(bondprice));
							order.put("explopriceqs",sy.subtract(bondprice));
							order.put("pctime",longzong.format(new Date()));

						}
					}

					//强制平仓
					selling(String.valueOf(order.get("hy_order_id")),String.valueOf(order.get("userid")),"1",order);
					//清空订单未实现盈利
					Map wsxsy1= new HashMap();
					wsxsy1.put("hy_order_id",order.get("hy_order_id"));
					wsxsy1.put("wsxsyprice","0.00");
					edit_sql("HyorderMapper.edit",wsxsy1);
					return order;
				}

			}
			if (null!=hyuser&&"2".equals(hyuser.get("typezh")+"")){
				//全仓保证金率=账户权益／（用户持仓所需的保证金+挂单冻结保证金）
				//btc1张为100美金，其余币种为10美金
				//账户权益
				BigDecimal zhqy= new BigDecimal(0.00);
				//挂单冻结保证金
				BigDecimal gddj=new BigDecimal(0.00);
				//1 BTC 2 ETH 3 EOS
				if ("1".equals(order.get("type")+"")){
					Map hyuserzh= new HashMap();
					hyuserzh.put("userid",order.get("userid"));
					hyuserzh=findById_sql("HybtcuserMapper.findById",hyuserzh);
					zhqy=(BigDecimal)hyuserzh.get("hy_kybzj_count");
					hyuserzh= new HashMap();
					hyuserzh.put("type",order.get("type"));
					hyuserzh.put("datetype",order.get("datetype"));
					hyuserzh.put("usertype",order.get("usertype"));
					hyuserzh.put("hangtype","2");
					hyuserzh.put("pagenum",0);
					hyuserzh.put("pagesize",1);
					List<Map> listmap=listAll_sql("HyorderMapper.listAllsum",hyuserzh);
					if (null!=listmap&&listmap.size()>0){
						gddj=new BigDecimal(listmap.get(0).get("zongbondprice")+"");
					}
				}
				if ("2".equals(order.get("type")+"")){
					Map hyuserzh= new HashMap();
					hyuserzh.put("userid",order.get("userid"));
					hyuserzh=findById_sql("HyethuserMapper.findById",hyuserzh);
					zhqy=(BigDecimal)hyuserzh.get("hy_kybzj_count");
					hyuserzh= new HashMap();
					hyuserzh.put("type",order.get("type"));
					hyuserzh.put("datetype",order.get("datetype"));
					hyuserzh.put("usertype",order.get("usertype"));
					hyuserzh.put("hangtype","2");
					hyuserzh.put("pagenum",0);
					hyuserzh.put("pagesize",1);
					List<Map> listmap=listAll_sql("HyorderMapper.listAllsum",hyuserzh);
					if (null!=listmap&&listmap.size()>0){
						gddj=new BigDecimal(listmap.get(0).get("zongbondprice")+"");
					}
				}
				if ("3".equals(order.get("type")+"")){
					Map hyuserzh= new HashMap();
					hyuserzh.put("userid",order.get("userid"));
					hyuserzh=findById_sql("HyeosuserMapper.findById",hyuserzh);
					zhqy=(BigDecimal)hyuserzh.get("hy_kybzj_count");
					hyuserzh= new HashMap();
					hyuserzh.put("type",order.get("type"));
					hyuserzh.put("datetype",order.get("datetype"));
					hyuserzh.put("usertype",order.get("usertype"));
					hyuserzh.put("hangtype","2");
					hyuserzh.put("pagenum",0);
					hyuserzh.put("pagesize",1);
					List<Map> listmap=listAll_sql("HyorderMapper.listAllsum",hyuserzh);
					if (null!=listmap&&listmap.size()>0){
						gddj=new BigDecimal(listmap.get(0).get("zongbondprice")+"");
					}
				}
				//保证金率 全仓保证金率=账户权益／（用户持仓所需的保证金+挂单冻结保证金）
				BigDecimal bzjl=zhqy.divide((gddj.add(bondprice)));
				bzjl=bzjl.multiply(new BigDecimal(100));
				//当保证金率小于等于杠杆，才会触发爆仓线
				if (bzjl.doubleValue()<=levers.doubleValue()){
					//爆仓超出多少
					if (sy.doubleValue()<0){
						double bcccprice=bondprice.subtract(sy.abs()).doubleValue();
						if (bcccprice<0){
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","5");//爆仓
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",order.get("hy_order_id"));
							wsxsy1.put("exploprice",sy.abs().subtract(bondprice));
							wsxsy1.put("explopriceqs",sy.abs().subtract(bondprice));
							wsxsy1.put("pctime",longzong.format(new Date()));
							wsxsy1.put("sussprice",order.get("pricetb")+"");
							edit_sql("HyorderMapper.edit",wsxsy1);
							if (sy.doubleValue()>0){
								order.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								order.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								order.put("yktype","3");
							}
							order.put("hangtype","5");//爆仓
							order.put("profitprice",sy);
							order.put("hy_order_id",order.get("hy_order_id"));
							order.put("exploprice",sy.abs().subtract(bondprice));
							order.put("explopriceqs",sy.abs().subtract(bondprice));
							order.put("pctime",longzong.format(new Date()));

						}
					}else if (sy.doubleValue()>0){
						double bcccprice=bondprice.subtract(sy).doubleValue();
						if (bcccprice<0){
							Map wsxsy1= new HashMap();
							if (sy.doubleValue()>0){
								wsxsy1.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								wsxsy1.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								wsxsy1.put("yktype","3");
							}
							wsxsy1.put("hangtype","5");//爆仓
							wsxsy1.put("profitprice",sy);
							wsxsy1.put("hy_order_id",order.get("hy_order_id"));
							wsxsy1.put("exploprice",sy.subtract(bondprice));
							wsxsy1.put("explopriceqs",sy.subtract(bondprice));
							wsxsy1.put("pctime",longzong.format(new Date()));
							wsxsy1.put("sussprice",order.get("pricetb")+"");
							edit_sql("HyorderMapper.edit",wsxsy1);

							if (sy.doubleValue()>0){
								order.put("yktype","1");
							}
							if (sy.doubleValue()<0){
								order.put("yktype","2");
							}
							if (sy.doubleValue()==0){
								order.put("yktype","3");
							}
							order.put("hangtype","5");//爆仓
							order.put("profitprice",sy);
							order.put("hy_order_id",order.get("hy_order_id"));
							order.put("exploprice",sy.subtract(bondprice));
							order.put("explopriceqs",sy.subtract(bondprice));
							order.put("pctime",longzong.format(new Date()));

						}
					}
					//强制平仓
					selling(String.valueOf(order.get("hy_order_id")),String.valueOf(order.get("userid")),"1",order);
					//清空订单未实现盈利
					Map wsxsy1= new HashMap();
					wsxsy1.put("hy_order_id",order.get("hy_order_id"));
					wsxsy1.put("wsxsyprice","0.00");
					edit_sql("HyorderMapper.edit",wsxsy1);
					return order;
				}

			}
		}
		return new HashMap();
	}
}
