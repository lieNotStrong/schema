package com.scheduling.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.scheduling.common.util.PageView;
import com.scheduling.dao.BaseDao;
import com.scheduling.service.ifac.BaseService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 胡国旺
 */
@Transactional
@Service("baseService")
@SuppressWarnings({"rawtypes","unchecked"})
public class BaseServiceImpl implements BaseService {
	protected BaseDao dao;
	@Resource
	public void init(SqlSessionFactory sqlSessionFactory) {
		dao = new BaseDao(sqlSessionFactory);
	}
	@Override
	public int save(String mapperName, Map params) {
		// TODO Auto-generated method stub
		return  dao.insertNoT(mapperName+".save", params);
	}
	
	@Override
	public int edit(String mapperName, Map params) {
		// TODO Auto-generated method stub
		return dao.updateNoT(mapperName+".edit", params);
	}

	@Override
	public int delete(String mapperName, Map params) {
		// TODO Auto-generated method stub
		return dao.deleteNoT(mapperName+".delete", params);
	}

	@Override
	public int deleteAll(String mapperName, String ids) {
		// TODO Auto-generated method stub
		return dao.deleteById(mapperName+".deleteAll", ids);
	}
	@Override
	public List<Map> listAll(String mapperName, Map params) {
		// TODO Auto-generated method stub
		return dao.findListNopage(mapperName+".listAll", params);
	}

	@Override
	public PageView datalistPage(String mapperName, Map params, PageView pageView) {
		// TODO Auto-generated method stub
		int count = queryCount(mapperName,params);
		pageView.setRowCount(count);
		pageView.setRecords(dao.findListPageList(mapperName+".datalistPage", params, pageView));
		return pageView;
	}

	@Override
	public int queryCount(String mapperName, Map params) {
		// TODO Auto-generated method stub
		return dao.findListPageListCount(mapperName+".queryCount", params);
	}
	@Override
	public Map findById(String mapperName, Map params) {
		// TODO Auto-generated method stub
		List<Map> lst = dao.findListNopage(mapperName+".listAll",params);
		return lst.size()>0?lst.get(0):null;
	}
	@Override
	public boolean checkName(String mapperName, Map params) {
		// TODO Auto-generated method stub
		List<Map> lst = dao.findListNopage(mapperName+".datalistPage",params);
		return lst.size()>0?false:true;
	}
	@Override
	public int save_sql(String sql, Map params) {
		// TODO Auto-generated method stub
		return dao.insertNoT(sql, params);
	}
	@Override
	public int save_sql(String sql, List params) {
		// TODO Auto-generated method stub
		return dao.insertNoT(sql, params);
	}
	@Override
	public int edit_sql(String sql, Map params) {
		// TODO Auto-generated method stub
		return dao.updateNoT(sql, params);
	}
	@Override
	public int delete_sql(String sql, Map params) {
		// TODO Auto-generated method stub
		return dao.deleteNoT(sql, params);
	}
	@Override
	public int deleteAll_sql(String sql, String ids) {
		// TODO Auto-generated method stub
		return dao.deleteById(sql, ids);
	}
	@Override
	public List<Map> listAll_sql(String sql, Map params) {
		// TODO Auto-generated method stub
		return dao.findListNopage(sql, params);
	}
	@Override
	public PageView datalistPage_sql(String sql, Map params, PageView pageView) {
		// TODO Auto-generated method stub
		int count = dao.findListNopage(sql, params).size();
		pageView.setRowCount(count);
		pageView.setRecords(dao.findListPageList(sql, params, pageView));
		return pageView;
	}
	@Override
	public int queryCount_sql(String sql, Map params) {
		// TODO Auto-generated method stub
		return dao.findListPageListCount(sql, params);
	}
	@Override
	public Map findById_sql(String sql, Map params) {
		// TODO Auto-generated method stub
		List<Map> lst = dao.findListNopage(sql,params);
		return lst.size()>0?lst.get(0):null;
	}
	@Override
	public boolean checkName_sql(String sql, Map params) {
		// TODO Auto-generated method stub
		List<Map> lst = dao.findListNopage(sql,params);
		return lst.size()>0?false:true;
	}

}
