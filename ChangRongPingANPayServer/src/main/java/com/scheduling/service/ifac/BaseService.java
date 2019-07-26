package com.scheduling.service.ifac;

import com.scheduling.common.util.PageView;

import java.util.List;
import java.util.Map;


/**
 * 适合于单表及综合查询，以及单表增删改。
 * @author 胡国旺
 *
 */
@SuppressWarnings("rawtypes")
public interface BaseService{
	
	public int save(String mapperName, Map params);
	
	public int edit(String mapperName, Map params);
	
	public int delete(String mapperName, Map params);
	
	public int deleteAll(String mapperName, String ids);
	
	public List<Map> listAll(String mapperName, Map params);
	
	public PageView datalistPage(String mapperName, Map params, PageView pageView);
	
	public int queryCount(String mapperName, Map params);
	
	public Map findById(String mapperName, Map params);
	
	public boolean checkName(String mapperName, Map params);
	
	
	
	public int save_sql(String sql, Map params);
	public int save_sql(String sql, List params);

	public int edit_sql(String sql, Map params);
	
	public int delete_sql(String sql, Map params);
	
	public int deleteAll_sql(String sql, String ids);
	
	public List<Map> listAll_sql(String sql, Map params);
	
	public PageView datalistPage_sql(String sql, Map params, PageView pageView);
	
	public int queryCount_sql(String sql, Map params);
	
	public Map findById_sql(String sql, Map params);
	
	public boolean checkName_sql(String sql, Map params);
}
