package com.scheduling.dao;

import com.scheduling.common.util.Page;
import com.scheduling.common.util.PageView;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * mybatis通用dao
 * @param <T> vo
 * @param <PK> 主键类型
 *
 * @author 胡国旺
 * @history
 * <TABLE id="HistoryTable" border="1">
 * 	<TR><TD>时间</TD><TD>描述</TD><TD>作者</TD></TR>
 *	<TR><TD>2015-6-26</TD><TD>创建初始版本</TD><TD>胡国旺</TD></TR>
 * </TABLE>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class
BaseDao<T, PK extends Serializable> extends SqlSessionDaoSupport{
	public static final String POSTFIX_INSERT = ".insert";
	public static final String POSTFIX_UPDATE = ".update";
	public static final String POSTFIX_UPDATEBYMAP = ".updateByMap";
	public static final String POSTFIX_DELETEBYID = ".deleteById";
	public static final String POSTFIX_DELETEBYIDS = ".deleteByIds";
	public static final String POSTFIX_DELETEBYMAP = ".deleteByMap";
	public static final String POSTFIX_SELECTBYID = ".selectById";
	public static final String POSTFIX_COUNTBYID = ".countById";
	public static final String POSTFIX_SELECTBYPROPERTY = ".selectByProperty";
	public static final String POSTFIX_SELECTALLLIST = ".selectAllList";
	public static final String POSTFIX_SELECTLISTBYPROPERTY = ".selectListByProperty";
	public static final String POSTFIX_SELECTBYMAP = ".selectByMap";
	public static final String POSTFIX_SELECTLISTBYMAP = ".selectListByMap";
	public static final String POSTFIX_COUNTBYPROPERTY = ".countByProperty";
	public static final String POSTFIX_COUNTBYMAP = ".countByMap";
	public static final String POSTFIX_QUERYPAGE = ".queryPage";
	public static final String POSTFIX_QUERYPAGECOUNT = ".queryPageCount";

	protected Class<T> entityClass;

	protected String clazzName;

	protected T t;

	public BaseDao() {
		// 通过范型反射，取得在子类中定义的class.
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		clazzName = entityClass.getSimpleName();
	}
	
	public BaseDao(final SqlSessionFactory sqlSessionFactory, final Class<T> entityClass) {
		// 通过范型反射，取得在子类中定义的class.
		this.entityClass = entityClass;
		clazzName = entityClass.getSimpleName();
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	public BaseDao(final SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	/**
	 * 新增对象  (insert)
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Serializable insert(T entity) {
		return (Serializable) getSqlSession().insert(this.clazzName	+ POSTFIX_INSERT, entity);
	}
	
	/**
	 * 新增对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Serializable insert(String sqlStatement, T entity) {
		return (Serializable) getSqlSession().insert(this.clazzName	+ "." + sqlStatement, entity);
	}
	
	/**
	 * 新增对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @return
	 * @throws Exception
	 */
	public Serializable insertObject(String sqlStatement, Object obj) {
		return (Serializable) getSqlSession().insert(this.clazzName	+ "." + sqlStatement, obj);
	}
	
	/**
	 * 更新对象  (update)
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T update(T entity) {
		getSqlSession().update(this.clazzName + POSTFIX_UPDATE, entity);
		return entity;
	}
	
	/**
	 * 更新对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T update(String sqlStatement, T entity) {
		getSqlSession().update(this.clazzName +  "." + sqlStatement, entity);
		return entity;
	}
	
	/**
	 * 更新对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @return
	 * @throws Exception
	 */
	public Object updateObject(String sqlStatement, Object obj) {
		getSqlSession().update(this.clazzName +  "." + sqlStatement, obj);
		return obj;
	}

	/**
	 * 更新对象的部分属性  (updateByMap)
	 * @param id
	 * @param properties
	 * @param propertyValues
	 * @return
	 * @throws Exception
	 */
	public int update(PK id, String[] properties, Object[] propertyValues) {
		// 更新数据库
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < properties.length; i++) {
			map.put(properties[i], propertyValues[i]);
		}
		map.put("id", id);
		return getSqlSession().update(this.clazzName + POSTFIX_UPDATEBYMAP, map);
	}
	
	/**
	 * 更新对象的部分属性
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param id
	 * @param properties
	 * @param propertyValues
	 * @return
	 * @throws Exception
	 */
	public int update(String sqlStatement, PK id, String[] properties, Object[] propertyValues) {
		// 更新数据库
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < properties.length; i++) {
			map.put(properties[i], propertyValues[i]);
		}
		map.put("id", id);
		return getSqlSession().update(this.clazzName + "." + sqlStatement, map);
	}

	/**
	 * 根据ID删除对象  (deleteById)
	 * @param id
	 * @throws Exception
	 */
	public void deleteById(PK id) {
		getSqlSession().delete(this.clazzName + POSTFIX_DELETEBYID,	id);
	}
	
	/**
	 * 根据ID删除对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param id
	 * @throws Exception
	 */
	public int deleteById(String sqlStatement, PK id) {
		return getSqlSession().delete(this.clazzName + "." + sqlStatement,	id);
	}
	
	/**
	 * 根据ID删除对象  (deleteByIds)
	 * @param ids
	 * @throws Exception
	 */
	public void deleteByIds(List<PK> ids) {
		getSqlSession().delete(this.clazzName + POSTFIX_DELETEBYIDS, ids);
	}
	
	/**
	 * 根据ID删除对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param ids
	 * @throws Exception
	 */
	public void deleteByIds(String sqlStatement, List<PK> ids) {
		getSqlSession().delete(this.clazzName + "." + sqlStatement, ids);
	}
	
	/**
	 * 根据ID删除对象  (deleteByIds)
	 * @param ids
	 * @throws Exception
	 */
	public void deleteByIds(PK[] ids) {
		getSqlSession().delete(this.clazzName + POSTFIX_DELETEBYIDS, ids);
	}
	
	/**
	 * 根据ID删除对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param ids
	 * @throws Exception
	 */
	public void deleteByIds(String sqlStatement, PK[] ids) {
		getSqlSession().delete(this.clazzName + "." + sqlStatement, ids);
	}

	/**
	 * 根据条件删除对象  (deleteByMap)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int deleteByMap(Map<String, Object> map) {
		return getSqlSession().delete(this.clazzName	+ POSTFIX_DELETEBYMAP, map);
	}
	
	/**
	 * 根据条件删除对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int deleteByMap(String sqlStatement, Map<String, Object> map) {
		return getSqlSession().delete(this.clazzName	+ "." + sqlStatement, map);
	}

	/**
	 * 通过id得到实体对象  (selectById)
	 * @param id
	 * @return
	 */
	public T findById(PK id) {
		return (T) getSqlSession().selectOne(this.clazzName + POSTFIX_SELECTBYID, id);
	}
	
	/**
	 * 通过id得到实体对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param id
	 * @return
	 */
	public T findById(String sqlStatement, PK id) {
		return (T) getSqlSession().selectOne(this.clazzName + "." + sqlStatement, id);
	}
	
	/**
	 * 通过某个property属性得到单一实体对象  (selectByProperty)
	 * @param property 属性参数
	 * @return
	 */
	public T findByProperty(Object property) {
		return (T) getSqlSession().selectOne(this.clazzName + POSTFIX_SELECTBYPROPERTY, property);
	}
	
	/**
	 * 通过某个property属性得到单一实体对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param property
	 * @return
	 */
	public T findByProperty(String sqlStatement, Object property) {
		try {
			return (T) getSqlSession().selectOne(this.clazzName + "." + sqlStatement, property);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	/**
	 * 通过实体属性得到单一实体对象
	 * @param sqlStatement
	 * @return
	 */
	public T findByT(String sqlStatement, T t) {
		return (T) getSqlSession().selectOne(this.clazzName + "." + sqlStatement, t);
	}
	
	/**
	 * 通过某个property属性得到实体对象List  (selectListByProperty)
	 * @param property 属性参数
	 * @return
	 */
	public List<T> findListByProperty(Object property) {
		return (List<T>) getSqlSession().selectList(this.clazzName + POSTFIX_SELECTLISTBYPROPERTY, property);
	}
	
	/**
	 * 通过某个property属性得到实体对象List  (selectListByProperty)
	 * @return
	 */
	public List<T> findAllList() {
		return (List<T>) getSqlSession().selectList(this.clazzName + POSTFIX_SELECTALLLIST);
	}
	
	/**
	 * 通过某个property属性得到实体对象List
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param property
	 * @return
	 */
	public List<T> findListByProperty(String sqlStatement, Object property) {
		return (List<T>) getSqlSession().selectList(this.clazzName + "." + sqlStatement, property);
	}
	
	/**
	 * 通过map得到实体对象  (selectByMap)
	 * @param map 参数
	 * @return
	 */
	public T findByMap(Map map) {
		if(map == null){
			return (T) getSqlSession().selectOne(this.clazzName + POSTFIX_SELECTBYMAP);
		} else {
			return (T) getSqlSession().selectOne(this.clazzName + POSTFIX_SELECTBYMAP, map);
		}
	}
	
	/**
	 * 通过map得到实体对象
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param map
	 * @return
	 */
	public T findByMap(String sqlStatement, Map map) {
		if(map == null){
			return (T) getSqlSession().selectOne(this.clazzName + "." + sqlStatement);
		} else {
			return (T) getSqlSession().selectOne(this.clazzName + "." + sqlStatement, map);
		}
	}
	
	/**
	 * 通过map得到实体对象List  (selectListByMap)
	 * @param map 参数
	 * @return
	 */
	public List<T> findListByMap(Map map) {
		if(map == null){
			return (List<T>) getSqlSession().selectList(this.clazzName + POSTFIX_SELECTLISTBYMAP);
		} else {
			return (List<T>) getSqlSession().selectList(this.clazzName + POSTFIX_SELECTLISTBYMAP, map);
		}
	}
	
	/**
	 * 通过map得到实体对象List
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param map
	 * @return
	 */
	public List<T> findListByMap(String sqlStatement, Map map) {
		if(map == null){
			return (List<T>) getSqlSession().selectList(this.clazzName + "." + sqlStatement);
		} else {
			return (List<T>) getSqlSession().selectList(this.clazzName + "." + sqlStatement, map);
		}
	}
	
	/**
	 * 根据id得到记录数  (countById)
	 * @param id 
	 * @return
	 */
	public Integer countById(PK  id) {
		return getSqlSession().selectOne(this.clazzName + POSTFIX_COUNTBYID, id);
	}
	
	/**
	 * 根据id得到记录数  (countById)
	 * @param id 
	 * @return
	 */
	public Integer countById(String sqlStatement, PK  id) {
		return getSqlSession().selectOne(this.clazzName + "." + sqlStatement, id);
	}
	
	/**
	 * 根据某个property属性得到记录数  (countByProperty)
	 * @param property 属性参数
	 * @return
	 */
	public Integer countByProperty(Object property) {
		return getSqlSession().selectOne(this.clazzName + POSTFIX_COUNTBYPROPERTY, property);
	}
	
	/**
	 * 根据某个property属性得到记录数
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param property
	 * @return
	 */
	public Integer countByProperty(String sqlStatement, Object property) {
		return getSqlSession().selectOne(this.clazzName + "." + sqlStatement, property);
	}
	
	/**
	 * 通过map得到记录数  (countByMap)
	 * @param map 参数
	 * @return
	 */
	public Integer countByMap(Map map) {
		if(map == null){
			return getSqlSession().selectOne(this.clazzName + POSTFIX_COUNTBYMAP);
		} else {
			return getSqlSession().selectOne(this.clazzName + POSTFIX_COUNTBYMAP, map);
		}
	}
	
	/**
	 * 通过map得到记录数
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param map
	 * @return
	 */
	public Integer countByMap(String sqlStatement, Map map) {
		if(map == null){
			return getSqlSession().selectOne(this.clazzName + "." + sqlStatement);
		} else {
			return getSqlSession().selectOne(this.clazzName + "." + sqlStatement, map);
		}
	}
	
	/**
	 * 通过map得到记录数  (queryPageCount)
	 * @param map 参数
	 * @return
	 */
	public Integer queryPageCount(Map map) {
		if(map == null){
			return getSqlSession().selectOne(this.clazzName + POSTFIX_QUERYPAGECOUNT);
		} else {
			return getSqlSession().selectOne(this.clazzName + POSTFIX_QUERYPAGECOUNT, map);
		}
	}
	
	/**
	 * 通过map得到记录数
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param map
	 * @return
	 */
	public Integer queryPageCount(String sqlStatement, Map map) {
		if(map == null){
			return getSqlSession().selectOne(this.clazzName + "." + sqlStatement);
		} else {
			return getSqlSession().selectOne(this.clazzName + "." + sqlStatement, map);
		}
	}

	/**
	 * 分页查询  (queryPage)
	 * @param map 参数
	 * @param page 分页对象
	 * @return
	 */
	public Page<T> pageQueryBy(Map<String, Object> map, Page<T> page) {
		page.setTotalCount(Long.valueOf(queryPageCount(map).toString()));
		if(map == null){
			map = new HashMap<String, Object>();
		}
		map.put("orderBy", page.getOrderBy());
		map.put("order", page.getOrder());
		page.setResult((List<T>) getSqlSession().selectList(this.clazzName + POSTFIX_QUERYPAGE, map, new RowBounds(page.getPageSize()*(page.getPageNo()-1), page.getPageSize())));
		return page ;
	}
	
	/**
	 * 分页查询
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param map
	 * @param page
	 * @return
	 */
	public Page<T> pageQueryBy(String sqlStatement, Map<String, Object> map, Page<T> page) {
		page.setTotalCount(Long.valueOf(queryPageCount(sqlStatement + "Count", map).toString()));
		if(map == null){
			map = new HashMap<String, Object>();
		}
		map.put("orderBy", page.getOrderBy());
		map.put("order", page.getOrder());
		page.setResult((List<T>) getSqlSession().selectList(this.clazzName + "." + sqlStatement, map, new RowBounds(page.getPageSize()*(page.getPageNo()-1), page.getPageSize())));
		return page ;
	}
	
	/**
	 * 通过obj得到记录数  (queryPageCount)
	 * @param obj 参数
	 * @return
	 */
	public Integer queryPageCount(Object obj) {
		if(obj == null){
			return getSqlSession().selectOne(this.clazzName + POSTFIX_QUERYPAGECOUNT);
		} else {
			return getSqlSession().selectOne(this.clazzName + POSTFIX_QUERYPAGECOUNT, obj);
		}
	}
	
	/**
	 * 分页查询  (queryPage)
	 * @param obj 参数
	 * @param page 分页对象
	 * @return
	 */
	public Page<T> pageQueryBy(Object obj, Page<T> page) {
		page.setTotalCount(Long.valueOf(queryPageCount(obj).toString()));
		page.setResult((List<T>) getSqlSession().selectList(this.clazzName + POSTFIX_QUERYPAGE, obj, new RowBounds(page.getPageSize()*(page.getPageNo()-1), page.getPageSize())));
		return page ;
	}
	
	/**
	 * 通过map得到记录数
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @return
	 */
	public Integer queryPageCount(String sqlStatement, Object obj) {
		if(obj == null){
			return getSqlSession().selectOne(this.clazzName + "." + sqlStatement);
		} else {
			return getSqlSession().selectOne(this.clazzName + "." + sqlStatement, obj);
		}
	}
	
	/**
	 * 分页查询
	 * @param sqlStatement mybatis文件中sql mapper 的id
	 * @param page
	 * @return
	 */
	public Page<T> pageQueryBy(String sqlStatement, Object obj, Page<T> page) {
		page.setTotalCount(Long.valueOf(queryPageCount(sqlStatement + "Count", obj).toString()));
		page.setResult((List<T>) getSqlSession().selectList(this.clazzName + "." + sqlStatement, obj, new RowBounds(page.getPageSize()*(page.getPageNo()-1), page.getPageSize())));
		return page ;
	}

	public List<T> query(PageView pageView, T t) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("paging", pageView);
		
		map.put("t", t);
		return getSqlSession().selectList(this.clazzName+"."+"query" ,map);
	}
	
	
	public List<T> querySon(PageView pageView,T t) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("paging", pageView);
		map.put("t", t);
		return getSqlSession().selectList(this.clazzName+"."+"querySon" ,map);
	}
	
	public List<T> queryList(String sqlStatement,PageView pageView,T t) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("paging", pageView);
		map.put("t", t);
		return getSqlSession().selectList(this.clazzName+"."+sqlStatement ,map);
	}
	
	public List<T> queryGoodsList(PageView pageView,Map m) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("paging", pageView);
		map.put("t", m);
		return getSqlSession().selectList(this.clazzName+"."+"queryGoodsList" ,map);
	}
	
	public List<T> queryGoodsListByBrandId(PageView pageView,Map m) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("paging", pageView);
		map.put("t", m);
		return getSqlSession().selectList(this.clazzName+"."+"queryGoodsListByBrandId" ,map);
	}
	public List pageList(String sql,PageView pageView,Map m) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("paging", pageView);
		map.put("t", m);
		return getSqlSession().selectList(sql,map,new RowBounds(pageView.getPageSize()*(pageView.getPageNow()-1), pageView.getPageSize()));
	}
	public List<Map<String,?>> findListPageList(String sql,Map param,PageView pageView) {
		return  getSqlSession().selectList(sql, param,new RowBounds(pageView.getPageSize()*(pageView.getPageNow()-1), pageView.getPageSize()));
	}
	public int findListPageListCount(String sql,Map param) {
		return  getSqlSession().selectOne(sql, param);
	}
	public List<T> findList(String sql,Map param) {
		return  getSqlSession().selectList(sql, param);
	}
	public List findListNopage(String sql,Map param) {
		return  getSqlSession().selectList(sql, param);
	}

	public int insertNoT(String sql,List param) {
		return getSqlSession().insert(sql, param);
	}
	public int insertNoT(String sql,Map<String,?> param) {
		return getSqlSession().insert(sql, param);
	}
	public int updateNoT(String sql,Map param) {
		return getSqlSession().update(sql, param);
	}
	public int deleteNoT(String sql,Map param) {
		return getSqlSession().delete(sql, param);
	}

}