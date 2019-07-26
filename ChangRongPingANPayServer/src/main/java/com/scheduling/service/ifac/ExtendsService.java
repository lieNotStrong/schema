package com.scheduling.service.ifac;

import java.util.Map;


/**
 * 适合于多表增删改事务处理操作
 * @author 胡国旺
 *
 */
@SuppressWarnings("rawtypes")
public interface ExtendsService extends BaseService{
	//点击平仓
	public int updateOrder(Map order, Map express);
	public int delorder(Map order, String userid);
	//创建合约账户
	public int adduhyusee(Map hyuser,Map btcuser,Map ethuser,Map eosuser );
	public int adduhyuseefw(Map hyuser,Map btcuser,Map ethuser,Map eosuser );
	public int adduhyuseec2c(Map hyuser,Map btcuser,Map ethuser,Map eosuser );
	public int addbzj(Map maouy,Map zuser,Map user,Map maouylog);
//判断是否爆仓
	public int SFbc(Map order);
	//交割
	public int jgcc(Map order);
	public Map SFbcqwe(Map order);

}
